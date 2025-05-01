package backend.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ServidorRemoto extends Remote{
    // Métodos para obtener datos
    int getHumanosRefugio() throws RemoteException;
    int[] getHumanosTuneles() throws RemoteException;
    int[] getHumanosZonasRiesgo() throws RemoteException;
    int[] getZombisZonasRiesgo() throws RemoteException;
    Map<String, Integer> getTopZombisLetales() throws RemoteException;

    // Método para pausar/reanudar
    void togglePausa() throws RemoteException;
    
    // Métodos para actualizar la informacion cuando sea necesario
    int actualizarRefugio() throws RemoteException;
    int[] actualizarRiesgo() throws RemoteException;
    void actualizarTunel1(int i) throws RemoteException;
    void actualizarTunel2(int i) throws RemoteException;
    void actualizarTunel3(int i) throws RemoteException;
    void actualizarTunel4(int i) throws RemoteException;
    boolean necesitaActualizar() throws RemoteException;
}
