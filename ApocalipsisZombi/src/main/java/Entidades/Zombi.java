package Entidades;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import Helpers.ApocalipsisLogger;
import Helpers.IdGenerator;
import Zonas.MapaZonas;
import Zonas.Zona;

public class Zombi extends Thread {
    private final String id;
    private int muertes;
    private Zona zonaActual;
    private final Random random = new Random();
    private final MapaZonas mapa;

    public Zombi(IdGenerator idgen, String humId, MapaZonas mapa) {
        this.id = idgen.nuevoIdZombi(humId);
        this.mapa = mapa;
        this.muertes = 0;
    }

    public Zombi(MapaZonas mapa) {
        this.id = "Z00000";
        this.mapa = mapa;
        this.muertes = 0;
    }

    @Override
    public void run() {
        try {
            ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
            while (true) {
                int zona = random.nextInt(4) + 1;
                setZonaActual(Zona.valueOf("RIESGO_" + zona));
                logger.log("[" + id + "] entra a " + zonaActual);

                List<Humano> humanos = mapa.humanosEnZona(zonaActual);
                if (!humanos.isEmpty()) {
                    Humano objetivo = humanos.get(random.nextInt(humanos.size()));
                    logger.log("[" + id + "] atacando a " + objetivo.getIdHumano());
                    sleepRandom(500, 1500);

                    boolean muerto = objetivo.serAtacado();
                    if (muerto) {
                        muertes++;
                        convertirEnZombi(objetivo);
                        logger.log("[" + id + "] mató a " + objetivo.getIdHumano() + ". Total muertes: " + muertes);
                    }
                } else {
                    logger.log("[" + id + "] no encontró humanos.");
                    sleepRandom(2000, 3000);
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

    public String getIdZombi() {
        return id;
    }

    private Zombi convertirEnZombi(Humano h) throws IOException {
        if (h.getIdHumano().startsWith("Z")) return null;
        Zombi nuevo = new Zombi(new IdGenerator(), h.getIdHumano(), mapa);
        Zona zona = h.getZonaActual();

        mapa.quitarHumanoZona(h, zona);
        mapa.guardarZombi(nuevo, zona);

        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        logger.log("[" + id + "] " + h.getIdHumano() + " ha sido convertido en zombi " + nuevo.getIdZombi());

        h.interrupt();
        nuevo.start();
        return nuevo;
    }
}
