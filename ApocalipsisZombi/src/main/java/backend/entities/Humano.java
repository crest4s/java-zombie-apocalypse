package backend.entities;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import backend.utils.ApocalipsisLogger;
import backend.utils.IdGenerator;
import backend.utils.PausaGlobal;
import backend.zones.MapaZonas;
import backend.zones.Refugio;
import backend.zones.Tunel;
import backend.zones.Zona;

public class Humano extends Thread {
    private final Refugio ref;
    private final Tunel[] tuneles;
    private Zona zonaActual;
    private final String id;
    private boolean marcado;
    private boolean haSidoAtacado;
    private final Random random = new Random();
    private final MapaZonas mapa;
    private int comidaRecolectada;

    public Humano(IdGenerator idgen, Refugio ref, Tunel[] tuneles, MapaZonas mapa) {
        this.ref = ref;
        this.tuneles = tuneles;
        this.zonaActual = Zona.ZONA_COMUN;
        this.id = idgen.nuevoIdHumano();
        this.marcado = false;
        this.haSidoAtacado = false;
        this.mapa = mapa;
        this.comidaRecolectada = 0;
    }

    @Override
    public void run() {
        try {
            ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
            while (!isInterrupted()) {
                setZonaActual(Zona.ZONA_COMUN);
                logger.log("[" + id + "] preparándose para salir...");
                sleepConPausa(sleepRandom(1000, 2000));
                PausaGlobal.getInstance().esperarSiPausado();
                
                //Seleccion de tunel
                int indiceTunel = random.nextInt(tuneles.length);
                Tunel tunel = tuneles[indiceTunel];

                //Entrada a tunel seleccionado
                logger.log("[" + id + "] esperando en túnel " + tuneles[indiceTunel].getZona().name());
                PausaGlobal.getInstance().esperarSiPausado();
                tunel.esperarGrupoParaSalir(this);
                
                //Cruzar tunel
                PausaGlobal.getInstance().esperarSiPausado();
                tunel.entrarTunel(this, false);
                sleepConPausa(1000);
                
                //Salir de tunel
                PausaGlobal.getInstance().esperarSiPausado();
                tunel.salirTunel(this);
                PausaGlobal.getInstance().esperarSiPausado();

                //Cambio a zona insegura
                setZonaActual(tunel.getAreaInsegura());
                logger.log("[" + id + "] explorando en " + zonaActual);
                sleepConPausa(sleepRandom(3000, 5000));
                PausaGlobal.getInstance().esperarSiPausado();
                
                //Comprobacion de ataque recibido
                if (!haSidoAtacado) {
                    comidaRecolectada += 2;
                    logger.log("[" + id + "] recolectó 2 unidades de comida.");
                }
                
                //Entrada a tunel para refugio
                PausaGlobal.getInstance().esperarSiPausado();
                tunel.entrarTunel(this, true);
                sleepConPausa(1000);
                
                //Salida de tunel 
                PausaGlobal.getInstance().esperarSiPausado();
                tunel.salirTunel(this);
                PausaGlobal.getInstance().esperarSiPausado();

                //Guardar comida si es posible
                if (comidaRecolectada > 0) {
                    ref.dejarComida(2, id);
                    comidaRecolectada = 0;
                }
                
                //Se resetea el estado de atacado
                haSidoAtacado = false;
                
                //
                PausaGlobal.getInstance().esperarSiPausado();
                setZonaActual(Zona.DESCANSO);
                logger.log("[" + id + "] descansando...");
                sleepConPausa(sleepRandom(2000, 4000));
                
                PausaGlobal.getInstance().esperarSiPausado();

                setZonaActual(Zona.COMEDOR);
                logger.log("[" + id + "] intentando comer...");
                ref.cogerComida(id);
                sleepConPausa(sleepRandom(3000, 5000));
                
                PausaGlobal.getInstance().esperarSiPausado();

                if (marcado) {
                    setZonaActual(Zona.DESCANSO);
                    logger.log("[" + id + "] curándose heridas...");
                    sleepConPausa(sleepRandom(3000, 5000));
                    PausaGlobal.getInstance().esperarSiPausado();
                    marcado = false;
                }
                
                PausaGlobal.getInstance().esperarSiPausado();
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            try {
                ApocalipsisLogger.getInstance().log("[" + id + "] interrumpido o muerto.");
            } catch (IOException ignored) {}
            Thread.currentThread().interrupt();
        } catch (IOException e) {}
    }

    public void setZonaActual(Zona nuevaZona) {
        synchronized(this) {
            Zona zonaAnterior = this.zonaActual;
            this.zonaActual = nuevaZona;
            mapa.moverHumano(this, zonaAnterior, nuevaZona);
        }
    }

    public Zona getZonaActual() {
        return zonaActual;
    }

    public String getIdHumano() {
        return id;
    }

    public synchronized boolean serAtacado() throws IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        if (this.isInterrupted()) return false;
        
        if (haSidoAtacado) {
            return false;
        }

        haSidoAtacado = true;
        boolean seDefiende = random.nextInt(3) < 2; // 2/3 chance de defenderse
        if (seDefiende) {
            marcado = true;
            logger.log("[" + id + "] ha sido herido pero sobrevivió al ataque.");
            return false;
        } else {
            mapa.quitarHumanoZona(this, zonaActual);
            logger.log("[" + id + "] fue eliminado por un zombi.");
            for (Tunel tunel : tuneles) {
                tunel.eliminarHumanoDeColas(this);
            }
            interrupt();
            return true;
        }
    }

    private int sleepRandom(int min, int max) throws InterruptedException {
        return min + random.nextInt(max - min + 1);
    }
    
    public void sleepConPausa(long duracionTotal) throws InterruptedException {
        long tiempoRestante = duracionTotal;
        long inicio = System.currentTimeMillis();

        while (tiempoRestante > 0) {
            PausaGlobal.getInstance().esperarSiPausado();

            try {
                Thread.sleep(tiempoRestante);
                break; // finalizó correctamente
            } catch (InterruptedException e) {
                if (PausaGlobal.getInstance().estaPausado()) {
                    PausaGlobal.getInstance().esperarSiPausado(); // espera a ser reanudado
                } else {
                    throw e; // interrupción real, no por pausa
                }
            }

            long ahora = System.currentTimeMillis();
            tiempoRestante = duracionTotal - (ahora - inicio);
        }
    }
}
