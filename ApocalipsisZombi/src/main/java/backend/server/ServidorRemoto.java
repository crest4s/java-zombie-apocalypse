package backend.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Interfaz remota para el servidor RMI del simulador apocalíptico.
 * Define los métodos que pueden ser invocados remotamente por un cliente.
 */
public interface ServidorRemoto extends Remote {

    /**
     * Obtiene el número total de humanos que se encuentran actualmente en el refugio.
     *
     * @return número de humanos en zonas internas del refugio
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    int getHumanosRefugio() throws RemoteException;

    /**
     * Obtiene la cantidad de humanos que hay en cada uno de los túneles.
     * 
     * @return array con 4 posiciones, una por túnel
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    int[] getHumanosTuneles() throws RemoteException;

    /**
     * Devuelve el número de humanos en cada zona de riesgo.
     * 
     * @return array con 4 posiciones, una por cada zona de riesgo
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    int[] getHumanosZonasRiesgo() throws RemoteException;

    /**
     * Devuelve el número de zombis en cada zona de riesgo.
     * 
     * @return array con 4 posiciones, una por cada zona de riesgo
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    int[] getZombisZonasRiesgo() throws RemoteException;

    /**
     * Obtiene un ranking con los 3 zombis más letales (con más muertes).
     *
     * @return mapa con los IDs de zombis y su conteo de muertes, ordenado descendentemente
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    Map<String, Integer> getTopZombisLetales() throws RemoteException;

    /**
     * Alterna el estado de pausa global de la simulación (pausar o reanudar).
     *
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    void togglePausa() throws RemoteException;

    /**
     * Marca que se requiere una actualización del estado del refugio
     * y devuelve el total actualizado de humanos en él.
     *
     * @return número actualizado de humanos en el refugio
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    int actualizarRefugio() throws RemoteException;

    /**
     * Marca que se requiere una actualización de las zonas de riesgo
     * y devuelve la cantidad de humanos y zombis en cada una.
     *
     * @return array de 8 posiciones: [H1, H2, H3, H4, Z1, Z2, Z3, Z4]
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    int[] actualizarRiesgo() throws RemoteException;

    /**
     * Actualiza el número de humanos en el extremo del túnel 1.
     *
     * @param i número de humanos
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    void actualizarTunel1(int i) throws RemoteException;

    /**
     * Actualiza el número de humanos en el extremo del túnel 2.
     *
     * @param i número de humanos
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    void actualizarTunel2(int i) throws RemoteException;

    /**
     * Actualiza el número de humanos en el extremo del túnel 3.
     *
     * @param i número de humanos
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    void actualizarTunel3(int i) throws RemoteException;

    /**
     * Actualiza el número de humanos en el extremo del túnel 4.
     *
     * @param i número de humanos
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    void actualizarTunel4(int i) throws RemoteException;

    /**
     * Indica si se ha solicitado una actualización desde el servidor.
     *
     * @return true si hay una actualización pendiente, false si no
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    boolean necesitaActualizar() throws RemoteException;

    /**
     * Indica si la simulación está actualmente en pausa.
     *
     * @return true si la simulación está pausada, false si no
     * @throws RemoteException si ocurre un error de comunicación remota
     */
    boolean estaPausado() throws RemoteException;

}
