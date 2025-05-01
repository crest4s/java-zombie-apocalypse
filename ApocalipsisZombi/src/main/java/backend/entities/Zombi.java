package backend.entities;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import backend.utils.ApocalipsisLogger;
import backend.utils.IdGenerator;
import backend.utils.PausaGlobal;
import backend.zones.MapaZonas;
import backend.zones.Zona;

public class Zombi extends Thread {
    private final String id;
    private int muertes;
    private Zona zonaActual;
    private final Random random = new Random();
    private final MapaZonas mapa;
    private final IdGenerator idgen;

    public Zombi(IdGenerator idgen, String humId, MapaZonas mapa) {
        this.id = idgen.nuevoIdZombi(humId);
        this.idgen = idgen;
        this.mapa = mapa;
        this.muertes = 0;
    }

    public Zombi(MapaZonas mapa, IdGenerator idgen) {
        this.id = "Z0000";
        this.idgen = idgen;
        this.mapa = mapa;
        this.muertes = 0;
    }

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
                    sleepRandom(500, 1500);
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
                    sleepRandom(2000, 3000);
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

    private void sleepRandom(int min, int max) throws InterruptedException {
        sleep(min + random.nextInt(max - min + 1));
    }

    public void setZonaActual(Zona zona) {
        if (this.zonaActual != null) {
            mapa.quitarZombiZona(this, this.zonaActual);
        }
        this.zonaActual = zona;
        mapa.guardarZombi(this, zona);
    }

    public Zona getZonaActual() {
        return zonaActual;
    }
    
    public int getMuertes(){
        return muertes;
    }

    public String getIdZombi() {
        return id;
    }

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
