package Zonas;

import Entidades.Zombi;
import Entidades.Humano;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MapaZonas {
    private final Map<Zona, List<Humano>> humanosPorZona = new ConcurrentHashMap<>();
    private final Map<Zona, List<Zombi>> zombisPorZona = new ConcurrentHashMap<>();

    public MapaZonas() {
        for (Zona zona : Zona.values()) {
            humanosPorZona.put(zona, new CopyOnWriteArrayList<>());
            zombisPorZona.put(zona, new CopyOnWriteArrayList<>());
        }
    }

    // Humanos
    public void guardarHumano(Humano h, Zona z) {
        humanosPorZona.get(z).add(h);
    }

    public void quitarHumanoZona(Humano h, Zona z) {
        humanosPorZona.get(z).remove(h);
    }

    public void moverHumanoZona(Humano h, Zona anterior, Zona nueva) {
        quitarHumanoZona(h, anterior);
        guardarHumano(h, nueva);
    }

    // Zombis
    public void guardarZombi(Zombi z, Zona zombiZona) {
        zombisPorZona.get(zombiZona).add(z);
    }

    public void quitarZombiZona(Zombi z, Zona zombiZona) {
        zombisPorZona.get(zombiZona).remove(z);
    }

    // Consultas
    public List<Humano> humanosEnZona(Zona z) {
        return humanosPorZona.get(z);
    }

    public List<Zombi> zombisEnZona(Zona z) {
        return zombisPorZona.get(z);
    }
}
