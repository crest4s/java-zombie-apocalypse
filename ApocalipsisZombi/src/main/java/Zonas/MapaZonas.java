package Zonas;

import Entidades.Zombi;
import Entidades.Humano;
import UI.ActualizadorGUI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MapaZonas {
    private final Map<Zona, List<Humano>> humanosPorZona = new ConcurrentHashMap<>();
    private final Map<Zona, List<Zombi>> zombisPorZona = new ConcurrentHashMap<>();
    private ActualizadorGUI act;

    public MapaZonas() {
        for (Zona zona : Zona.values()) {
            humanosPorZona.put(zona, new CopyOnWriteArrayList<>());
            zombisPorZona.put(zona, new CopyOnWriteArrayList<>());
        }
    }
    
    public void setActualizador(ActualizadorGUI act){
        this.act = act;
    }
    
    // Humanos
    public void guardarHumano(Humano h, Zona z) {
        humanosPorZona.get(z).add(h);
        act.actualizarZona(z);
    }

    public void quitarHumanoZona(Humano h, Zona z) {
        humanosPorZona.get(z).remove(h);
        act.actualizarZona(z);
    }

    // Zombis
    public void guardarZombi(Zombi z, Zona zombiZona) {
        zombisPorZona.get(zombiZona).add(z);
        act.actualizarZona(zombiZona);
    }

    public void quitarZombiZona(Zombi z, Zona zombiZona) {
        zombisPorZona.get(zombiZona).remove(z);
        act.actualizarZona(zombiZona);
    }

    // Consultas
    public List<Humano> humanosEnZona(Zona z) {
        return humanosPorZona.get(z);
    }

    public List<Zombi> zombisEnZona(Zona z) {
        return zombisPorZona.get(z);
    }
    
    public synchronized void moverHumano(Humano h, Zona zonaOrigen, Zona zonaDestino) {
        humanosPorZona.get(zonaOrigen).remove(h);
        humanosPorZona.get(zonaDestino).add(h);
        act.actualizarZona(zonaOrigen);
        act.actualizarZona(zonaDestino);
    }
}
