package Zonas;

import Entidades.Humano;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;
import Helpers.ApocalipsisLogger;
import UI.ApocalipsisGUI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tunel {
    
    private int id;
    private Zona zona;
    
    //private CyclicBarrier esperaExpedicion = new CyclicBarrier(3);
    private Semaphore accesoTunel = new Semaphore(1);
    private ApocalipsisGUI gui;
    
    private final Object monitor = new Object();
    private int esperandoExterior = 0;
    private int esperandoRefugio = 0;
    
    private final List<Humano> grupoExpedicion = new ArrayList<>();
    private final int TAM_GRUPO = 3;
    
    private final List<Humano> ladoRefugio = new CopyOnWriteArrayList<>();
    private final List<Humano> ladoRiesgo = new CopyOnWriteArrayList<>();
    
    public Tunel(int id, ApocalipsisGUI gui){
        this.id = id;
        this.gui = gui;
        
        switch (id) {
            case 1 -> this.zona = Zona.TUNEL_1;
            case 2 -> this.zona = Zona.TUNEL_2;
            case 3 -> this.zona = Zona.TUNEL_3;
            case 4 -> this.zona = Zona.TUNEL_4;
            default -> throw new IllegalArgumentException("ID de túnel no válido");
        }
    }
    
    public Zona getZona(){
        return zona;
    }
    
    public void setZona(Zona zona){
        this.zona=zona;
    }
    public List<Humano> getLadoRefugio() {
        return ladoRefugio;
    }

    public List<Humano> getLadoRiesgo() {
        return ladoRiesgo;
    }

    public void esperarGrupoParaSalir(Humano hum) throws InterruptedException, BrokenBarrierException, IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        logger.log("[" + hum.getIdHumano() + "] se prepara para salir por "+zona);
        synchronized (monitor) {
            grupoExpedicion.add(hum);
            if (grupoExpedicion.size() < TAM_GRUPO) {
                monitor.wait();
            } else {
                monitor.notifyAll(); // Despierta al grupo
                grupoExpedicion.clear(); // reinicia para siguiente grupo
            }
        }
    }
   
    //Gestionar zonas a las que entra
    public void entrarTunel(Humano hum, boolean desdeExterior) throws InterruptedException, IOException, BrokenBarrierException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();

        hum.setZonaActual(zona);                    // Aparece en la zona de túnel
        gui.mostrarHumanoTunel(hum, zona);          // Lo mostramos en el campo de túnel

        if (desdeExterior) {
            synchronized (monitor) {
                esperandoExterior++;
            }

            accesoTunel.acquire();

            synchronized (monitor) {
                esperandoExterior--;
                gui.quitarHumanoTunel(zona);        // Sale del TextField del túnel
            }

        } else {
            synchronized (monitor) {
                esperandoRefugio++;
                grupoExpedicion.add(hum);

                while (grupoExpedicion.size() < TAM_GRUPO && esperandoExterior == 0) {
                    monitor.wait();  // Esperamos a completar el grupo o a que haya exteriores
                }

                if (esperandoExterior > 0 && grupoExpedicion.get(0) != hum) {
                    // Si no soy el primero del grupo y hay exteriores esperando, me espero
                    while (grupoExpedicion.get(0) != hum) {
                        monitor.wait();
                    }
                }

                accesoTunel.acquire(); // Túnel libre para entrar
                grupoExpedicion.remove(hum);
                esperandoRefugio--;
                monitor.notifyAll();   // Por si hay otros esperando
            }

            gui.quitarHumanoTunel(zona); // Sale del TextField del túnel
        }
        logger.log("[" + hum.getIdHumano() + "] entra a " + zona + " desde " + hum.getZonaActual());
    }
    
    public void salirTunel(Humano hum) throws IOException{
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance(); 
        accesoTunel.release();  // Libera el túnel

        synchronized (monitor) {
            monitor.notifyAll(); // Notifica para que los siguientes puedan entrar
    }     
        logger.log("[" + hum.getIdHumano() + "] sale del tunel");
    }
    
    public Zona getAreaInsegura(){
        Zona zonaInsegura;
        switch (id) {
            case 1 -> zonaInsegura = Zona.RIESGO_1;
            case 2 -> zonaInsegura = Zona.RIESGO_2;
            case 3 -> zonaInsegura = Zona.RIESGO_3;
            case 4 -> zonaInsegura = Zona.RIESGO_4;
            default -> throw new IllegalArgumentException("ID de túnel no válido");
        }
        return zonaInsegura;
    }
}
