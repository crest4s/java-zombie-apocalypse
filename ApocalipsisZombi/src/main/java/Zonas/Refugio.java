package Zonas;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import Helpers.ApocalipsisLogger;

public class Refugio {

    private int cantidadComida;
    private final Semaphore semaforoComida;
    private Zona[] zonasRefugio = {Zona.DESCANSO, Zona.COMEDOR, Zona.ZONA_COMUN};
    
    public Refugio(){
        this.cantidadComida = 0;
        this.semaforoComida = new Semaphore(1);
    }
    
    public void dejarComida(int comida, String id) throws InterruptedException, IOException{
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        semaforoComida.acquire();
        cantidadComida += comida;
        logger.log("]" + id + "] ha dejado comida en el refugio");
        semaforoComida.release();
    }
    
    public synchronized void cogerComida(String id) throws InterruptedException, IOException{
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        while (cantidadComida<=0){
            wait();
        }
        cantidadComida--;
        logger.log("[" + id + "] ha cogido un alimento del refugio.");
        notify();
    }
    
    public int getCantidadComida(){
        return cantidadComida; 
    }
}
