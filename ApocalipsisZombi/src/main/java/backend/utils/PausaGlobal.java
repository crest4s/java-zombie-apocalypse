package backend.utils;

/**
 * Clase singleton que controla el estado de pausa global en la simulación.
 * Permite suspender y reanudar la ejecución coordinada de todos los hilos.
 */
public class PausaGlobal {
    private static final PausaGlobal instancia = new PausaGlobal();
    private boolean pausado = false;

    /**
     * Constructor privado para implementar el patrón Singleton.
     */
    private PausaGlobal() {}

    /**
     * Obtiene la única instancia de {@code PausaGlobal}.
     *
     * @return instancia única de PausaGlobal
     */
    public static PausaGlobal getInstance() {
        return instancia;
    }

    /**
     * Activa el modo de pausa global. Todos los hilos que llamen a {@code esperarSiPausado}
     * quedarán bloqueados hasta que se llame a {@code reanudar()}.
     */
    public synchronized void pausar() {
        pausado = true;
    }

    /**
     * Reanuda la ejecución de todos los hilos que estaban esperando por la pausa.
     * Introduce una pequeña espera para asegurar la consistencia de los estados.
     */
    public synchronized void reanudar() {
        try {
            Thread.sleep(100); // espera de 100ms antes de despertar hilos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // preservar la interrupción
        }
        pausado = false;
        notifyAll();
    }

    /**
     * Método que debe ser llamado por los hilos para comprobar si deben suspenderse.
     * Si el sistema está pausado, el hilo esperará hasta que sea notificado.
     */
    public synchronized void esperarSiPausado() {
        while (pausado) {
            try {
                wait();
            } catch (InterruptedException e) {
                // Propagamos la interrupción para que el hilo se pueda detener si lo necesita
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Indica si actualmente la simulación está en pausa.
     *
     * @return true si está pausado, false en caso contrario
     */
    public synchronized boolean estaPausado() {
        return pausado;
    }
}
