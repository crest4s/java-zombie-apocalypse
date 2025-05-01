    package UI;

    import Zonas.*;
    import Entidades.*;
import java.rmi.RemoteException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
    import java.util.stream.Collectors;
    import javax.swing.SwingUtilities;
import servidor.ServidorRMI;

    public class ActualizadorGUI {
        private final ApocalipsisGUI gui;
        private final MapaZonas mapa;
        private final Tunel[] tuneles;
        private final Refugio refugio;
        private final ServidorRMI objR;

        public ActualizadorGUI(ApocalipsisGUI gui, ServidorRMI objRemoto, MapaZonas mapa, Tunel[] tuneles, Refugio refugio) {
            this.gui = gui;
            this.mapa = mapa;
            this.tuneles = tuneles;
            this.refugio = refugio;
            this.objR = objRemoto;
        }

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
                switch (zona) {
                    case DESCANSO -> {
                        gui.getTextDescanso().setText(textoHumanos);
                        try {
                            objR.actualizarRefugio();
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case COMEDOR -> {
                        gui.getTextComedor().setText(textoHumanos);
                        try {
                            objR.actualizarRefugio();
                        } catch (RemoteException ex) {
                            ex.printStackTrace();                        
                        }
                    }
                    case ZONA_COMUN -> {
                        gui.getTextComun().setText(textoHumanos);
                        try {
                            objR.actualizarRefugio();
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case RIESGO_1 -> {
                        gui.getTextRiesgoHumanos1().setText(textoHumanos);
                        gui.getTextRiesgoZombis1().setText(textoZombis);
                        try {
                            objR.actualizarRiesgo();
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case RIESGO_2 -> {
                        gui.getTextRiesgoHumanos2().setText(textoHumanos);
                        gui.getTextRiesgoZombis2().setText(textoZombis);
                        try {
                            objR.actualizarRiesgo();
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case RIESGO_3 -> {
                        gui.getTextRiesgoHumanos3().setText(textoHumanos);
                        gui.getTextRiesgoZombis3().setText(textoZombis);
                        try {
                            objR.actualizarRiesgo();
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case RIESGO_4 -> {
                        gui.getTextRiesgoHumanos4().setText(textoHumanos);
                        gui.getTextRiesgoZombis4().setText(textoZombis);
                        try {
                            objR.actualizarRiesgo();
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    default -> {} // Nada si no aplica
                }
            });
        }

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
                switch (zonaTunel) {
                    case TUNEL_1 -> {
                        gui.getTextEsperaRefugio1().setText(textoRefugio);
                        gui.getTextEsperaRiesgo1().setText(textoRiesgo);
                        try {
                            objR.actualizarTunel1(colaRefugio.size() + colaRiesgo.size());
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case TUNEL_2 -> {
                        gui.getTextEsperaRefugio2().setText(textoRefugio);
                        gui.getTextEsperaRiesgo2().setText(textoRiesgo);
                        try {
                            objR.actualizarTunel2(colaRefugio.size() + colaRiesgo.size());
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case TUNEL_3 -> {
                        gui.getTextEsperaRefugio3().setText(textoRefugio);
                        gui.getTextEsperaRiesgo3().setText(textoRiesgo);
                        try {
                            objR.actualizarTunel3(colaRefugio.size() + colaRiesgo.size());
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case TUNEL_4 -> {
                        gui.getTextEsperaRefugio4().setText(textoRefugio);
                        gui.getTextEsperaRiesgo4().setText(textoRiesgo);
                        try {
                            objR.actualizarTunel4(colaRefugio.size() + colaRiesgo.size());
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    default -> {} // Nada si no es túnel
                }
            });
        }

        public synchronized void actualizarComida() {
            int cantidad = refugio.getCantidadComida();
            SwingUtilities.invokeLater(() -> gui.getContadorComida().setText(String.valueOf(cantidad)));
        }
    }
