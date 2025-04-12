/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author crestas
 */
public class Humano extends Thread{
    private Refugio ref;
    private Tunel tun[];
    private Zona zonaActual;
    private final String id;
    private boolean atacado;
    private final Random random = new Random();
    
    public Humano(IdGenerator idgen, Refugio ref, Tunel tun[]){
        this.ref = ref;
        this.tun = tun;     
        this.zonaActual = Zona.ZONA_COMUN;
        this.id = idgen.nuevoIdHumano();
        this.atacado = false;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                //Zona Comun
                zonaActual = Zona.ZONA_COMUN;
                log("Preparandose para entrar al mundo exterior...");
                sleepRandom(1000, 2000);
                
                //Random para ir a un tunel
                int tunel = random.nextInt(4)+1;
                ref.esperarTunel(tunel);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
        }
    }
    
    public Zona getZonaActual(){
        return zonaActual;
    }
    
    public String getIdHumano() {
        return id;
    }
    
    public void setZonaActual(Zona zona){
        this.zonaActual = zona;
    }
    private void sleepRandom(int min, int max) throws InterruptedException {
        sleep(min + random.nextInt(max - min + 1));
    }

    private void log(String msg) {
        System.out.println("[" + id + "] " + msg);
    }
}
