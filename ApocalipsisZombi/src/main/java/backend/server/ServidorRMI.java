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

/**
 * Implementación del servidor remoto usando RMI para la simulación apocalíptica.
 * Permite consultar y actualizar el estado del sistema desde el cliente.
 */
public class ServidorRMI extends UnicastRemoteObject implements ServidorRemoto{
    private final MapaZonas mapa;
    private boolean actualizacion;
    private final int[] extremoTuneles;

    /**
     * Constructor del servidor RMI.
     *
     * @param mapa instancia del mapa de zonas compartido
     * @throws RemoteException si ocurre un error de conexión remota
     */
    public ServidorRMI(MapaZonas mapa) throws RemoteException {
        this.mapa = mapa;
        actualizacion = false;
        extremoTuneles = new int[4];
    }
    
    /**
     * Devuelve el total de humanos que están dentro del refugio (zonas internas).
     *
     * @throws RemoteException 
     * @return número total de humanos en el refugio
     */
    @Override
    public int getHumanosRefugio() throws RemoteException {
        return (mapa.humanosEnZona(Zona.DESCANSO).size()+mapa.humanosEnZona(Zona.COMEDOR).size()+mapa.humanosEnZona(Zona.ZONA_COMUN).size());
    }
    
    /**
     * Devuelve la cantidad de humanos presentes en cada túnel (considerando ambos extremos).
     *
     * @throws RemoteException 
     * @return array de 4 posiciones, una por túnel
     */
    @Override
    public int[] getHumanosTuneles() throws RemoteException {
        int[] humTunel = new int[4];
        
        humTunel[0] = mapa.humanosEnZona(Zona.TUNEL_1).size() + extremoTuneles[0];
        humTunel[1] = mapa.humanosEnZona(Zona.TUNEL_2).size() + extremoTuneles[1];
        humTunel[2] = mapa.humanosEnZona(Zona.TUNEL_3).size() + extremoTuneles[2];
        humTunel[3] = mapa.humanosEnZona(Zona.TUNEL_4).size() + extremoTuneles[3];
        
        return humTunel;
    }

    /**
     * Devuelve el número de humanos presentes en cada zona de riesgo.
     *
     * @return array de 4 posiciones correspondientes a las zonas de riesgo
     */
    @Override
    public int[] getHumanosZonasRiesgo() throws RemoteException {
        int[] humRiesgo = new int[4];

        humRiesgo[0] = mapa.humanosEnZona(Zona.RIESGO_1).size();
        humRiesgo[1] = mapa.humanosEnZona(Zona.RIESGO_2).size();
        humRiesgo[2] = mapa.humanosEnZona(Zona.RIESGO_3).size();
        humRiesgo[3] = mapa.humanosEnZona(Zona.RIESGO_4).size();

        return humRiesgo;
    }

    /**
     * Devuelve el número de zombis presentes en cada zona de riesgo.
     * 
     * @throws RemoteException 
     * @return array de 4 posiciones correspondientes a las zonas de riesgo
     */
    @Override
    public int[] getZombisZonasRiesgo() throws RemoteException {
        int[] zombRiesgo = new int[4];

        zombRiesgo[0] = mapa.zombisEnZona(Zona.RIESGO_1).size();
        zombRiesgo[1] = mapa.zombisEnZona(Zona.RIESGO_2).size();
        zombRiesgo[2] = mapa.zombisEnZona(Zona.RIESGO_3).size();
        zombRiesgo[3] = mapa.zombisEnZona(Zona.RIESGO_4).size();

        return zombRiesgo;
    }

    /**
     * Devuelve el top 3 de zombis más letales (por número de muertes).
     * 
     * @throws RemoteException 
     * @return mapa con IDs de zombis y número de muertes, ordenado descendentemente
     */

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

    /**
     */
    
    /**
     * Activa o desactiva la pausa global en la simulación.
     * 
     * @throws RemoteException 
     */
    @Override
    public void togglePausa() throws RemoteException {
        PausaGlobal pausa = PausaGlobal.getInstance();
    
        if (pausa.estaPausado()) {
            pausa.reanudar();
        } else {
            pausa.pausar();
        }
    }
    
    /**
     * Señaliza que se necesita actualizar el estado del refugio y devuelve el total actual.
     * 
     * @throws RemoteException 
     */
    @Override
    public int actualizarRefugio() throws RemoteException{
        actualizacion = true;
        return getHumanosRefugio();
    }
    
    /**
     * Señaliza que se necesita actualizar las zonas de riesgo y devuelve humanos y zombis.
     *
     * @return array de 8 posiciones: 4 primeros para humanos, 4 últimos para zombis
     * @throws RemoteException 
     */
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
    
    /**
     * Actualiza el conteo de humanos en el extremo del túnel 1.
     * 
     * @param i
     * @throws RemoteException 
     */
    @Override
    public void actualizarTunel1(int i) throws RemoteException{
        actualizacion  = true;
        extremoTuneles[0]=i;
    }
    
    /**
     * Actualiza el conteo de humanos en el extremo del túnel 2.
     * 
     * @param i
     * @throws RemoteException 
     */
    @Override
    public void actualizarTunel2(int i) throws RemoteException{
        actualizacion  = true;
        extremoTuneles[1]=i;
    }
    
    /**
     * Actualiza el conteo de humanos en el extremo del túnel 3.
     * 
     * @param i
     * @throws RemoteException 
     */
    @Override
    public void actualizarTunel3(int i) throws RemoteException{
        actualizacion  = true;
        extremoTuneles[2]=i;
    }
    
    /**
     * Actualiza el conteo de humanos en el extremo del túnel 4.
     * 
     * @param i
     * @throws RemoteException 
     */
    @Override
    public void actualizarTunel4(int i) throws RemoteException{
        actualizacion  = true;
        extremoTuneles[3]=i;
    }
    /**
     * Indica si hay datos nuevos que requieren actualización en la interfaz.
     * 
     * @return true si se ha solicitado actualización, false en caso contrario
     * @throws RemoteException 
     */
    @Override
    public boolean necesitaActualizar() throws RemoteException{
        boolean act = actualizacion;
        if (act) {
            actualizacion = false;
        }
        return act;
    }
}