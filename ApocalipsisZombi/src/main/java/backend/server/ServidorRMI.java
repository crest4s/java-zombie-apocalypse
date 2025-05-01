package backend.server;

import backend.entities.Zombi;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import backend.zones.MapaZonas;
import backend.utils.PausaGlobal;
import backend.zones.Zona;
import java.util.LinkedHashMap;
import java.util.List;

public class ServidorRMI extends UnicastRemoteObject implements ServidorRemoto{
    private final MapaZonas mapa;
    private boolean actualizacion;
    private final int[] extremoTuneles;

    public ServidorRMI(MapaZonas mapa) throws RemoteException {
        this.mapa = mapa;
        actualizacion = false;
        extremoTuneles = new int[4];
    }

    @Override
    public int getHumanosRefugio() throws RemoteException {
        return (mapa.humanosEnZona(Zona.DESCANSO).size()+mapa.humanosEnZona(Zona.COMEDOR).size()+mapa.humanosEnZona(Zona.ZONA_COMUN).size());
    }
    
    @Override
    public int[] getHumanosTuneles() throws RemoteException {
        int[] humTunel = new int[4];
        
        humTunel[0] = mapa.humanosEnZona(Zona.TUNEL_1).size() + extremoTuneles[0];
        humTunel[1] = mapa.humanosEnZona(Zona.TUNEL_2).size() + extremoTuneles[1];
        humTunel[2] = mapa.humanosEnZona(Zona.TUNEL_3).size() + extremoTuneles[2];
        humTunel[3] = mapa.humanosEnZona(Zona.TUNEL_4).size() + extremoTuneles[3];
        
        return humTunel;
    }

    @Override
    public int[] getHumanosZonasRiesgo() throws RemoteException {
        int[] humRiesgo = new int[4];

        humRiesgo[0] = mapa.humanosEnZona(Zona.RIESGO_1).size();
        humRiesgo[1] = mapa.humanosEnZona(Zona.RIESGO_2).size();
        humRiesgo[2] = mapa.humanosEnZona(Zona.RIESGO_3).size();
        humRiesgo[3] = mapa.humanosEnZona(Zona.RIESGO_4).size();

        return humRiesgo;
    }

    @Override
    public int[] getZombisZonasRiesgo() throws RemoteException {
        int[] zombRiesgo = new int[4];

        zombRiesgo[0] = mapa.zombisEnZona(Zona.RIESGO_1).size();
        zombRiesgo[1] = mapa.zombisEnZona(Zona.RIESGO_2).size();
        zombRiesgo[2] = mapa.zombisEnZona(Zona.RIESGO_3).size();
        zombRiesgo[3] = mapa.zombisEnZona(Zona.RIESGO_4).size();

        return zombRiesgo;
    }

    @Override
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

    @Override
    public void togglePausa() throws RemoteException {
        PausaGlobal pausa = PausaGlobal.getInstance();
    
        if (pausa.estaPausado()) {
            pausa.reanudar();
        } else {
            pausa.pausar();
        }
    }
    
    @Override
    public int actualizarRefugio() throws RemoteException{
        actualizacion = true;
        return getHumanosRefugio();
    }
    
    @Override
    public int[] actualizarRiesgo() throws RemoteException{
        actualizacion = true;
        int[] humanosZombisRiesgo = new int[8];
        for (int i = 0; i<8; i++){
            if (i<4){
                humanosZombisRiesgo[i] = getHumanosZonasRiesgo()[i];
            } else {
                humanosZombisRiesgo[i] = getZombisZonasRiesgo()[i-4];
            }
        }
        return humanosZombisRiesgo;
    }
    
    @Override
    public void actualizarTunel1(int i) throws RemoteException{
        actualizacion  = true;
        extremoTuneles[0]=i;
    }
    
    @Override
    public void actualizarTunel2(int i) throws RemoteException{
        actualizacion  = true;
        extremoTuneles[1]=i;
    }
    
    @Override
    public void actualizarTunel3(int i) throws RemoteException{
        actualizacion  = true;
        extremoTuneles[2]=i;
    }
    
    @Override
    public void actualizarTunel4(int i) throws RemoteException{
        actualizacion  = true;
        extremoTuneles[3]=i;
    }
        
    @Override
    public boolean necesitaActualizar() throws RemoteException{
        boolean act = actualizacion;
        if (act) {
            actualizacion = false;
        }
        return act;
    }
}