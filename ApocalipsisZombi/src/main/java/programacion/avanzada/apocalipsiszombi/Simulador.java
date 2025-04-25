package programacion.avanzada.apocalipsiszombi;

import Helpers.IdGenerator;
import Zonas.MapaZonas;
import Zonas.Tunel;
import Zonas.Refugio;
import Entidades.Zombi;
import Entidades.Humano;
import UI.ApocalipsisGUI;
import static java.lang.Thread.sleep;

public class Simulador {

    public static void main(String[] args) throws InterruptedException {
        IdGenerator idgen = new IdGenerator();
        Tunel[] tun = {new Tunel(1), new Tunel(2), new Tunel(3), new Tunel(4)}; 
        Refugio ref = new Refugio();
        MapaZonas mapa = new MapaZonas();
        
        ApocalipsisGUI gui = new ApocalipsisGUI(mapa, tun);
        gui.setVisible(true);

        Zombi z = new Zombi(mapa);
        z.start();
        
        for(int i = 1; i<=10000; i++){
            new Humano(idgen, ref, tun, mapa, gui).start();
            sleep((int)(Math.random()*1500 + 500));
        }
    }
}
