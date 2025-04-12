/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 *
 * @author crestas
 */
public class Refugio {
    //Comedor (usar monitor para dar la comida de forma ordenada si no hay)
    private int cantidadComida;
    private Semaphore semaforoComida;
    private ArrayList<String> ocupacionComedor;
    
    //Zona de descanso
    private ArrayList<String> ocupacionDescanso;
    
    //Zona común
    private ArrayList<String> ocupacionComun;
    private CyclicBarrier esperaExpedicionT1;
    private CyclicBarrier esperaExpedicionT2;
    private CyclicBarrier esperaExpedicionT3;
    private CyclicBarrier esperaExpedicionT4;
    
    //Túneles
    private Tunel[] tuneles;
    
    public Refugio(Tunel[] tuneles){
        cantidadComida = 0;
        semaforoComida = new Semaphore(1);
        ocupacionComedor = new ArrayList<>();
        
        ocupacionDescanso = new ArrayList<>();
        
        ocupacionComun = new ArrayList<>();
        esperaExpedicionT1 = new CyclicBarrier(3); //Esperar a 3 humanos para acceder a Tunel 1
        esperaExpedicionT2 = new CyclicBarrier(3); //Esperar a 3 humanos para acceder a Tunel 2
        esperaExpedicionT3 = new CyclicBarrier(3); //Esperar a 3 humanos para acceder a Tunel 3
        esperaExpedicionT4 = new CyclicBarrier(3); //Esperar a 3 humanos para acceder a Tunel 4
        
        this.tuneles = tuneles;
    }
    
    public void dejarComida(int comida) throws InterruptedException{
        semaforoComida.acquire();
        cantidadComida += comida;
        System.out.println("Se han dejado "+comida+" alimentos");
        semaforoComida.release();
    }
    
    public synchronized void cogerComida() throws InterruptedException{
        while (cantidadComida<=0){
            wait();
        }
        cantidadComida--;
        notify();
    }
    
    public void esperarTunel(int numTunel) throws InterruptedException, BrokenBarrierException{
        switch (numTunel){
            case 1 ->{
                esperaExpedicionT1.await();
            }
            case 2 -> {
                esperaExpedicionT2.await();
            }
            case 3 -> {
                esperaExpedicionT3.await();
            }
            case 4 -> {
                esperaExpedicionT4.await();
            }
        }
    }
    
    public void llegarComedor(String idHumano){
        ocupacionComedor.add(idHumano);
        System.out.println("Humanos dentro del comedor: "+ocupacionComedor);
    }
    public void llegarZonaComun(String idHumano){
        ocupacionComun.add(idHumano);
        System.out.println("Humanos dentro de la zona comun: "+ocupacionComun);
    }
    public void llegarDescanso(String idHumano){
        ocupacionDescanso.add(idHumano);
        System.out.println("Humanos dentro de la zona de descanso: "+ocupacionDescanso);
    }
    public void salirComedor (String idHumano){
        ocupacionComedor.remove(idHumano);
        System.out.println("Humanos dentro del comedor: "+ocupacionComedor);
    }
    public void salirZonaComun(String idHumano){
        ocupacionComun.remove(idHumano);
        System.out.println("Humanos dentro de la zona comun: "+ocupacionComun);
    }
    public void salirDescanso(String idHumano){
        ocupacionDescanso.remove(idHumano);
        System.out.println("Humanos dentro de la zona de descanso: "+ocupacionDescanso);
    }
}
