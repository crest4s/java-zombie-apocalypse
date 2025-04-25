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
        MapaZonas mapa = new MapaZonas();
        Refugio ref = new Refugio();
        ApocalipsisGUI gui = new ApocalipsisGUI(mapa, new Tunel[0]);
        
        Tunel[] tun = {new Tunel(1, gui), new Tunel(2, gui), new Tunel(3, gui), new Tunel(4, gui)}; 
        
        gui.setTuneles(tun);
        gui.setVisible(true);

        Zombi z = new Zombi(mapa);
        z.start();
        
        for(int i = 1; i<=10000; i++){
            new Humano(idgen, ref, tun, mapa, gui).start();
            sleep((int)(Math.random()*1500 + 500));
        }
    }
}
