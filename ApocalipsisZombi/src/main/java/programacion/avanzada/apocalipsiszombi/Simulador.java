package programacion.avanzada.apocalipsiszombi;

import Helpers.IdGenerator;
import Zonas.MapaZonas;
import Zonas.Tunel;
import Zonas.Refugio;
import Entidades.Zombi;
import Entidades.Humano;

public class Simulador {

    public static void main(String[] args) {
        IdGenerator idgen = new IdGenerator();
        MapaZonas mapa = new MapaZonas();
        Refugio refugio = new Refugio();
        
        // Crear túneles
        Tunel[] tuneles = {
            new Tunel(1),
            new Tunel(2),
            new Tunel(3),
            new Tunel(4)
        };

        // Crear y lanzar zombi inicial
        Zombi primerZombi = new Zombi(mapa);
        primerZombi.start();

        // Crear humanos progresivamente
        for (int i = 1; i <= 10000; i++) {
            Humano h = new Humano(idgen, refugio, tuneles, mapa);
            h.start();
            try {
                Thread.sleep((int) (Math.random() * 1500 + 500)); // Sleep entre 0.5s y 2s
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
