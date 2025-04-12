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
    private Tunel tuneles[];
    private Zona zonaActual;
    private final String id;
    private boolean marcado;
    private final Random random = new Random();
    
    public Humano(IdGenerator idgen, Refugio ref, Tunel tun[]){
        this.ref = ref;
        this.tuneles = tun;     
        this.zonaActual = Zona.ZONA_COMUN;
        this.id = idgen.nuevoIdHumano();
        this.marcado = false;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                //Zona Comun
                zonaActual = Zona.ZONA_COMUN;
                log("Preparandose para entrar al mundo exterior...");
                sleepRandom(1000, 2000);
                
                 // Selecciona tunel y espera a grupo (a implementar)
                int indiceTunel = random.nextInt(tuneles.length);
                Tunel tunel = tuneles[indiceTunel];

                log("esperando grupo en túnel " + (indiceTunel + 1));
                tunel.esperarGrupoParaSalir(this); // esperar grupo de 3 para salir del refugio

                zonaActual = tunel.getZona(); // se obtiene la zona del tunel
                tunel.entrarAlTunelDesdeRefugio(this);
                sleep(1000); // cruzar túnel
                
                // EXPLORACIÓN EXTERIOR
                zonaActual = tunel.getAreaInsegura(); 
                log("recolectando comida en " + zonaActual);
                sleepRandom(3000, 5000);

                // SIMULAR ATAQUE
                boolean atacado = random.nextBoolean();
                if (atacado) {
                    log("está siendo atacado por un zombi...");
                    sleepRandom(500, 1500);
                    boolean seDefiende = random.nextInt(3) < 2;
                    if (seDefiende) {
                        marcado = true;
                        log("logró defenderse.");
                    } 
                    else {
                        log("ha muerto y se convierte en zombi.");
                        // zombificación se haría aquí
                        break;
                    }
                } 
                else {
                    log("recolectó 2 piezas de comida.");
                    ref.dejarComida(2, id);
                }

                // REGRESO
                tunel.entrarAlTunelDesdeExterior(this);
                sleep(1000); // cruzar túnel
                zonaActual = Zona.DESCANSO;

                // DESCANSO
                log("descansa.");
                sleepRandom(2000, 4000);

                // COMER
                zonaActual = Zona.COMEDOR;
                log("intentando comer.");
                ref.cogerComida(id); // bloqueará si no hay comida
                sleepRandom(3000, 5000);

                // RECUPERACIÓN SI FUE MARCADO
                if (marcado) {
                    zonaActual = Zona.DESCANSO;
                    log("se recupera de las heridas.");
                    sleepRandom(3000, 5000);
                    marcado = false;
                }

                // VUELTA A ZONA COMÚN
                zonaActual = Zona.ZONA_COMUN;

            } catch (InterruptedException e) {
                log("interrumpido.");
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
