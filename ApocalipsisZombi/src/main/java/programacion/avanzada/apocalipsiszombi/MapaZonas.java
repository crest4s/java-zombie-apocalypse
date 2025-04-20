/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 *
 * @author adria
 */
public class MapaZonas {
    private Map<Zona, List<Humano>> humanosPorZona = new ConcurrentHashMap<>();
    private Map<Zona, List<Zombi>> zombisPorZona = new ConcurrentHashMap<>();
    
    public MapaZonas(){
        for(Zona zona : Zona.values()){
            humanosPorZona.put(zona, new CopyOnWriteArrayList<>());
            zombisPorZona.put(zona, new CopyOnWriteArrayList<>());        
        }
    }
    
    public void guardarHumano(Humano h, Zona z){
        humanosPorZona.get(z).add(h);
    }
    
    public void quitarHumanoZona(Humano h, Zona z){
        humanosPorZona.get(z).remove(h);
    }         
    
    public void guardarZombi(Zombi z, Zona zona){
        zombisPorZona.get(zona).add(z);
    }
    
    public void quitarZombiZona(Zombi z, Zona zona){
        zombisPorZona.get(zona).remove(z);
    }   
    
    public List<Humano> humanosEnZona(Zona z){
        return humanosPorZona.get(z);
    }
    
    public List<Zombi> zombisEnZona(Zona z){
        return zombisPorZona.get(z);
    }           
}
