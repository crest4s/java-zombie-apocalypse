package backend.zones;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import backend.utils.ApocalipsisLogger;
import frontend.server.ActualizadorServerGUI;

public class Refugio {

    private int cantidadComida;
    private final Semaphore semaforoComida;
    private ActualizadorServerGUI act;
    
    public Refugio(){
        this.cantidadComida = 0;
        this.semaforoComida = new Semaphore(1);
    }
    
    public void setActualizador(ActualizadorServerGUI act){
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
