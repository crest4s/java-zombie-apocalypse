package Zonas;

import Entidades.Humano;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import Helpers.ApocalipsisLogger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tunel {
    
    private int id;
    private Zona zona;
    
    private CyclicBarrier esperaExpedicion = new CyclicBarrier(3);
    private Semaphore accesoTunel = new Semaphore(1);
    
    private final Object monitor = new Object();
    private int esperandoExterior = 0;
    private int esperandoRefugio = 0;
    
    private final List<Humano> ladoRefugio = new CopyOnWriteArrayList<>();
    private final List<Humano> ladoRiesgo = new CopyOnWriteArrayList<>();

    
    public Tunel(int id){
        this.id=id;
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
        esperaExpedicion.await();
    }
   
    //gestionar zonas a las que entra
    public void entrarTunel(Humano hum, boolean desdeExterior) throws InterruptedException, IOException, BrokenBarrierException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        
        synchronized (monitor) {
            if (desdeExterior) {
                esperandoExterior++;
                while (!accesoTunel.tryAcquire()) {
                    monitor.wait(); // espera a que el túnel esté libre
                }
                esperandoExterior--;
                ladoRiesgo.add(hum);
            } else {
                esperandoRefugio++;
                esperarGrupoParaSalir(hum); // espera al grupo
                
                while (esperandoExterior > 0 || !accesoTunel.tryAcquire()) {
                    monitor.wait(); // espera si hay alguien del exterior o el túnel ocupado
                }
                esperandoRefugio--;
                ladoRefugio.add(hum);
            }
        }
        
        logger.log("[" + hum.getIdHumano() + "] entra a " + zona + " desde " + hum.getZonaActual());
        hum.setZonaActual(zona);
    }
    
    public void salirTunel(Humano hum) throws IOException{
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        ladoRiesgo.remove(hum);
        ladoRefugio.remove(hum); 
        accesoTunel.release(); //ya ha salido del tunel, se queda libre
        synchronized (monitor) {
            monitor.notifyAll(); //despierta a todos para ver quien puede entrar
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
