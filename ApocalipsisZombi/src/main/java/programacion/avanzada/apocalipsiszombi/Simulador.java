package programacion.avanzada.apocalipsiszombi;

import Helpers.IdGenerator;
import Zonas.MapaZonas;
import Zonas.Tunel;
import Zonas.Refugio;
import Entidades.Zombi;
import Entidades.Humano;
import static java.lang.Thread.sleep;

public class Simulador {

    public static void main(String[] args) throws InterruptedException {
        IdGenerator idgen = new IdGenerator();
        Tunel[] tun = {new Tunel(1), new Tunel(2), new Tunel(3), new Tunel(4)}; 
        Refugio ref = new Refugio();
        MapaZonas mapa = new MapaZonas();
        
        //MonitorSistema2 monitor = new MonitorSistema2(mapa, ref);
        //monitor.setVisible(true);
        
        Zombi z = new Zombi(mapa);
        z.start();
        
        for(int i = 1; i<=1000; i++){
            new Humano(idgen, ref, tun, mapa).start();
            sleep((int)(Math.random()*1500 + 500));
        }
    }
}
