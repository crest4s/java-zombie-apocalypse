package Zonas;

import Entidades.Humano;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import Helpers.ApocalipsisLogger;
import UI.ActualizadorGUI;

public class Tunel {
    private final int id;
    private final Zona zona;
    private final CyclicBarrier barrier = new CyclicBarrier(3);
    private final Semaphore accesoTunel = new Semaphore(1);
    private final Object monitor = new Object();
    private boolean grupoActivo = false;
    private final Queue<Humano> colaRefugio = new LinkedList<>();
    private final Queue<Humano> colaRiesgo = new LinkedList<>();
    private final Queue<Humano> grupoFormado = new LinkedList<>();
    private Humano humanoEnTunel = null;
    private ActualizadorGUI act;
    private MapaZonas mapa;

    public Tunel(int id, MapaZonas mapa) {
        this.id = id;
        this.mapa = mapa;
        switch (id) {
            case 1 -> this.zona = Zona.TUNEL_1;
            case 2 -> this.zona = Zona.TUNEL_2;
            case 3 -> this.zona = Zona.TUNEL_3;
            case 4 -> this.zona = Zona.TUNEL_4;
            default -> throw new IllegalArgumentException("ID de túnel no válido");
        }
    }
    
    public void setActualizador(ActualizadorGUI act){
        this.act = act;
    }
    
    public Zona getZona() {
        return zona;
    }

    public synchronized Queue<Humano> getColaRefugio() {
        return colaRefugio;
    }

    public synchronized Queue<Humano> getColaRiesgo() {
        return colaRiesgo;
    }

    public synchronized Humano getHumanoDentro() {
        return humanoEnTunel;
    }

    public synchronized void setHumanoDentro(Humano h) {
        humanoEnTunel = h;
    }
    
    public synchronized Humano getCruzando() {
        return humanoEnTunel;
    }

    public void esperarGrupoParaSalir(Humano h) throws IOException, InterruptedException, BrokenBarrierException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        synchronized (monitor) {
            colaRefugio.add(h);
            mapa.quitarHumanoZona(h, h.getZonaActual()); //poner al humano en zona transito para evitar problemas en interfaz
            h.setZonaActual(Zona.TRANSITO);
            act.actualizarColas(zona);
            logger.log("[" + h.getIdHumano() + "] esperando en la cola del refugio en " + zona.name());
        }
        barrier.await(); // espera a formar grupo de 3
        synchronized (monitor) {
            grupoFormado.add(h);
            if (grupoFormado.size() == 3) {
                grupoActivo = true;
                monitor.notifyAll(); // avisamos a los que estén esperando
            }
        }
    }

    public void entrarTunel(Humano h, boolean desdeExterior) throws InterruptedException, IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();

        if (desdeExterior) {
            synchronized (monitor) {
                colaRiesgo.add(h);
                act.actualizarColas(zona);
                logger.log("[" + h.getIdHumano() + "] espera desde el exterior en " + zona.name());
            }
            accesoTunel.acquire();

            synchronized (monitor) {
                colaRiesgo.remove(h); //sale de las colas
                act.actualizarColas(zona); 
                setHumanoDentro(h); //entra en el tunel
                act.actualizarTunel(this);
                h.setZonaActual(zona);
                logger.log("[" + h.getIdHumano() + "] entra al túnel desde el exterior.");
            }
        } else {
            synchronized (monitor) {
                while (true) {
                    if (!grupoFormado.isEmpty() && grupoFormado.peek().equals(h) && accesoTunel.availablePermits() > 0) {
                        accesoTunel.acquire(); //se reserva el acceso
                        grupoFormado.poll(); //sale del grupo formado
                        colaRefugio.remove(h); //sale de las colas
                        act.actualizarColas(zona); 
                        setHumanoDentro(h); //entra al tunel
                        act.actualizarTunel(this);
                        h.setZonaActual(zona);
                        logger.log("[" + h.getIdHumano() + "] entra al túnel desde el refugio.");
                        break;
                    } else {
                        monitor.wait(); //si no soy el primero, espero
                    }
                }
            }
        }
    }

    public void salirTunel(Humano h) throws IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();

        synchronized (monitor) {
            

            if (humanoEnTunel == h) {
                humanoEnTunel = null;
            } // 1. Vaciar referencia
            
            accesoTunel.release();
            act.actualizarTunel(this); // 2. Refrescar visualización
            logger.log("[" + h.getIdHumano() + "] sale del " + zona.name());
            
            if (grupoFormado.isEmpty() && barrier.getNumberWaiting() == 0) {
                try {
                    barrier.reset();
                } catch (IllegalStateException e) {
                    logger.log("[" + h.getIdHumano() + "] fallo al resetear el barrier.");
                }
                grupoActivo = false;
            }
            monitor.notifyAll();
        }
}

    public Zona getAreaInsegura() {
        return switch (id) {
            case 1 -> Zona.RIESGO_1;
            case 2 -> Zona.RIESGO_2;
            case 3 -> Zona.RIESGO_3;
            case 4 -> Zona.RIESGO_4;
            default -> throw new IllegalArgumentException("ID de túnel no válido");
        };
    }
    
    public void eliminarHumanoDeColas(Humano h){
        synchronized (monitor) {
            colaRefugio.remove(h);
            colaRiesgo.remove(h);
            grupoFormado.remove(h);

            if (humanoEnTunel == h) {
                humanoEnTunel = null;
                accesoTunel.release();
                act.actualizarTunel(this);
            }

            act.actualizarColas(zona);
            monitor.notifyAll(); // desbloquear esperas
        }
    }
}
