package Zonas;

import Entidades.Zombi;
import Entidades.Humano;
import UI.ZonaChangeListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class MapaZonas {
    private Map<Zona, List<Humano>> humanosPorZona = new ConcurrentHashMap<>();
    private Map<Zona, List<Zombi>> zombisPorZona = new ConcurrentHashMap<>();
    private ZonaChangeListener listener;
    
    public MapaZonas(){
        for(Zona zona : Zona.values()){
            humanosPorZona.put(zona, new CopyOnWriteArrayList<>());
            zombisPorZona.put(zona, new CopyOnWriteArrayList<>());        
        }
    }
    
    //Listener para notificar a la GUI
    public void setZonaChangeListener(ZonaChangeListener listener) {
        this.listener = listener;
    }
    
    //Humanos
    public void guardarHumano(Humano h, Zona z){
        humanosPorZona.get(z).add(h);
        if (listener != null){
            listener.onCambioZona(h.getIdHumano(), null, z, false); //La zona anterior es nullç
        }
    }

    public void quitarHumanoZona(Humano h, Zona z){
        humanosPorZona.get(z).remove(h);
        if (listener != null){
            listener.onCambioZona(h.getIdHumano(), z, null, false); //La zona nueva es null
        }
    }     
    
    public void moverHumanoZona(Humano h, Zona anterior, Zona nueva){
        quitarHumanoZona(h, anterior);
        guardarHumano(h, nueva);
        if (listener != null){
            listener.onCambioZona(h.getIdHumano(), anterior, nueva, false);
        }
    }
    
    //Zombies
    public void guardarZombi(Zombi z, Zona zona){
        zombisPorZona.get(zona).add(z);
    }
    
    public void quitarZombiZona(Zombi z, Zona zona){
        zombisPorZona.get(zona).remove(z);
    }   
    
    //Presencia en zonas
    public List<Humano> humanosEnZona(Zona z){
        return humanosPorZona.get(z);
    }
    
    public List<Zombi> zombisEnZona(Zona z){
        return zombisPorZona.get(z);
    }           
}
