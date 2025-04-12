/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package programacion.avanzada.apocalipsiszombi;

import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author crestas
 */
public class Simulador {

    public static void main(String[] args) {
        IdGenerator idgen = new IdGenerator();
        Tunel[] tun = {new Tunel(1), new Tunel(2), new Tunel(3), new Tunel(4)}; 
        Refugio ref = new Refugio(tun);
        
        
        for(int i = 1; i<=10000; i++){
            new Humano(idgen, ref, tun).start();
        } 
        
        Zombi z = new Zombi(idgen);
        z.start();
    }
}
