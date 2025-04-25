package Zonas;

import Entidades.Humano;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;
import Helpers.ApocalipsisLogger;
import UI.ApocalipsisGUI;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CyclicBarrier;

public class Tunel {
    
    private int id;
    private Zona zona;
    private ApocalipsisGUI gui;
    
    private CyclicBarrier barrier = new CyclicBarrier(3);
    private Semaphore accesoTunel = new Semaphore(1);
    private final int TAM_GRUPO = 3;
   
    private final Object monitor = new Object();
    private boolean grupoActivo = false;
    
    private final Queue<Humano> colaRefugio = new LinkedList<>();
    private final Queue<Humano> colaRiesgo = new LinkedList<>();
    
    private int esperandoExterior = 0;
        
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
    
    public void esperarGrupoParaSalir(Humano h) throws IOException, InterruptedException, BrokenBarrierException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        synchronized (monitor) {
            colaRefugio.add(h);
            gui.mostrarHumanoTunel(h, zona);
            logger.log("[" + h.getIdHumano() + "] esperando en la cola de refugio en " + zona);
        }

        barrier.await(); // espera hasta formar grupo de 3

        synchronized (monitor) {
            if (!grupoActivo) {
                grupoActivo = true; // marca que este grupo está cruzando
            }
        }
    }
    
    //Gestionar zonas a las que entra
    public void entrarTunel(Humano h, boolean desdeExterior) throws InterruptedException, IOException, BrokenBarrierException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();

        if (desdeExterior) {
            synchronized (monitor) {
                colaRiesgo.add(h);
                gui.mostrarHumanoTunel(h, zona);
                logger.log("[" + h.getIdHumano() + "] espera desde el exterior en " + zona);
            }

            accesoTunel.acquire(); // única entrada activa
            synchronized (monitor) {
                colaRiesgo.remove(h);
                h.setZonaActual(zona);
                gui.mostrarHumanoTunel(h, zona);
                logger.log("[" + h.getIdHumano() + "] entra al túnel desde el exterior.");
            }

        } else {
            synchronized (monitor) {
                // Si hay gente esperando del exterior, espera hasta que se vacíe la cola
                while (!colaRiesgo.isEmpty() || !accesoTunel.tryAcquire()) {
                    monitor.wait();
                }

                colaRefugio.remove(h);
                h.setZonaActual(zona);
                gui.mostrarHumanoTunel(h, zona);
                logger.log("[" + h.getIdHumano() + "] entra al túnel desde el refugio.");
            }
        }
    }

    public void salirTunel(Humano h) throws IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        accesoTunel.release();

        synchronized (monitor) {
            gui.quitarHumanoTunel(zona);
            logger.log("[" + h.getIdHumano() + "] sale del túnel.");
            if (barrier.getNumberWaiting() == 0) {
                grupoActivo = false;
                barrier.reset(); // reset para siguiente grupo
            }
            monitor.notifyAll(); // despierta a otros en espera
        }
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
