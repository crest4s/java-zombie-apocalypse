package Entidades;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import Helpers.ApocalipsisLogger;
import Helpers.IdGenerator;
import Zonas.MapaZonas;
import Zonas.Refugio;
import Zonas.Tunel;
import Zonas.Zona;

public class Humano extends Thread {
    private final Refugio ref;
    private final Tunel[] tuneles;
    private Zona zonaActual;
    private final String id;
    private boolean marcado;
    private boolean haSidoAtacado;
    private final Random random = new Random();
    private final MapaZonas mapa;
    private int comidaRecolectada;

    public Humano(IdGenerator idgen, Refugio ref, Tunel[] tuneles, MapaZonas mapa) {
        this.ref = ref;
        this.tuneles = tuneles;
        this.zonaActual = Zona.ZONA_COMUN;
        this.id = idgen.nuevoIdHumano();
        this.marcado = false;
        this.haSidoAtacado = false;
        this.mapa = mapa;
        this.comidaRecolectada = 0;
    }

    @Override
    public void run() {
        try {
            ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
            while (!isInterrupted()) {
                setZonaActual(Zona.ZONA_COMUN);
                logger.log("[" + id + "] preparándose para salir...");
                sleepRandom(1000, 2000);

                int indiceTunel = random.nextInt(tuneles.length);
                Tunel tunel = tuneles[indiceTunel];

                logger.log("[" + id + "] esperando en túnel " + tuneles[indiceTunel].getZona().name());
                tunel.esperarGrupoParaSalir(this);

                tunel.entrarTunel(this, false);
                sleep(1000);
                tunel.salirTunel(this);

                setZonaActual(tunel.getAreaInsegura());
                logger.log("[" + id + "] explorando en " + zonaActual);
                sleepRandom(3000, 5000);

                if (!haSidoAtacado) {
                    comidaRecolectada += 2;
                    logger.log("[" + id + "] recolectó 2 unidades de comida.");
                }

                tunel.entrarTunel(this, true);
                sleep(1000);
                tunel.salirTunel(this);

                if (comidaRecolectada > 0) {
                    ref.dejarComida(2, id);
                    comidaRecolectada = 0;
                }

                haSidoAtacado = false;

                setZonaActual(Zona.DESCANSO);
                logger.log("[" + id + "] descansando...");
                sleepRandom(2000, 4000);

                setZonaActual(Zona.COMEDOR);
                logger.log("[" + id + "] intentando comer...");
                ref.cogerComida(id);
                sleepRandom(3000, 5000);

                if (marcado) {
                    setZonaActual(Zona.DESCANSO);
                    logger.log("[" + id + "] curándose heridas...");
                    sleepRandom(3000, 5000);
                    marcado = false;
                }
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            try {
                ApocalipsisLogger.getInstance().log("[" + id + "] interrumpido o muerto.");
            } catch (IOException ignored) {}
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setZonaActual(Zona nuevaZona) {
        if (this.zonaActual != null && !this.zonaActual.equals(nuevaZona)) {
            mapa.quitarHumanoZona(this, this.zonaActual);
        }
        this.zonaActual = nuevaZona;
        mapa.guardarHumano(this, nuevaZona);
    }

    public Zona getZonaActual() {
        return zonaActual;
    }

    public String getIdHumano() {
        return id;
    }

    public synchronized boolean serAtacado() throws IOException {
        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        if (this.isInterrupted()) return false;
        
        if (haSidoAtacado) {
            return false;
        }

        haSidoAtacado = true;
        boolean seDefiende = random.nextInt(3) < 2; // 2/3 chance de defenderse
        if (seDefiende) {
            marcado = true;
            logger.log("[" + id + "] ha sido herido pero sobrevivió al ataque.");
            return false;
        } else {
            mapa.quitarHumanoZona(this, zonaActual);
            logger.log("[" + id + "] fue eliminado por un zombi.");
            interrupt();
            return true;
        }
    }

    private void sleepRandom(int min, int max) throws InterruptedException {
        sleep(min + random.nextInt(max - min + 1));
    }
}
