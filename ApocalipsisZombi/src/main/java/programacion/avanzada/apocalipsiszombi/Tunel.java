/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

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
    
    private CyclicBarrier esperaExpedicion;
    
    private Semaphore accesoTunel;

    
    public Tunel(int id){
        this.id=id;
        
        esperaExpedicion = new CyclicBarrier(3);
        
        accesoTunel = new Semaphore(1);
        
        switch(id) {
            case 1:
                this.zona = Zona.TUNEL_1;
                break;
            case 2:
                this.zona = Zona.TUNEL_2;
                break;
            case 3:
                this.zona = Zona.TUNEL_3;
                break;
            case 4:
                this.zona = Zona.TUNEL_4;
                break;
            default:
                throw new IllegalArgumentException("ID de túnel no válido");
        }
        
    }
    
    public void esperarTunel(int numTunel) throws InterruptedException, BrokenBarrierException{
        esperaExpedicion.await();
    }

    public Zona getZona(){
        return zona;
    }
    
    public void setZona(Zona zona){
        this.zona=zona;
    }

    public void esperarGrupoParaSalir(Humano hum) throws InterruptedException, BrokenBarrierException {
        esperaExpedicion.await();
        log(hum.getIdHumano(), "Se prepara para salir por "+zona);
    }
    
    public void log (String id, String msg){
        System.out.println("["+id+"] "+msg);
    }

    //gestionar zonas a las que entra
    public void entrarAlTunelDesdeRefugio(Humano hum) throws InterruptedException {
        accesoTunel.acquire(); //entra al tunel, lo ocupa
        log(hum.getIdHumano(), "entra a "+zona);
        hum.setZonaActual(zona);
        // Aquí no dormimos, porque el cruce real lo maneja Humano con sleep(1000)
        accesoTunel.release(); //ya ha salido del tunel, se queda libre
    }
    
    //gestionar zonas a las que entra
    public void entrarAlTunelDesdeExterior(Humano hum) throws InterruptedException {
        accesoTunel.acquire();
        log(hum.getIdHumano(), "Regresa al refugio por " + zona);
        hum.setZonaActual(zona);
        // Aquí no dormimos, porque el cruce real lo maneja Humano con sleep(1000)
        accesoTunel.release();
    }

    
    public Zona getAreaInsegura(){
        Zona zonaInsegura;
        switch (id) {
            case 1:
                zonaInsegura = Zona.RIESGO_1;
                break;
            case 2:
                zonaInsegura = Zona.RIESGO_2;
                break;
            case 3:
                zonaInsegura = Zona.RIESGO_3;
                break;
            case 4:
                zonaInsegura = Zona.RIESGO_4;
                break;
            default:
                throw new IllegalArgumentException("ID de túnel no válido");
        }
        return zonaInsegura;
    }

}
