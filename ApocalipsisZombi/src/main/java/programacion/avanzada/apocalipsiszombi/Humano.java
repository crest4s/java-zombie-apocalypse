package programacion.avanzada.apocalipsiszombi;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class Humano extends Thread{
    private final Refugio ref;
    private final Tunel tuneles[];
    private Zona zonaActual;
    private final String id;
    private boolean marcado;
    private final Random random = new Random();
    private final MapaZonas mapa;
    private int comidaRecolectada;
    
    public Humano(IdGenerator idgen, Refugio ref, Tunel tun[], MapaZonas mapa){
        this.ref = ref;
        this.tuneles = tun;     
        this.zonaActual = Zona.ZONA_COMUN;
        this.id = idgen.nuevoIdHumano();
        this.marcado = false;
        this.mapa = mapa;
        this.comidaRecolectada = 0;
    }
    
    @Override
    public void run(){
        try {
            ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
            while(true){
                try {
                    //Entrada a zona comun
                    setZonaActual(Zona.ZONA_COMUN); 
                    logger.log("[" + id + "]" + "Preparandose para entrar al mundo exterior...");
                    sleepRandom(1000, 2000);
                    
                    //Seleccion de tunel
                    int indiceTunel = random.nextInt(tuneles.length);
                    Tunel tunel = tuneles[indiceTunel];
                    
                    //Proceso de espera a grupo para tunel
                    logger.log("[" + id + "]" + "esperando grupo en túnel" + (indiceTunel + 1));
                    setSeguraEspera(indiceTunel);
                    tunel.esperarGrupoParaSalir(this); 
                    
                    //Proceso interior del tunel
                    tunel.entrarTunel(this);
                    sleep(1000);
                    tunel.salirTunel(this);
                    
                    //Exploracion en el exterior
                    Zona InseguraActual = tunel.getAreaInsegura();
                    setZonaActual(InseguraActual);
                    logger.log("[" + id + "]" + "recolectando comida en " + zonaActual);
                    sleepRandom(3000, 5000);
                    
                    //Simulacion de ataque zombi
                    boolean atacado = random.nextBoolean();
                    if (atacado) {
                        logger.log("[" + id + "]" + "está siendo atacado por un zombi...");
                        sleepRandom(500, 1500);
                        boolean seDefiende = random.nextInt(3) < 2;
                        if (seDefiende) {
                            marcado = true;
                            logger.log("[" + id + "]" + "logró defenderse."); 
                        } else {
                            logger.log("[" + id + "]" + "ha sido herido de muerte y está colapsando...");
                            return; // Finaliza el hilo humano, el zombi se encargará de zombificar (el return lo saca del run)                          
                        }
                    } else {
                        comidaRecolectada++;
                        logger.log("[" + id + "]" + "recolectó 2 piezas de comida.");
                    }
                    
                    // Vuelta a la zona segura
                    setInseguraEspera(indiceTunel);
                    tunel.entrarTunel(this);
                    sleep(1000); 
                    tunel.salirTunel(this);

                    //Dejar la comida recolectada
                    if(!atacado){
                        ref.dejarComida(2, id);
                        comidaRecolectada--;
                        logger.log("[" + id + "]" + "dejó 2 piezas de comida.");   
                    }
                    
                    //Entrada a la zona de descanso
                    setZonaActual(Zona.DESCANSO);
                    logger.log("[" + id + "]" + "descansa.");
                    sleepRandom(2000, 4000);
                    
                    //Entrada al comedor
                    setZonaActual(Zona.COMEDOR);
                    logger.log("[" + id + "]" + "intentando comer.");
                    ref.cogerComida(id); // bloqueará si no hay comida
                    sleepRandom(3000, 5000);

                    //Si fue marcado, se recupera
                    if (marcado) {
                        setZonaActual(Zona.DESCANSO);
                        logger.log("[" + id + "]" + "recuperandose de las heridas....");
                        sleepRandom(3000, 5000);
                        marcado = false;
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
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
    
    public boolean serAtacado() throws IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();

        boolean seDefiende = random.nextInt(3) < 2; // 2/3 posibilidad de sobrevivir

        if (seDefiende) {
            marcado = true;
            logger.log("[" + id + "]" + " logró defenderse.");
            return false;
        } else {
            logger.log("[" + id + "]" + " no logró defenderse y ha muerto.");
            return true;
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
