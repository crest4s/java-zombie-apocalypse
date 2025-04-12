/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author adria
 */
public class IdGenerator {
    private final AtomicInteger humanoCounter = new AtomicInteger(1);
    private final AtomicInteger zombiCounter = new AtomicInteger(0);

    public String nuevoIdHumano() {
        return String.format("H%05d", humanoCounter.getAndIncrement());
    }

    public String nuevoIdZombi() {
        return String.format("Z%05d", zombiCounter.getAndIncrement());
    }
}
