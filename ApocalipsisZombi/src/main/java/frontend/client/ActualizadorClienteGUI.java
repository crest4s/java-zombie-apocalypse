package frontend.client;

import backend.server.ServidorRemoto;

import java.rmi.RemoteException;
import java.util.Map;
import javax.swing.SwingUtilities;

/**
 * Clase responsable de actualizar periódicamente la interfaz gráfica del cliente
 * en función de los datos obtenidos desde el servidor RMI.
 * Se ejecuta en un hilo separado.
 */
public class ActualizadorClienteGUI implements Runnable {
    private final ClienteGUI gui;
    private final ServidorRemoto servidor;

    /**
     * Constructor que inicializa la GUI del cliente y la conexión al servidor.
     *
     * @param gui interfaz gráfica del cliente
     * @param servidor referencia remota al servidor RMI
     */
    public ActualizadorClienteGUI(ClienteGUI gui, ServidorRemoto servidor) {
        this.gui = gui;
        this.servidor = servidor;
    }

    /**
     * Bucle principal del hilo.
     * Consulta periódicamente al servidor para verificar si hay datos nuevos.
     * Si los hay, actualiza la interfaz gráfica con la información más reciente.
     */
    @Override
    public void run() {
        while (true) {
            try {
                if (servidor.necesitaActualizar()) {
                    actualizarDatos();
                }
                // Actualiza el botón de pausa/reanudación según el estado del servidor
                boolean pausado = servidor.estaPausado();
                SwingUtilities.invokeLater(() -> {
                    if (pausado && !gui.getPauseResumeButton().getText().equals("Reanudar")) {
                        gui.getPauseResumeButton().setText("Reanudar");
                    } else if (!pausado && !gui.getPauseResumeButton().getText().equals("Pausar")) {
                        gui.getPauseResumeButton().setText("Pausar");
                    }
                });
                Thread.sleep(200); // Evita uso excesivo de CPU
            } catch (RemoteException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Obtiene los datos del servidor y actualiza la GUI en el hilo de eventos de Swing.
     *
     * @throws RemoteException si ocurre un error al comunicarse con el servidor RMI
     */
    private void actualizarDatos() throws RemoteException {
        int refugio = servidor.getHumanosRefugio();
        int[] tuneles = servidor.getHumanosTuneles();
        int[] zonas = servidor.getHumanosZonasRiesgo();
        int[] zombis = servidor.getZombisZonasRiesgo();
        Map<String, Integer> ranking = servidor.getTopZombisLetales();

        SwingUtilities.invokeLater(() -> {
            gui.getTextRefugio().setText(String.valueOf(refugio));

            gui.getTextTunel1().setText(String.valueOf(tuneles[0]));
            gui.getTextTunel2().setText(String.valueOf(tuneles[1]));
            gui.getTextTunel3().setText(String.valueOf(tuneles[2]));
            gui.getTextTunel4().setText(String.valueOf(tuneles[3]));

            gui.getTextRiesgo1H().setText(String.valueOf(zonas[0]));
            gui.getTextRiesgo2H().setText(String.valueOf(zonas[1]));
            gui.getTextRiesgo3H().setText(String.valueOf(zonas[2]));
            gui.getTextRiesgo4H().setText(String.valueOf(zonas[3]));

            gui.getTextRiesgo1Z().setText(String.valueOf(zombis[0]));
            gui.getTextRiesgo2Z().setText(String.valueOf(zombis[1]));
            gui.getTextRiesgo3Z().setText(String.valueOf(zombis[2]));
            gui.getTextRiesgo4Z().setText(String.valueOf(zombis[3]));

            StringBuilder sb = new StringBuilder();
            ranking.forEach((id, muertes) -> sb.append(id).append(": ").append(muertes).append(" muertes\n"));
            gui.getTopZombisTextArea().setText(sb.toString());
        });
    }
}
