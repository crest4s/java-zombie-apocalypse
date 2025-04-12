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
        while (true) {
            try {
                // Moverse a otra zona
                int zona = random.nextInt(4) + 1;
                zonaActual = Zona.valueOf("RIESGO_" + zona);
                log("entra a " + zonaActual);

                // Buscar humanos (esto luego irá conectado a mapa real)
                boolean hayHumanos = random.nextBoolean();

                if (hayHumanos) {
                    log("atacando a un humano...");
                    sleepRandom(500, 1500);
                    muertes++;
                    log("mató a un humano. Total muertes: " + muertes);
                } 
                else {
                    log("no hay nadie. Espera...");
                    sleepRandom(2000, 3000);
                }

            } catch (InterruptedException e) {
                log("interrumpido.");
                break;
            }
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
