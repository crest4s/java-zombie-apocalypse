package servidor;

import Entidades.Zombi;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import Zonas.MapaZonas;
import Helpers.PausaGlobal;
import Zonas.Zona;
import java.util.LinkedHashMap;
import java.util.List;

public class ServidorRMI extends UnicastRemoteObject implements ServidorRemoto{
    private final MapaZonas mapa;
    private boolean actualizacion;

    public ServidorRMI(MapaZonas mapa) throws RemoteException {
        this.mapa = mapa;
        actualizacion = false;
    }

    public int getHumanosRefugio() throws RemoteException {
        return (mapa.humanosEnZona(Zona.DESCANSO).size()+mapa.humanosEnZona(Zona.COMEDOR).size()+mapa.humanosEnZona(Zona.ZONA_COMUN).size());
    }
    
    //@Override
    public int[] getHumanosTuneles() throws RemoteException {
        int[] humTunel = new int[4];
        
        humTunel[0] = mapa.humanosEnZona(Zona.TUNEL_1).size();
        humTunel[1] = mapa.humanosEnZona(Zona.TUNEL_2).size();
        humTunel[2] = mapa.humanosEnZona(Zona.TUNEL_3).size();
        humTunel[3] = mapa.humanosEnZona(Zona.TUNEL_4).size();
        
        return humTunel;
    }

    //@Override
    public int[] getHumanosZonasRiesgo() throws RemoteException {
        int[] humRiesgo = new int[4];

        humRiesgo[0] = mapa.humanosEnZona(Zona.RIESGO_1).size();
        humRiesgo[1] = mapa.humanosEnZona(Zona.RIESGO_2).size();
        humRiesgo[2] = mapa.humanosEnZona(Zona.RIESGO_3).size();
        humRiesgo[3] = mapa.humanosEnZona(Zona.RIESGO_4).size();

        return humRiesgo;
    }

    //@Override
    public int[] getZombisZonasRiesgo() throws RemoteException {
        int[] zombRiesgo = new int[4];

        zombRiesgo[0] = mapa.zombisEnZona(Zona.RIESGO_1).size();
        zombRiesgo[1] = mapa.zombisEnZona(Zona.RIESGO_2).size();
        zombRiesgo[2] = mapa.zombisEnZona(Zona.RIESGO_3).size();
        zombRiesgo[3] = mapa.zombisEnZona(Zona.RIESGO_4).size();

        return zombRiesgo;
    }

    //@Override
    public Map<String, Integer> getTopZombisLetales() throws RemoteException {
        Map<String, Integer> topZombis = new HashMap<>();
        
        List<Zombi> zombis = mapa.getAllZombi();
        
        for (Zombi z: zombis){
            topZombis.put(z.getIdZombi(), z.getMuertes());
        }
        
        return topZombis.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(3)
                    .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                    ));
    }

    //@Override
    public void togglePausa() throws RemoteException {
        PausaGlobal pausa = PausaGlobal.getInstance();
    
        if (pausa.estaPausado()) {
            pausa.reanudar();
        } else {
            pausa.pausar();
        }
    }
    
    public void actualizarRefugio() throws RemoteException{
        
    }
    
    public void actualizarRiesgo() throws RemoteException{
        
    }
    
    public void actualizarTunel() throws RemoteException{
        
    }
    
    public boolean necesitaActualizar() throws RemoteException{
        boolean act = actualizacion;
        if (act) {
            actualizacion = false;
        }
        return act;
    }
}