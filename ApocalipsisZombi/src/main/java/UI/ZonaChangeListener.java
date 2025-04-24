package UI;

import Zonas.Zona;

public interface ZonaChangeListener {
     /**
     * Método invocado cuando una entidad cambia de zona.
     *
     * @param entidadId     ID del humano o zombi
     * @param zonaAnterior  Zona donde estaba anteriormente (puede ser null si acaba de ser creado)
     * @param zonaNueva     Zona nueva donde ha entrado (puede ser null si acaba de salir o morir)
     * @param esZombi       true si es un zombi, false si es un humano
     */
    void onCambioZona(String entidadId, Zona zonaAnterior, Zona zonaNueva, boolean esZombi);
}
