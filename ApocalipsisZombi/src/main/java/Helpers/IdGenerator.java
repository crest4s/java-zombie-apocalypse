package Helpers;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private final AtomicInteger humanoCounter = new AtomicInteger(1);

    public String nuevoIdHumano() {
        return String.format("H%05d", humanoCounter.getAndIncrement());
    }
    
    public String nuevoIdZombi(String idHumano) {
        if (idHumano != null && idHumano.startsWith("H")) {
            return "Z" + idHumano.substring(1); // Reemplaza la 'H' por 'Z'
        } else {
            throw new IllegalArgumentException("ID de humano inválido: " + idHumano);
        }
    }
}
