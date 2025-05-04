package backend.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton responsable de registrar eventos en la simulación apocalíptica.
 * Escribe mensajes con marca temporal en un archivo de log y los muestra por consola.
 */
public class ApocalipsisLogger {
    private static ApocalipsisLogger instance;
    private static final Object lock = new Object();
    private final BufferedWriter writer;
    private static final String FILE_NAME = "apocalipsis.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor privado que inicializa el escritor de archivos.
     * El archivo se sobrescribe en cada ejecución (no se agrega al existente).
     *
     * @throws IOException si ocurre un error al abrir el archivo
     */
    private ApocalipsisLogger() throws IOException {
        writer = new BufferedWriter(new FileWriter(FILE_NAME, false));
    }

    /**
     * Obtiene la instancia única del logger (patrón Singleton).
     * 
     * @return instancia de {@code ApocalipsisLogger}
     * @throws IOException si falla la inicialización del archivo de log
     */
    public static ApocalipsisLogger getInstance() throws IOException {
        if (instance == null) {
            synchronized (ApocalipsisLogger.class) {
                if (instance == null) {
                    instance = new ApocalipsisLogger();
                }
            }
        }
        return instance;
    }

    /**
     * Registra un mensaje en el log y lo muestra por consola.
     * El mensaje se precede con una marca de tiempo.
     *
     * @param mensaje mensaje a registrar
     */
    public void log(String mensaje) {
        System.out.println(mensaje);
        String timestamp = LocalDateTime.now().format(formatter);
        String linea = timestamp + " - " + mensaje;
        synchronized (lock) {
            try {
                writer.write(linea);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.err.println("Error escribiendo en el log: " + e.getMessage());
            }
        }
    }

    /**
     * Cierra el archivo de log liberando el recurso asociado.
     * Se recomienda llamar a este método al finalizar la simulación.
     */
    public void cerrar() {
        synchronized (lock) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Error cerrando el log: " + e.getMessage());
            }
        }
    }
}
