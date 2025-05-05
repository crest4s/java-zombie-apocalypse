package backend.entities;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import backend.utils.ApocalipsisLogger;
import backend.utils.IdGenerator;
import backend.utils.PausaGlobal;
import backend.zones.MapaZonas;
import backend.zones.Refugio;
import backend.zones.Tunel;
import backend.zones.Zona;

/**
 * Representa a un humano dentro del mundo post-apocalíptico.
 * Cada humano es un hilo que sigue un ciclo de exploración, recolección y descanso.
 */
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
    
    /**
     * Crea una nueva instancia de humano
     * 
     * @param idgen
     * @param ref
     * @param tuneles
     * @param mapa 
     */
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

    /**
     * Lógica principal del hilo. Simula el ciclo de vida de un humano:
     * salir del refugio, explorar, recolectar comida, regresar, comer y descansar.
     */
    @Override
    public void run() {
        try {
            ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
            while (!isInterrupted()) {
                setZonaActual(Zona.ZONA_COMUN);
                logger.log("[" + id + "] preparándose para salir...");
                sleepConPausa(sleepRandom(1000, 2000));
                PausaGlobal.getInstance().esperarSiPausado();
                
                //Seleccion de tunel
                int indiceTunel = random.nextInt(tuneles.length);
                Tunel tunel = tuneles[indiceTunel];

                //Entrada a tunel seleccionado
                logger.log("[" + id + "] esperando en túnel " + tuneles[indiceTunel].getZona().name());
                PausaGlobal.getInstance().esperarSiPausado();
                tunel.esperarGrupoParaSalir(this);
                
                //Cruzar tunel
                PausaGlobal.getInstance().esperarSiPausado();
                tunel.entrarTunel(this, false);
                sleepConPausa(1000);
                
                //Salir de tunel
                PausaGlobal.getInstance().esperarSiPausado();
                tunel.salirTunel(this);
                PausaGlobal.getInstance().esperarSiPausado();

                //Cambio a zona insegura
                setZonaActual(tunel.getAreaInsegura());
                logger.log("[" + id + "] explorando en " + zonaActual);
                sleepConPausa(sleepRandom(3000, 5000));
                PausaGlobal.getInstance().esperarSiPausado();
                
                //Comprobacion de ataque recibido
                if (!haSidoAtacado) {
                    comidaRecolectada += 2;
                    logger.log("[" + id + "] recolectó 2 unidades de comida.");
                }
                
                //Entrada a tunel para refugio
                PausaGlobal.getInstance().esperarSiPausado();
                tunel.entrarTunel(this, true);
                sleepConPausa(1000);
                
                //Salida de tunel 
                PausaGlobal.getInstance().esperarSiPausado();
                tunel.salirTunel(this);
                PausaGlobal.getInstance().esperarSiPausado();

                //Guardar comida si es posible
                if (comidaRecolectada > 0) {
                    ref.dejarComida(2, id);
                    comidaRecolectada = 0;
                }
                
                //Se resetea el estado de atacado
                haSidoAtacado = false;
                
                //
                PausaGlobal.getInstance().esperarSiPausado();
                setZonaActual(Zona.DESCANSO);
                logger.log("[" + id + "] descansando...");
                sleepConPausa(sleepRandom(2000, 4000));
                
                PausaGlobal.getInstance().esperarSiPausado();

                setZonaActual(Zona.COMEDOR);
                logger.log("[" + id + "] intentando comer...");
                ref.cogerComida(id);
                sleepConPausa(sleepRandom(3000, 5000));
                
                PausaGlobal.getInstance().esperarSiPausado();

                if (marcado) {
                    setZonaActual(Zona.DESCANSO);
                    logger.log("[" + id + "] curándose heridas...");
                    sleepConPausa(sleepRandom(3000, 5000));
                    PausaGlobal.getInstance().esperarSiPausado();
                    marcado = false;
                }
                
                PausaGlobal.getInstance().esperarSiPausado();
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            try {
                ApocalipsisLogger.getInstance().log("[" + id + "] interrumpido o muerto.");
            } catch (IOException ignored) {}
            Thread.currentThread().interrupt();
        } catch (IOException e) {}
    }

    /**
     * Cambia la zona actual del humano y actualiza el mapa.
     * 
     * @param nuevaZona nueva zona a la que se moverá el humano
     */
    public void setZonaActual(Zona nuevaZona) {
        synchronized(this) {
            Zona zonaAnterior = this.zonaActual;
            this.zonaActual = nuevaZona;
            mapa.moverHumano(this, zonaAnterior, nuevaZona);
        }
    }

    /**
     * Obtiene la zona actual en la que se encuentra el humano.
     * 
     * @return zona actual
     */
    public Zona getZonaActual() {
        return zonaActual;
    }
    
    /**
     * Devuelve el identificador único del humano.
     * 
     * @return id del humano
     */
    public String getIdHumano() {
        return id;
    }

    /**
     * Simula un ataque de un zombi al humano.
     * 
     * @return true si el humano fue eliminado, false si sobrevivió
     * @throws IOException si falla el log
     */
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
            for (Tunel tunel : tuneles) {
                tunel.eliminarHumanoDeColas(this);
            }
            interrupt();
            return true;
        }
    }

    /**
     * Genera un tiempo de espera aleatorio dentro de un rango.
     * 
     * @param min mínimo en milisegundos
     * @param max máximo en milisegundos
     * @return duración aleatoria en milisegundos
     * @throws InterruptedException si el hilo es interrumpido
     */
    private int sleepRandom(int min, int max) throws InterruptedException {
        return min + random.nextInt(max - min + 1);
    }
    
    /**
     * Realiza una pausa con control de pausa global.
     * 
     * @param duracionTotal duración total de la pausa en milisegundos
     * @throws InterruptedException si el hilo es interrumpido
     */
    public void sleepConPausa(long duracionTotal) throws InterruptedException {
        final long pausaIntervalo = 100; // duración máxima de cada fragmento
        long inicio = System.currentTimeMillis();

        while (true) {
            PausaGlobal.getInstance().esperarSiPausado();

            long transcurrido = System.currentTimeMillis() - inicio;
            if (transcurrido >= duracionTotal) {
                break;
            }

            long tiempoEspera = Math.min(pausaIntervalo, duracionTotal - transcurrido);
            Thread.sleep(tiempoEspera);
        }
    }
}
