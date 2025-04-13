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
        logID(hum.getIdHumano(), "Se prepara para salir por "+zona);
    }
    
    private void logID (String id, String msg){
        System.out.println("["+id+"] "+msg);
    }

    void entrarAlTunelDesdeRefugio(Humano hum) throws InterruptedException {
        accesoTunel.acquire();
        logID(hum.getIdHumano(), "Entra al "+zona);
        hum.setZonaActual(zona);
    }
}
