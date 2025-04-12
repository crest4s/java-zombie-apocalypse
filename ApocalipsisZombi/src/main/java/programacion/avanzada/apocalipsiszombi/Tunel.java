/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author crestas
 */
public class Tunel {
    
    private int id;
    
    private Zona zona;
    
    private CyclicBarrier esperaExpedicion;

    
    public Tunel(int id){
        this.id=id;
        
        esperaExpedicion = new CyclicBarrier(3);
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
}
