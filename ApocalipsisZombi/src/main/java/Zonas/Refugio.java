package Zonas;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import Helpers.ApocalipsisLogger;
import UI.ActualizadorGUI;

public class Refugio {

    private int cantidadComida;
    private final Semaphore semaforoComida;
    private Zona[] zonasRefugio = {Zona.DESCANSO, Zona.COMEDOR, Zona.ZONA_COMUN};
    private ActualizadorGUI act;
    
    public Refugio(){
        this.cantidadComida = 0;
        this.semaforoComida = new Semaphore(1);
    }
    
    public void setActualizador(ActualizadorGUI act){
        this.act = act;
    }
    
    public void dejarComida(int comida, String id) throws InterruptedException, IOException{
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        semaforoComida.acquire();
        cantidadComida += comida;
        logger.log("[" + id + "] ha dejado comida en el refugio");
        semaforoComida.release();
        act.actualizarComida();
    }
    
    public synchronized void cogerComida(String id) throws InterruptedException, IOException{
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        while (cantidadComida<=0){
            wait();
        }
        cantidadComida--;
        act.actualizarComida();
        logger.log("[" + id + "] ha cogido un alimento del refugio.");
        notify();
    }
    
    public int getCantidadComida(){
        return cantidadComida; 
    }
}
