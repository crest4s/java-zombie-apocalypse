package Entidades;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import Helpers.ApocalipsisLogger;
import Helpers.IdGenerator;
import UI.ApocalipsisGUI;
import Zonas.MapaZonas;
import Zonas.Refugio;
import Zonas.Tunel;
import Zonas.Zona;

public class Humano extends Thread{
    private final Refugio ref;
    private final Tunel tuneles[];
    private Zona zonaActual;
    private final String id;
    private boolean marcado;
    private boolean haSidoAtacado;
    private final Random random = new Random();
    private final MapaZonas mapa;
    private int comidaRecolectada;
    private final ApocalipsisGUI gui;
    
    public Humano(IdGenerator idgen, Refugio ref, Tunel tun[], MapaZonas mapa, ApocalipsisGUI gui){
        this.ref = ref;
        this.tuneles = tun;     
        this.zonaActual = Zona.ZONA_COMUN;
        this.id = idgen.nuevoIdHumano();
        this.marcado = false;
        this.haSidoAtacado = false;
        this.mapa = mapa;
        this.comidaRecolectada = 0;
        this.gui = gui;
    }
    
    @Override
    public void run(){
        try {
            ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
            while(true){
                try {
                    //Entrada a zona comun
                    setZonaActual(Zona.ZONA_COMUN); 
                    logger.log("[" + id + "] preparandose para entrar al mundo exterior...");
                    sleepRandom(1000, 2000);
                    
                    //Seleccion de tunel
                    int indiceTunel = random.nextInt(tuneles.length);
                    Tunel tunel = tuneles[indiceTunel];
                    
                    //Proceso de espera a grupo para tunel
                    logger.log("[" + id + "] esperando grupo en túnel" + (indiceTunel + 1));
                    tunel.esperarGrupoParaSalir(this); 
                    
                    //Proceso interior del tunel
                    tunel.entrarTunel(this, false);
                    sleep(1000);
                    tunel.salirTunel(this);
                    
                    //Exploracion en el exterior
                    Zona inseguraActual = tunel.getAreaInsegura();
                    setZonaActual(inseguraActual);
                    logger.log("[" + id + "] recolectando comida en " + zonaActual);
                    try {
                        sleepRandom(3000, 5000);
                    } catch (InterruptedException ie) {
                        logger.log("[" + id + "] interrumpido mientras recolectaba comida.");
                        continue;
                    }
                    
                    // Solo recolecta comida si no fue atacado por un zombi
                    if (!haSidoAtacado) {
                        comidaRecolectada++;
                        logger.log("[" + id + "] recolectó 2 piezas de comida.");
                    } else {
                        logger.log("[" + id + "] fue atacado y no pudo recolectar comida.");
                    }
                    
                    // Vuelta a la zona segura
                    tunel.entrarTunel(this, true);
                    gui.mostrarHumanoTunel(this, tunel.getZona());
                    sleep(1000);
                    gui.quitarHumanoTunel(tunel.getZona());
                    tunel.salirTunel(this);
                    
                    haSidoAtacado = false; //resetear estado de atacado una vez sale del túnel

                    //Dejar la comida recolectada
                    if(comidaRecolectada > 0){
                        ref.dejarComida(2, id);
                        comidaRecolectada--;
                        logger.log("[" + id + "] dejó 2 piezas de comida.");   
                    }
                    
                    //Entrada a la zona de descanso
                    setZonaActual(Zona.DESCANSO);
                    logger.log("[" + id + "] descansa.");
                    sleepRandom(2000, 4000);
                    
                    //Entrada al comedor
                    setZonaActual(Zona.COMEDOR);
                    logger.log("[" + id + "] intentando comer.");
                    ref.cogerComida(id); // bloqueará si no hay comida
                    sleepRandom(3000, 5000);

                    //Si fue marcado, se recupera
                    if (marcado) {
                        setZonaActual(Zona.DESCANSO);
                        logger.log("[" + id + "] recuperandose de las heridas....");
                        sleepRandom(3000, 5000);
                        marcado = false;
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    logger.log("[" + id + "] interrumpido."); // Si el humano muere se interrumpe por "interrupt()"
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
    
    public synchronized void setZonaActual(Zona nuevaZona){
        if (this.zonaActual != null && !this.zonaActual.equals(nuevaZona)) {
            mapa.quitarHumanoZona(this, this.zonaActual);
        }
        this.zonaActual = nuevaZona;
        mapa.guardarHumano(this, nuevaZona);
    }
    private void sleepRandom(int min, int max) throws InterruptedException {
        sleep(min + random.nextInt(max - min + 1));
    }
    
    public synchronized boolean serAtacado() throws IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();

        if (haSidoAtacado) {
            logger.log("[" + id + "] ya fue atacado. Ignorando ataque.");
            return false;
        }

        haSidoAtacado = true;
 
        this.interrupt();
        
        boolean seDefiende = random.nextInt(3) < 2; // 2/3 posibilidad de sobrevivir

        if (seDefiende) {
            marcado = true;
            logger.log("[" + id + "] logró defenderse.");
            return false;
        } else {
            logger.log("[" + id + "] no logró defenderse y ha muerto.");
            mapa.quitarHumanoZona(this, zonaActual);
            return true;
        }
    }
}
