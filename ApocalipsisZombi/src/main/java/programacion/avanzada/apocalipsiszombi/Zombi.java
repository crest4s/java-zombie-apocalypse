/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.util.List;
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
    private MapaZonas mapa;
    
    public Zombi(IdGenerator idgen, String humId, MapaZonas mapa){
        this.id = idgen.nuevoIdZombi(humId);
        this.mapa = mapa;
        this.muertes = 0;
    }
    
    public Zombi(MapaZonas mapa){
        this.muertes = 0;
        this.mapa = mapa;
        this.id = "Z00000";
    }
    @Override
    public void run(){
        while (true) {
            try {
                // Moverse a otra zona
                int zona = random.nextInt(4) + 1;
                zonaActual = Zona.valueOf("RIESGO_" + zona);
                log("entra a " + zonaActual);

                // Buscar humanos en la zona actual
                List<Humano> humanosDisponibles = mapa.humanosEnZona(zonaActual);
                
                if (!humanosDisponibles.isEmpty()) {
                    //Seleccionar humano al que atacar
                    Humano humanoObjetivo = humanosDisponibles.get(random.nextInt(humanosDisponibles.size()));
                    
                    //Atacando al humano
                    log("atacando a humano " + humanoObjetivo.getIdHumano());
                    if(humanoObjetivo.serAtacadoPor(this)){
                        sleepRandom(500, 1500);

                        //Convertir al humano en zombi
                        muertes++;
                        Zombi zombi = convertirEnZombi(humanoObjetivo);
                        log("mató a un humano. Total muertes: " + muertes);
                    } 
                    else {
                        log("el humano se defendió. Sigue con vida.");
                    }

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
        if(this.zonaActual != null) {
            mapa.quitarZombiZona(this, this.zonaActual); // Quitar de la zona anterior
        }
        this.zonaActual = zona;
        mapa.guardarZombi(this, zonaActual); // Agregar a la nueva zona
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
    
    public Zombi convertirEnZombi(Humano h) {
        
        /*
        String idZombi = "Z" + h.getIdHumano().substring(1);
        IdGenerator idgen = new IdGenerator();
        Zona zona = h.getZonaActual();

        // Quitar humano y agregar zombi al mapa
        mapa.quitarHumanoZona(h, zona);

        Zombi zombi = new Zombi(idgen, idZombi, mapa); // ejemplo de constructor
        mapa.guardarZombi(zombi, zona);

        System.out.println(h.getIdHumano() + " ha sido convertido en " + idZombi);
        
        h.interrupt(); //El humano convertido en zombi deja de ejecutar su run()

        return zombi;
        */
        
        // Verificar que el humano no sea ya un zombi
        if (h.getIdHumano().startsWith("Z")) {
            log("Error: Intento de convertir un zombi en zombi: " + h.getIdHumano());
            return null;
        }

        // Crear nuevo zombi usando el ID del humano
        Zombi zombi = new Zombi(new IdGenerator(), h.getIdHumano(), mapa);
        Zona zona = h.getZonaActual();

        // Quitar humano y agregar zombi al mapa
        mapa.quitarHumanoZona(h, zona);
        mapa.guardarZombi(zombi, zona);

        System.out.println(h.getIdHumano() + " ha sido convertido en " + zombi.getIdZombi());

        h.interrupt(); // El humano deja de ejecutar su run()
        zombi.start(); // El nuevo zombi comienza a actuar

        return zombi;
    }

    

    
}
