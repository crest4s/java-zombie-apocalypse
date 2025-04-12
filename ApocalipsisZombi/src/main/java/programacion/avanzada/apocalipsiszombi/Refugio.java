/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.util.ArrayList;
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
    
    public Refugio(){
        cantidadComida = 0;
        semaforoComida = new Semaphore(1);
        ocupacionComedor = new ArrayList<>();
        
        ocupacionDescanso = new ArrayList<>();
        
        ocupacionComun = new ArrayList<>();
    }
    
    public void dejarComida(int comida, String id) throws InterruptedException{
        semaforoComida.acquire();
        cantidadComida += comida;
        logID(id,"Ha dejado comida en el refugio");
        semaforoComida.release();
    }
    
    public synchronized void cogerComida(String id) throws InterruptedException{
        while (cantidadComida<=0){
            wait();
        }
        cantidadComida--;
        logID(id, "Ha cogido un alimento del refugio");
        notify();
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
    
    private void logID(String id, String msg){
        System.out.println("["+id+"] "+msg);
    }
}
