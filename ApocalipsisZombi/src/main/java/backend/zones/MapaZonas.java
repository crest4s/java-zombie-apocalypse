package backend.zones;

import backend.entities.Zombi;
import backend.entities.Humano;
import frontend.server.ActualizadorServerGUI;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase que gestiona la distribución de humanos y zombis en las diferentes zonas del mapa.
 * Proporciona operaciones seguras para acceder y modificar las zonas en un entorno multihilo.
 */
public class MapaZonas {
    private final Map<Zona, List<Humano>> humanosPorZona = new ConcurrentHashMap<>();
    private final Map<Zona, List<Zombi>> zombisPorZona = new ConcurrentHashMap<>();
    private ActualizadorServerGUI act;

    /**
     * Constructor que inicializa todas las zonas con listas vacías de humanos y zombis.
     */
    public MapaZonas() {
        for (Zona zona : Zona.values()) {
            humanosPorZona.put(zona, new CopyOnWriteArrayList<>());
            zombisPorZona.put(zona, new CopyOnWriteArrayList<>());
        }
    }

    /**
     * Establece el componente encargado de actualizar la interfaz gráfica al modificar zonas.
     *
     * @param act instancia de {@link ActualizadorServerGUI}
     */
    public void setActualizador(ActualizadorServerGUI act) {
        this.act = act;
    }

    // --- Gestión de humanos ---

    /**
     * Añade un humano a una zona específica.
     *
     * @param h humano a añadir
     * @param z zona donde se almacenará
     */
    public void guardarHumano(Humano h, Zona z) {
        humanosPorZona.get(z).add(h);
        act.actualizarZona(z);
    }

    /**
     * Elimina un humano de una zona específica.
     *
     * @param h humano a eliminar
     * @param z zona desde la cual se eliminará
     */
    public void quitarHumanoZona(Humano h, Zona z) {
        humanosPorZona.get(z).remove(h);
        act.actualizarZona(z);
    }

    // --- Gestión de zombis ---

    /**
     * Añade un zombi a una zona específica.
     *
     * @param zombi zombi a añadir
     * @param zombiZona zona donde se almacenará
     */
    public void guardarZombi(Zombi zombi, Zona zombiZona) {
        zombisPorZona.get(zombiZona).add(zombi);
        act.actualizarZona(zombiZona);
    }

    /**
     * Elimina un zombi de una zona específica.
     *
     * @param zombi zombi a eliminar
     * @param zombiZona zona desde la cual se eliminará
     */
    public void quitarZombiZona(Zombi zombi, Zona zombiZona) {
        zombisPorZona.get(zombiZona).remove(zombi);
        act.actualizarZona(zombiZona);
    }

    // --- Consultas ---

    /**
     * Obtiene la lista de humanos en una zona determinada.
     *
     * @param z zona a consultar
     * @return lista de humanos presentes en esa zona
     */
    public List<Humano> humanosEnZona(Zona z) {
        return humanosPorZona.get(z);
    }

    /**
     * Obtiene la lista de zombis en una zona determinada.
     *
     * @param z zona a consultar
     * @return lista de zombis presentes en esa zona
     */
    public List<Zombi> zombisEnZona(Zona z) {
        return zombisPorZona.get(z);
    }

    /**
     * Obtiene una lista combinada de todos los zombis en las zonas de riesgo.
     *
     * @return lista de todos los zombis activos
     */
    public List<Zombi> getAllZombi() {
        List<Zombi> todosZombi = new CopyOnWriteArrayList<>();

        todosZombi.addAll(zombisEnZona(Zona.RIESGO_1));
        todosZombi.addAll(zombisEnZona(Zona.RIESGO_2));
        todosZombi.addAll(zombisEnZona(Zona.RIESGO_3));
        todosZombi.addAll(zombisEnZona(Zona.RIESGO_4));

        return todosZombi;
    }

    /**
     * Mueve un humano de una zona a otra, actualizando ambas listas y notificando a la interfaz.
     *
     * @param h humano a mover
     * @param zonaOrigen zona actual del humano
     * @param zonaDestino nueva zona a la que se moverá
     */
    public synchronized void moverHumano(Humano h, Zona zonaOrigen, Zona zonaDestino) {
        humanosPorZona.get(zonaOrigen).remove(h);
        humanosPorZona.get(zonaDestino).add(h);
        act.actualizarZona(zonaOrigen);
        act.actualizarZona(zonaDestino);
    }
}
