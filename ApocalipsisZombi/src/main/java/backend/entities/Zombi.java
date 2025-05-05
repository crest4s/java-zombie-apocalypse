package backend.entities;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import backend.utils.ApocalipsisLogger;
import backend.utils.IdGenerator;
import backend.utils.PausaGlobal;
import backend.zones.MapaZonas;
import backend.zones.Zona;

/**
 * Representa a un zombi en el simulador apocalíptico.
 * Cada zombi es un hilo que se mueve por zonas inseguras y ataca a humanos.
 */
public class Zombi extends Thread {
    private final String id;
    private int muertes;
    private Zona zonaActual;
    private final Random random = new Random();
    private final MapaZonas mapa;
    private final IdGenerator idgen;

    /**
     * Crea un nuevo zombi a partir de un humano convertido.
     * 
     * @param idgen generador de IDs
     * @param humId ID del humano del que proviene
     * @param mapa referencia al mapa de zonas
     */
    public Zombi(IdGenerator idgen, String humId, MapaZonas mapa) {
        this.id = idgen.nuevoIdZombi(humId);
        this.idgen = idgen;
        this.mapa = mapa;
        this.muertes = 0;
    }

    /**
     * Crea un zombi base con ID fijo "Z0000".
     * 
     * @param mapa referencia al mapa de zonas
     * @param idgen generador de IDs
     */
    public Zombi(MapaZonas mapa, IdGenerator idgen) {
        this.id = "Z0000";
        this.idgen = idgen;
        this.mapa = mapa;
        this.muertes = 0;
    }

    /**
     * Lógica principal del hilo zombi. Se mueve por zonas de riesgo,
     * busca humanos, los ataca y convierte si es posible.
     */
    @Override
    public void run() {
        try {
            ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
            while (true) {
                PausaGlobal.getInstance().esperarSiPausado();
                int zona = random.nextInt(4) + 1;
                setZonaActual(Zona.valueOf("RIESGO_" + zona));
                logger.log("[" + id + "] entra a " + zonaActual);
                PausaGlobal.getInstance().esperarSiPausado();

                List<Humano> humanos = mapa.humanosEnZona(zonaActual);
                if (!humanos.isEmpty()) {
                    PausaGlobal.getInstance().esperarSiPausado();
                    Humano objetivo = humanos.get(random.nextInt(humanos.size()));
                    logger.log("[" + id + "] atacando a " + objetivo.getIdHumano());
                    sleepConPausa(sleepRandom(500, 1500));
                    PausaGlobal.getInstance().esperarSiPausado();

                    boolean muerto = objetivo.serAtacado();
                    PausaGlobal.getInstance().esperarSiPausado();
                    if (muerto) {
                        muertes++;
                        convertirEnZombi(objetivo);
                        logger.log("[" + id + "] mató a " + objetivo.getIdHumano() + ". Total muertes: " + muertes);
                    }
                } else {
                    logger.log("[" + id + "] no encontró humanos.");
                    sleepConPausa(sleepRandom(2000, 3000));
                    PausaGlobal.getInstance().esperarSiPausado();
                }
            }
        } catch (InterruptedException e) {
            try {
                ApocalipsisLogger.getInstance().log("[" + id + "] interrumpido.");
            } catch (IOException ignored) {}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera un valor aleatorio de duración en milisegundos.
     *
     * @param min tiempo mínimo
     * @param max tiempo máximo
     * @return duración aleatoria
     */
    private int sleepRandom(int min, int max) throws InterruptedException {
        return min + random.nextInt(max - min + 1);
    }
    
    /**
     * Pausa el hilo durante el tiempo indicado, respetando la pausa global.
     *
     * @param duracionTotal duración total en milisegundos
     * @throws InterruptedException si el hilo es interrumpido
     */
    public void sleepConPausa(long duracionTotal) throws InterruptedException {
        final long intervalo = 100;
        long tiempoTranscurrido = 0;

        while (tiempoTranscurrido < duracionTotal) {
            if (PausaGlobal.getInstance().estaPausado()) {
                PausaGlobal.getInstance().esperarSiPausado();
                continue;
            }

            long esperar = Math.min(intervalo, duracionTotal - tiempoTranscurrido);
            Thread.sleep(esperar);
            tiempoTranscurrido += esperar;
        }
    }



    /**
     * Cambia la zona actual del zombi y actualiza su posición en el mapa.
     *
     * @param zona nueva zona a la que se mueve el zombi
     */
    public void setZonaActual(Zona zona) {
        if (this.zonaActual != null) {
            mapa.quitarZombiZona(this, this.zonaActual);
        }
        this.zonaActual = zona;
        mapa.guardarZombi(this, zona);
    }

    /**
     * Devuelve la zona actual del zombi.
     *
     * @return zona en la que se encuentra el zombi
     */
    public Zona getZonaActual() {
        return zonaActual;
    }
    
    /**
     * Devuelve el número total de humanos eliminados por este zombi.
     *
     * @return cantidad de muertes
     */
    public int getMuertes(){
        return muertes;
    }

    /**
     * Devuelve el identificador único del zombi.
     *
     * @return ID del zombi
     */
    public String getIdZombi() {
        return id;
    }

    /**
     * Convierte un humano muerto en un nuevo zombi.
     *
     * @param h humano a convertir
     * @return nueva instancia de zombi creada a partir del humano
     * @throws IOException si falla al escribir en el log
     * @throws InterruptedException si el hilo del humano no puede unirse
     */
    private Zombi convertirEnZombi(Humano h) throws IOException, InterruptedException {
        if (h.getIdHumano().startsWith("Z")) return null;
        Zombi nuevo = new Zombi(idgen, h.getIdHumano(), mapa);
        Zona zona = h.getZonaActual();
        
        h.interrupt();
        mapa.quitarHumanoZona(h, zona);
        h.join();
        
        nuevo.setZonaActual(zona);
        nuevo.start();

        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        logger.log("[" + id + "] " + h.getIdHumano() + " ha sido convertido en zombi " + nuevo.getIdZombi());

        return nuevo;
    }
}
