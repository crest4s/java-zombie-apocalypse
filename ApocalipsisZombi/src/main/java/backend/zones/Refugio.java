package backend.zones;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import backend.utils.ApocalipsisLogger;
import frontend.server.ActualizadorGUI;

/**
 * Representa el refugio donde los humanos pueden dejar y recoger comida.
 * Gestiona el acceso concurrente a los recursos usando sincronización y semáforos.
 */
public class Refugio {

    private int cantidadComida;
    private final Semaphore semaforoComida;
    private ActualizadorGUI act;

    /**
     * Crea un refugio vacío, sin comida inicialmente.
     * Se utiliza un semáforo para controlar el acceso concurrente a la comida.
     */
    public Refugio() {
        this.cantidadComida = 0;
        this.semaforoComida = new Semaphore(1);
    }

    /**
     * Establece el actualizador de la interfaz gráfica que será notificado cuando cambie el estado del refugio.
     *
     * @param act instancia de {@link ActualizadorGUI}
     */
    public void setActualizador(ActualizadorGUI act) {
        this.act = act;
    }

    /**
     * Permite a un humano dejar comida en el refugio.
     * El acceso está protegido por un semáforo para evitar condiciones de carrera.
     *
     * @param comida cantidad de comida a dejar
     * @param id     identificador del humano que deja la comida
     * @throws InterruptedException si el hilo es interrumpido mientras espera el semáforo
     * @throws IOException si ocurre un error al escribir en el log
     */
    public void dejarComida(int comida, String id) throws InterruptedException, IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        semaforoComida.acquire();
        cantidadComida += comida;
        logger.log("[" + id + "] ha dejado comida en el refugio");
        semaforoComida.release();
        act.actualizarComida();
    }

    /**
     * Permite a un humano recoger comida del refugio.
     * Si no hay comida disponible, el hilo espera hasta que se le notifique.
     *
     * @param id identificador del humano que intenta comer
     * @throws InterruptedException si el hilo es interrumpido mientras espera comida
     * @throws IOException si ocurre un error al escribir en el log
     */
    public synchronized void cogerComida(String id) throws InterruptedException, IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        while (cantidadComida <= 0) {
            wait();
        }
        cantidadComida--;
        act.actualizarComida();
        logger.log("[" + id + "] ha cogido un alimento del refugio.");
        notify();
    }

    /**
     * Devuelve la cantidad actual de comida almacenada en el refugio.
     *
     * @return número de unidades de comida
     */
    public int getCantidadComida() {
        return cantidadComida;
    }
}
