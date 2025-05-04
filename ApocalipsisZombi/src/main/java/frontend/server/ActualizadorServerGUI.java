package frontend.server;

import backend.entities.Humano;
import backend.entities.Zombi;
import backend.server.ServidorRMI;
import backend.zones.*;

import javax.swing.SwingUtilities;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Clase encargada de sincronizar los datos del backend con la interfaz gráfica del servidor.
 * Actualiza el estado de zonas, túneles, colas y comida en la GUI y comunica cambios al servidor RMI.
 */
public class ActualizadorServerGUI {
    private final ApocalipsisGUI gui;
    private final MapaZonas mapa;
    private final Tunel[] tuneles;
    private final Refugio refugio;
    private final ServidorRMI objR;

    /**
     * Constructor del actualizador de GUI.
     *
     * @param gui      interfaz gráfica del servidor
     * @param objRemoto referencia al objeto remoto RMI para comunicar actualizaciones
     * @param mapa     instancia del mapa de zonas
     * @param tuneles  arreglo de túneles del sistema
     * @param refugio  instancia del refugio donde se almacena comida
     */
    public ActualizadorServerGUI(ApocalipsisGUI gui, ServidorRMI objRemoto, MapaZonas mapa, Tunel[] tuneles, Refugio refugio) {
        this.gui = gui;
        this.mapa = mapa;
        this.tuneles = tuneles;
        this.refugio = refugio;
        this.objR = objRemoto;
    }

    /**
     * Actualiza la visualización de una zona determinada, incluyendo humanos y zombis.
     *
     * @param zona zona a actualizar
     */
    public synchronized void actualizarZona(Zona zona) {
        List<Humano> humanos = mapa.humanosEnZona(zona);
        List<Zombi> zombis = mapa.zombisEnZona(zona);

        String textoHumanos = humanos.stream()
                                     .map(Humano::getIdHumano)
                                     .collect(Collectors.joining(", "));
        String textoZombis = zombis.stream()
                                   .map(Zombi::getIdZombi)
                                   .collect(Collectors.joining(", "));

        SwingUtilities.invokeLater(() -> {
            try {
                switch (zona) {
                    case DESCANSO -> {
                        gui.getTextDescanso().setText(textoHumanos);
                        objR.actualizarRefugio();
                    }
                    case COMEDOR -> {
                        gui.getTextComedor().setText(textoHumanos);
                        objR.actualizarRefugio();
                    }
                    case ZONA_COMUN -> {
                        gui.getTextComun().setText(textoHumanos);
                        objR.actualizarRefugio();
                    }
                    case RIESGO_1 -> {
                        gui.getTextRiesgoHumanos1().setText(textoHumanos);
                        gui.getTextRiesgoZombis1().setText(textoZombis);
                        objR.actualizarRiesgo();
                    }
                    case RIESGO_2 -> {
                        gui.getTextRiesgoHumanos2().setText(textoHumanos);
                        gui.getTextRiesgoZombis2().setText(textoZombis);
                        objR.actualizarRiesgo();
                    }
                    case RIESGO_3 -> {
                        gui.getTextRiesgoHumanos3().setText(textoHumanos);
                        gui.getTextRiesgoZombis3().setText(textoZombis);
                        objR.actualizarRiesgo();
                    }
                    case RIESGO_4 -> {
                        gui.getTextRiesgoHumanos4().setText(textoHumanos);
                        gui.getTextRiesgoZombis4().setText(textoZombis);
                        objR.actualizarRiesgo();
                    }
                    default -> {}
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Actualiza la interfaz gráfica con el humano que está cruzando actualmente un túnel.
     *
     * @param tunel túnel que se desea actualizar
     */
    public synchronized void actualizarTunel(Tunel tunel) {
        Humano cruzando = tunel.getCruzando();
        String texto = (cruzando != null) ? cruzando.getIdHumano() : "";

        SwingUtilities.invokeLater(() -> {
            switch (tunel.getZona()) {
                case TUNEL_1 -> gui.getTunel1().setText(texto);
                case TUNEL_2 -> gui.getTunel2().setText(texto);
                case TUNEL_3 -> gui.getTunel3().setText(texto);
                case TUNEL_4 -> gui.getTunel4().setText(texto);
            }
        });
    }

    /**
     * Actualiza las colas de humanos esperando en cada extremo de un túnel.
     *
     * @param zonaTunel zona del túnel correspondiente
     */
    public synchronized void actualizarColas(Zona zonaTunel) {
        int idx = zonaTunel.ordinal() - Zona.TUNEL_1.ordinal();
        if (idx < 0 || idx >= tuneles.length) return;

        Tunel tunel = tuneles[idx];
        Queue<Humano> colaRefugio = tunel.getColaRefugio();
        Queue<Humano> colaRiesgo = tunel.getColaRiesgo();

        String textoRefugio = new ArrayList<>(colaRefugio).stream()
                .map(Humano::getIdHumano)
                .collect(Collectors.joining(", "));

        String textoRiesgo = colaRiesgo.stream()
                .map(Humano::getIdHumano)
                .collect(Collectors.joining(", "));

        SwingUtilities.invokeLater(() -> {
            try {
                switch (zonaTunel) {
                    case TUNEL_1 -> {
                        gui.getTextEsperaRefugio1().setText(textoRefugio);
                        gui.getTextEsperaRiesgo1().setText(textoRiesgo);
                        objR.actualizarTunel1(colaRefugio.size() + colaRiesgo.size());
                    }
                    case TUNEL_2 -> {
                        gui.getTextEsperaRefugio2().setText(textoRefugio);
                        gui.getTextEsperaRiesgo2().setText(textoRiesgo);
                        objR.actualizarTunel2(colaRefugio.size() + colaRiesgo.size());
                    }
                    case TUNEL_3 -> {
                        gui.getTextEsperaRefugio3().setText(textoRefugio);
                        gui.getTextEsperaRiesgo3().setText(textoRiesgo);
                        objR.actualizarTunel3(colaRefugio.size() + colaRiesgo.size());
                    }
                    case TUNEL_4 -> {
                        gui.getTextEsperaRefugio4().setText(textoRefugio);
                        gui.getTextEsperaRiesgo4().setText(textoRiesgo);
                        objR.actualizarTunel4(colaRefugio.size() + colaRiesgo.size());
                    }
                    default -> {}
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Actualiza el contador visual de comida disponible en el refugio.
     */
    public synchronized void actualizarComida() {
        int cantidad = refugio.getCantidadComida();
        SwingUtilities.invokeLater(() -> gui.getContadorComida().setText(String.valueOf(cantidad)));
    }
}
