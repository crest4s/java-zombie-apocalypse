/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.util.Random;

/**
 *
 * @author crestas
 */
public class Zombi extends Thread{
    private String id;
    private int muertes;
    public Zona zonaActual;
    private final Random random = new Random();
    
    public Zombi(IdGenerator idgen){
        this.id = idgen.nuevoIdZombi();
        this.muertes = 0;
    }
    
    @Override
    public void run(){
        while(true){
            
        }
    }
    
    public Zona getZonaActual(){
        return zonaActual;
    }
    
    public void setZonaActual(Zona zona){
        this.zonaActual = zona;
    }
    
    public String getIdZombi() {
        return id;
    }
     
    private void sleepRandom(int min, int max) throws InterruptedException {
        sleep(min + random.nextInt(max - min + 1));
    }

    private void log(String msg) {
        System.out.println("[" + id + "] " + msg);
    }

    
}
