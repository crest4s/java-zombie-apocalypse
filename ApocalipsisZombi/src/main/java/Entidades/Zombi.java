package Entidades;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import Helpers.ApocalipsisLogger;
import Helpers.IdGenerator;
import Zonas.MapaZonas;
import Zonas.Zona;

public class Zombi extends Thread{
    private String id;
    private int muertes;
    public Zona zonaActual;
    private final Random random = new Random();
    private MapaZonas mapa;
    
    public Zombi(IdGenerator idgen, String humId, MapaZonas mapa){
        this.id = idgen.nuevoIdZombi(humId);
        this.mapa = mapa;
        this.muertes = 0;
    }
    
    public Zombi(MapaZonas mapa){
        this.muertes = 0;
        this.mapa = mapa;
        this.id = "Z00000";
    }
    
    @Override
    public void run(){
        try {
            ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
            while (true) {
                try {
                    // Moverse a otra zona
                    int zona = random.nextInt(4) + 1;
                    setZonaActual(Zona.valueOf("RIESGO_" + zona));
                    logger.log("[" + id + "] entra a " + zonaActual);
                    
                    // Buscar humanos en la zona actual
                    List<Humano> humanosDisponibles = mapa.humanosEnZona(zonaActual);
                    
                    if (!humanosDisponibles.isEmpty()) {
                        //Selección de humano a atacar
                        Humano humanoObjetivo = humanosDisponibles.get(random.nextInt(humanosDisponibles.size()));
                        logger.log("[" + id + "] atacando al humano " + humanoObjetivo.getIdHumano());
                        sleepRandom(500, 1500);

                        boolean humanoMuerto = humanoObjetivo.serAtacado();

                        if (humanoMuerto) {
                            muertes++;
                            Zombi nuevoZombi = convertirEnZombi(humanoObjetivo);
                            logger.log("[" + id + "] mató al humano. Total muertes: " + muertes);
                        } else {
                            logger.log("[" + id + "] el humano se defendió. Sigue con vida.");
                        }  
                    } else {
                        logger.log("[" + id + "] no hay nadie. Espera...");
                        sleepRandom(2000, 3000);
                    }
                    
                } catch (InterruptedException e) {
                    logger.log("[" + id + "] interrumpido.");
                    break;
                } 
            }
        } catch (IOException ex) {}
    }
    
    public Zona getZonaActual(){
        return zonaActual;
    }
    
    public void setZonaActual(Zona zona){
        if(this.zonaActual != null) {
            mapa.quitarZombiZona(this, this.zonaActual); // Quitar de la zona anterior
        }
        this.zonaActual = zona;
        mapa.guardarZombi(this, zonaActual); // Agregar a la nueva zona
    }
    
    public String getIdZombi() {
        return id;
    }
     
    private void sleepRandom(int min, int max) throws InterruptedException {
        sleep(min + random.nextInt(max - min + 1));
    }
    
    public Zombi convertirEnZombi(Humano h) throws IOException {        
        // Verificar que el humano no sea ya un zombi
        if (h.getIdHumano().startsWith("Z")) {
            //logger.log("[" + id + "]" + "Error: Intento de convertir un zombi en zombi: " + h.getIdHumano());
            return null;
        }

        // Crear nuevo zombi usando el ID del humano
        Zombi zombi = new Zombi(new IdGenerator(), h.getIdHumano(), mapa);
        Zona zona = h.getZonaActual();

        // Quitar humano y agregar zombi al mapa
        mapa.quitarHumanoZona(h, zona);
        mapa.guardarZombi(zombi, zona);

        ApocalipsisLogger logger = ApocalipsisLogger.getInstance();
        logger.log("[" + id + "] " + h.getIdHumano() + " ha sido convertido en " + zombi.getIdZombi());

        h.interrupt(); // El humano deja de ejecutar su run()
        zombi.start(); // El nuevo zombi comienza a actuar

        return zombi;
    }
}
