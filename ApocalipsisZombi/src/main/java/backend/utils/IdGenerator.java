package backend.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase encargada de generar identificadores únicos para humanos y zombis
 * dentro del entorno de simulación.
 */
public class IdGenerator {
    private final AtomicInteger humanoCounter = new AtomicInteger(1);

    /**
     * Genera un nuevo ID único para un humano.
     * El formato es "H0001", "H0002", etc.
     *
     * @return ID único de humano
     */
    public String nuevoIdHumano() {
        return String.format("H%04d", humanoCounter.getAndIncrement());
    }

    /**
     * Genera un nuevo ID de zombi basado en un ID de humano.
     * El formato transforma la letra 'H' inicial en 'Z'.
     * Por ejemplo, "H0023" se convierte en "Z0023".
     *
     * @param idHumano ID del humano original
     * @return ID del zombi resultante
     * @throws IllegalArgumentException si el ID no comienza con 'H'
     */
    public String nuevoIdZombi(String idHumano) {
        if (idHumano != null && idHumano.startsWith("H")) {
            return "Z" + idHumano.substring(1); // Reemplaza la 'H' por 'Z'
        } else {
            throw new IllegalArgumentException("ID de humano inválido: " + idHumano);
        }
    }
}
