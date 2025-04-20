/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

/**
 *
 * @author crestas
 */
public class Humano extends Thread{
    private final Refugio ref;
    private final Tunel tuneles[];
    private Zona zonaActual;
    private final String id;
    private boolean marcado;
    private final Random random = new Random();
    private final MapaZonas mapa;
    
    public Humano(IdGenerator idgen, Refugio ref, Tunel tun[], MapaZonas mapa){
        this.ref = ref;
        this.tuneles = tun;     
        this.zonaActual = Zona.ZONA_COMUN;
        this.id = idgen.nuevoIdHumano();
        this.marcado = false;
        this.mapa = mapa;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                // Zona Comun
                setZonaActual(Zona.ZONA_COMUN); //Actualizar posicion a zona comun
                log("Preparandose para entrar al mundo exterior...");
                sleepRandom(1000, 2000);
                
                 // Selecciona tunel y espera a grupo (a implementar)
                int indiceTunel = random.nextInt(tuneles.length);
                Tunel tunel = tuneles[indiceTunel];

                log("esperando grupo en túnel " + (indiceTunel + 1));
                tunel.esperarGrupoParaSalir(this); // Esperar grupo de 3 para salir del refugio

                Zona zonaTunel = tunel.getZona();// Se obtiene la zona del tunel
                setZonaActual(zonaTunel);// Actualizar posicion al tunel correspondiente
                tunel.entrarAlTunelDesdeRefugio(this);
                sleep(1000); // Cruzar túnel
                
                // EXPLORACIÓN EXTERIOR
                Zona InseguraActual = tunel.getAreaInsegura(); 
                setZonaActual(InseguraActual);
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
                setZonaActual(zonaTunel);
                sleep(1000); // cruzar túnel
                setZonaActual(Zona.DESCANSO);

                // DESCANSO
                log("descansa.");
                sleepRandom(2000, 4000);

                // COMER
                setZonaActual(Zona.COMEDOR);
                log("intentando comer.");
                ref.cogerComida(id); // bloqueará si no hay comida
                sleepRandom(3000, 5000);

                // RECUPERACIÓN SI FUE MARCADO
                if (marcado) {
                    setZonaActual(Zona.DESCANSO);
                    log("se recupera de las heridas.");
                    sleepRandom(3000, 5000);
                    marcado = false;
                }

                // VUELTA A ZONA COMÚN
                setZonaActual(Zona.ZONA_COMUN);

            } catch (InterruptedException | BrokenBarrierException e) {
                log("interrumpido."); // Si el humano muere se interrumpe por "interrupt()"
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
        mapa.quitarHumanoZona(this, this.zonaActual);
        this.zonaActual = zona;
        mapa.guardarHumano(this, zonaActual);
    }
    private void sleepRandom(int min, int max) throws InterruptedException {
        sleep(min + random.nextInt(max - min + 1));
    }

    private void log(String msg) {
        System.out.println("[" + id + "] " + msg);
    }
    
    public boolean serAtacadoPor(Zombi atacante) throws InterruptedException {
        log("está siendo atacado por " + atacante.getIdZombi());

        sleepRandom(500, 1500);

        boolean seDefiende = random.nextInt(3) < 2;

        if (seDefiende) {
            marcado = true;
            log("logró defenderse del zombi " + atacante.getIdZombi());
            return false; // NO murió
        } else {
            log("no logró defenderse... ha muerto.");
            return true; // Murió
        }
    }

}
