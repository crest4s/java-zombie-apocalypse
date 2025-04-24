package Helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApocalipsisLogger {
    private static ApocalipsisLogger instance;
    private static final Object lock = new Object();
    private final BufferedWriter writer;
    private static final String FILE_NAME = "apocalipsis.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ApocalipsisLogger() throws IOException {
        writer = new BufferedWriter(new FileWriter(FILE_NAME, true)); // append = true
    }

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

    public void log(String mensaje) {
        System.out.println(mensaje);
        String timestamp = LocalDateTime.now().format(formatter);
        String linea = timestamp + " - " + mensaje;
        synchronized (lock) {
            try {
                writer.write(linea);
                writer.newLine();
                writer.flush(); // importante para que se guarde al momento
            } catch (IOException e) {
                System.err.println("Error escribiendo en el log: " + e.getMessage());
            }
        }
    }

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

