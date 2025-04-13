/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 *
 * @author crestas
 */
public class Refugio {

    private int cantidadComida;
    private final Semaphore semaforoComida;
    private Zona[] zonasRefugio = {Zona.DESCANSO, Zona.COMEDOR, Zona.ZONA_COMUN};
    
    public Refugio(){
        this.cantidadComida = 0;
        this.semaforoComida = new Semaphore(1);
    }
    
    public void dejarComida(int comida, String id) throws InterruptedException{
        semaforoComida.acquire();
        cantidadComida += comida;
        logID(id,"Ha dejado comida en el refugio");
        semaforoComida.release();
    }
    
    public synchronized void cogerComida(String id) throws InterruptedException{
        while (cantidadComida<=0){
            wait();
        }
        cantidadComida--;
        logID(id, "Ha cogido un alimento del refugio");
        notify();
    }
    
    private void logID(String id, String msg){
        System.out.println("["+id+"] "+msg);
    }
}
