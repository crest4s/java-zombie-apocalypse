/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try {
            ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
            while(true){
                try {
                    // Zona Comun
                    setZonaActual(Zona.ZONA_COMUN); //Actualizar posicion a zona comun
                    logger.log("[" + id + "]" + "Preparandose para entrar al mundo exterior...");
                    sleepRandom(1000, 2000);
                    
                    // Selecciona tunel y espera a grupo (a implementar)
                    int indiceTunel = random.nextInt(tuneles.length);
                    Tunel tunel = tuneles[indiceTunel];
                    
                    logger.log("[" + id + "]" + "esperando grupo en túnel" + (indiceTunel + 1));
                    setSeguraEspera(indiceTunel);
                    tunel.esperarGrupoParaSalir(this); // Esperar grupo de 3 para salir del refugio
                    
                    Zona zonaTunel = tunel.getZona();// Se obtiene la zona del tunel
                    tunel.entrarAlTunelDesdeRefugio(this);
                    sleep(1000); // Cruzar túnel
                    tunel.salirTunel(this);
                    
                    // EXPLORACIÓN EXTERIOR
                    Zona InseguraActual = tunel.getAreaInsegura();
                    setZonaActual(InseguraActual);
                    logger.log("[" + id + "]" + "recolectando comida en " + zonaActual);
                    sleepRandom(3000, 5000);
                    
                    // SIMULAR ATAQUE
                    boolean atacado = random.nextBoolean();
                    if (atacado) {
                        logger.log("[" + id + "]" + "está siendo atacado por un zombi...");
                        sleepRandom(500, 1500);
                        boolean seDefiende = random.nextInt(3) < 2;
                        if (seDefiende) {
                            marcado = true;
                            logger.log("[" + id + "]" + "logró defenderse."); 
                        }
                        else {
                            logger.log("[" + id + "]" + "ha sido herido de muerte y está colapsando...");
                            return; // Finaliza el hilo humano, el zombi se encargará de zombificar (el return lo saca del run)

                        }
                    }
                    else {
                        logger.log("[" + id + "]" + "recolectó 2 piezas de comida.");
                        ref.dejarComida(2, id);
                    }
                    
                    // REGRESO
                    setInseguraEspera(indiceTunel);
                    tunel.entrarAlTunelDesdeExterior(this);
                    sleep(1000); // cruzar túnel
                    tunel.salirTunel(this);

                    // DESCANSO
                    setZonaActual(Zona.DESCANSO);
                    logger.log("[" + id + "]" + "descansa.");
                    sleepRandom(2000, 4000);
                    
                    // COMER
                    setZonaActual(Zona.COMEDOR);
                    logger.log("[" + id + "]" + "intentando comer.");
                    ref.cogerComida(id); // bloqueará si no hay comida
                    sleepRandom(3000, 5000);

                    // RECUPERACIÓN SI FUE MARCADO
                    if (marcado) {
                        setZonaActual(Zona.DESCANSO);
                        logger.log("[" + id + "]" + "se recupera de las heridas.");
                        sleepRandom(3000, 5000);
                        marcado = false;
                    }
                    
                    // VUELTA A ZONA COMÚN
                    setZonaActual(Zona.ZONA_COMUN);
                    
                } 
                catch (InterruptedException | BrokenBarrierException e) {
                    logger.log("[" + id + "]" + "interrumpido."); // Si el humano muere se interrumpe por "interrupt()"
                    break;
                }
            }
        } 
        catch (IOException ex) {}
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
    
    public boolean serAtacadoPor(Zombi atacante) throws InterruptedException, IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        logger.log("[" + id + "]" + "está siendo atacado por " + atacante.getIdZombi());

        sleepRandom(500, 1500);

        boolean seDefiende = random.nextInt(3) < 2;

        if (seDefiende) {
            marcado = true;
            logger.log("[" + id + "]" + "logró defenderse del zombi " + atacante.getIdZombi());
            return false; // NO murió
        } else {
            logger.log("[" + id + "]" + "no logró defenderse... ha muerto.");
            return true; // Murió
            
        }
    }
    
    public void setSeguraEspera(int i){
        switch (i){
            case 1 -> setZonaActual(Zona.ESPERA_REFUGIO_1);
            case 2 -> setZonaActual(Zona.ESPERA_REFUGIO_2);
            case 3 -> setZonaActual(Zona.ESPERA_REFUGIO_3);
            case 4 -> setZonaActual(Zona.ESPERA_REFUGIO_4);
        }
    }
    public void setInseguraEspera(int i){
        switch (i){
            case 1 -> setZonaActual(Zona.ESPERA_RIESGO_1);
            case 2 -> setZonaActual(Zona.ESPERA_RIESGO_2);
            case 3 -> setZonaActual(Zona.ESPERA_RIESGO_3);
            case 4 -> setZonaActual(Zona.ESPERA_RIESGO_4);
        }
    }

}
