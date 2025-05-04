package backend.zones;

import backend.entities.Humano;
import backend.utils.ApocalipsisLogger;
import frontend.server.ActualizadorGUI;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Representa un túnel que conecta el refugio con una zona de riesgo.
 * Controla el acceso de los humanos en ambos sentidos con sincronización de grupos y semáforos.
 */
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
    private final MapaZonas mapa;

    /**
     * Crea un túnel con un identificador y una referencia al mapa de zonas.
     *
     * @param id identificador del túnel (1-4)
     * @param mapa instancia compartida del mapa de zonas
     */
    public Tunel(int id, MapaZonas mapa) {
        this.id = id;
        this.mapa = mapa;
        this.zona = switch (id) {
            case 1 -> Zona.TUNEL_1;
            case 2 -> Zona.TUNEL_2;
            case 3 -> Zona.TUNEL_3;
            case 4 -> Zona.TUNEL_4;
            default -> throw new IllegalArgumentException("ID de túnel no válido");
        };
    }

    /**
     * Establece el actualizador de GUI que notifica cambios de estado.
     *
     * @param act instancia de {@link ActualizadorGUI}
     */
    public void setActualizador(ActualizadorGUI act) {
        this.act = act;
    }

    /**
     * Devuelve la zona asociada a este túnel.
     *
     * @return zona del túnel
     */
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

    /**
     * Hace que un humano espere en la cola del refugio hasta que se forme un grupo de 3 personas.
     *
     * @param h humano que espera
     * @throws IOException si hay error en el log
     * @throws InterruptedException si se interrumpe el hilo
     * @throws BrokenBarrierException si falla la barrera cíclica
     */
    public void esperarGrupoParaSalir(Humano h) throws IOException, InterruptedException, BrokenBarrierException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        synchronized (monitor) {
            colaRefugio.add(h);
            mapa.quitarHumanoZona(h, h.getZonaActual());
            h.setZonaActual(Zona.TRANSITO);
            act.actualizarColas(zona);
            logger.log("[" + h.getIdHumano() + "] esperando en la cola del refugio en " + zona.name());
        }

        barrier.await(); // espera a formar grupo

        synchronized (monitor) {
            grupoFormado.add(h);
            if (grupoFormado.size() == 3) {
                grupoActivo = true;
                monitor.notifyAll(); // notifica a todos los del grupo
            }
        }
    }

    /**
     * Controla la entrada de un humano al túnel, desde el refugio o desde la zona de riesgo.
     *
     * @param h humano que desea entrar
     * @param desdeExterior true si viene de la zona de riesgo
     * @throws InterruptedException si se interrumpe el hilo
     * @throws IOException si hay error en el log
     */
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
                colaRiesgo.remove(h);
                act.actualizarColas(zona);
                setHumanoDentro(h);
                act.actualizarTunel(this);
                h.setZonaActual(zona);
                logger.log("[" + h.getIdHumano() + "] entra al túnel desde el exterior.");
            }
        } else {
            synchronized (monitor) {
                while (true) {
                    if (!grupoFormado.isEmpty() && grupoFormado.peek().equals(h) && accesoTunel.availablePermits() > 0) {
                        accesoTunel.acquire();
                        grupoFormado.poll();
                        colaRefugio.remove(h);
                        act.actualizarColas(zona);
                        setHumanoDentro(h);
                        act.actualizarTunel(this);
                        h.setZonaActual(zona);
                        logger.log("[" + h.getIdHumano() + "] entra al túnel desde el refugio.");
                        break;
                    } else {
                        monitor.wait();
                    }
                }
            }
        }
    }

    /**
     * Controla la salida del humano del túnel y actualiza su estado.
     *
     * @param h humano que sale del túnel
     * @throws IOException si hay error en el log
     */
    public void salirTunel(Humano h) throws IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();

        synchronized (monitor) {
            if (humanoEnTunel == h) {
                humanoEnTunel = null;
            }

            accesoTunel.release();
            act.actualizarTunel(this);
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

    /**
     * Devuelve la zona de riesgo asociada a este túnel.
     *
     * @return zona de riesgo correspondiente
     */
    public Zona getAreaInsegura() {
        return switch (id) {
            case 1 -> Zona.RIESGO_1;
            case 2 -> Zona.RIESGO_2;
            case 3 -> Zona.RIESGO_3;
            case 4 -> Zona.RIESGO_4;
            default -> throw new IllegalArgumentException("ID de túnel no válido");
        };
    }

    /**
     * Elimina a un humano de todas las colas y del túnel si estuviera dentro.
     * Se usa por ejemplo cuando el humano muere.
     *
     * @param h humano a eliminar
     */
    public void eliminarHumanoDeColas(Humano h) {
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
            monitor.notifyAll();
        }
    }
}
