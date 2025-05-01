package backend.core;

import backend.server.ServidorRMI;
import backend.utils.IdGenerator;
import backend.zones.MapaZonas;
import backend.zones.Tunel;
import backend.zones.Refugio;
import backend.entities.Zombi;
import backend.entities.Humano;
import backend.utils.PausaGlobal;
import backend.ui.ActualizadorGUI;
import frontend.ui.ApocalipsisGUI;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Simulador {

    public static void main(String[] args) throws RemoteException {
        IdGenerator idgen = new IdGenerator();
        MapaZonas mapa = new MapaZonas();
        Refugio refugio = new Refugio();
        
        // Crear túneles
        Tunel[] tuneles = {
            new Tunel(1, mapa),
            new Tunel(2, mapa),
            new Tunel(3, mapa),
            new Tunel(4, mapa)
        };
        
        ServidorRMI objRemoto = null;
        try {
            
            objRemoto = new ServidorRMI(mapa);
            // Crea e inicializa el registro RMI en el puerto 1099
            LocateRegistry.createRegistry(1099);

            // Registrar el objeto remoto en el registro
            Naming.rebind("//localhost/objeto", objRemoto);
            System.out.println("Servidor RMI listo.");

        } catch (MalformedURLException | RemoteException e) {
            System.err.println("Error en el servidor RMI: " + e.getMessage());            
        }
        
        
        // GUI
        ApocalipsisGUI gui = new ApocalipsisGUI();
        gui.setVisible(true);
        
        //Actualizador 
        ActualizadorGUI act = new ActualizadorGUI(gui, objRemoto, mapa, tuneles, refugio);
        mapa.setActualizador(act);
        refugio.setActualizador(act);
        for(Tunel tunel : tuneles){
            tunel.setActualizador(act);
        }
        
        // Crear y lanzar zombi inicial
        Zombi primerZombi = new Zombi(mapa, idgen);
        primerZombi.start();

        // Crear humanos progresivamente
        for (int i = 1; i <= 10000; i++) {
            Humano h = new Humano(idgen, refugio, tuneles, mapa);
            h.start();
            try {
                PausaGlobal.getInstance().esperarSiPausado();
                Thread.sleep((int) (Math.random() * 1500 + 500)); // Sleep entre 0.5s y 2s
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
