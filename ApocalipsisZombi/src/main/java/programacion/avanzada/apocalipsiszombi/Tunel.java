/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 *
 * @author crestas
 */
public class Tunel {
    
    private int id;
    private Zona zona;
    
    private CyclicBarrier esperaExpedicion = new CyclicBarrier(3);
    private Semaphore accesoTunel = new Semaphore(1);
    
    private final Object monitor = new Object();
    private int esperandoExterior = 0;
    private int esperandoRefugio = 0;

    
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
    
    //= 0
    public void esperarGrupoParaSalir(Humano hum) throws InterruptedException, BrokenBarrierException, IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        logger.log("[" + hum.getIdHumano() + "]" + "Se prepara para salir por "+zona);
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
            } else {
                esperandoRefugio++;
                esperarGrupoParaSalir(hum); // espera al grupo
                
                while (esperandoExterior > 0 || !accesoTunel.tryAcquire()) {
                    monitor.wait(); // espera si hay alguien del exterior o el túnel ocupado
                }
                esperandoRefugio--;
            }
        }
        
        logger.log("[" + hum.getIdHumano() + "] entra a " + zona + " desde " + hum.getZonaActual());
        hum.setZonaActual(zona);
    }
    
    public void salirTunel(Humano hum) throws IOException{
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        accesoTunel.release(); //ya ha salido del tunel, se queda libre
        synchronized (monitor) {
            monitor.notifyAll(); //despierta a todos para ver quien puede entrar
        }
        logger.log("[" + hum.getIdHumano() + "]" +  "Sale del tunel");
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
