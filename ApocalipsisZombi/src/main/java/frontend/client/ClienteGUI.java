package frontend.client;

import backend.server.ServidorRemoto;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Interfaz gráfica del cliente que se conecta al servidor RMI para visualizar
 * el estado de la simulación (refugio, túneles, zonas de riesgo y top de zombis).
 */
public class ClienteGUI extends javax.swing.JFrame {

    private ServidorRemoto servidor;

    /**
     * Constructor que inicializa la interfaz gráfica y conecta con el servidor.
     */
    public ClienteGUI() {
        initComponents();
        conectarServidor();
    }

    // --- Getters de los componentes de la GUI ---

    public JTextField getTextRefugio() { return refugioTextField; }

    public JTextField getTextRiesgo1H() { return riesgo1HTextField; }
    public JTextField getTextRiesgo2H() { return riesgo2HTextField; }
    public JTextField getTextRiesgo3H() { return riesgo3HTextField; }
    public JTextField getTextRiesgo4H() { return riesgo4HTextField; }

    public JTextField getTextRiesgo1Z() { return riesgo1ZTextField; }
    public JTextField getTextRiesgo2Z() { return riesgo2ZTextField; }
    public JTextField getTextRiesgo3Z() { return riesgo3ZTextField; }
    public JTextField getTextRiesgo4Z() { return riesgo4ZTextField; }

    public JTextField getTextTunel1() { return tunel1TextField; }
    public JTextField getTextTunel2() { return tunel2TextField; }
    public JTextField getTextTunel3() { return tunel3TextField; }
    public JTextField getTextTunel4() { return tunel4TextField; }

    public JTextArea getTopZombisTextArea() { return topZombisTextArea; }

    /**
     * Intenta establecer conexión con el servidor RMI y lanza el hilo de actualización si es exitosa.
     */
    private void conectarServidor() {
        try {
            servidor = (ServidorRemoto) Naming.lookup("//localhost/objeto");
            System.out.println("Cliente conectando al servidor");
            boolean pausado = servidor.estaPausado();
            pauseResumeButton.setText(pausado ? "Reanudar" : "Pausar");
            new Thread(new ActualizadorClienteGUI(this, servidor)).start();// Inicia el hilo de actualización
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar al servidor", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza manualmente todos los datos en la interfaz gráfica desde el servidor.
     *
     * @throws RemoteException si hay un fallo al recuperar datos del servidor RMI
     */
    private void actualizarDatos() throws RemoteException {
        // Humanos en refugio
        refugioTextField.setText(String.valueOf(servidor.getHumanosRefugio()));

        // Humanos en túneles
        int[] humanosTuneles = servidor.getHumanosTuneles();
        tunel1TextField.setText(String.valueOf(humanosTuneles[0]));
        tunel2TextField.setText(String.valueOf(humanosTuneles[1]));
        tunel3TextField.setText(String.valueOf(humanosTuneles[2]));
        tunel4TextField.setText(String.valueOf(humanosTuneles[3]));

        // Humanos en zonas de riesgo
        int[] humanosRiesgo = servidor.getHumanosZonasRiesgo();
        riesgo1HTextField.setText(String.valueOf(humanosRiesgo[0]));
        riesgo2HTextField.setText(String.valueOf(humanosRiesgo[1]));
        riesgo3HTextField.setText(String.valueOf(humanosRiesgo[2]));
        riesgo4HTextField.setText(String.valueOf(humanosRiesgo[3]));

        // Zombis en zonas de riesgo
        int[] zombisRiesgo = servidor.getZombisZonasRiesgo();
        riesgo1ZTextField.setText(String.valueOf(zombisRiesgo[0]));
        riesgo2ZTextField.setText(String.valueOf(zombisRiesgo[1]));
        riesgo3ZTextField.setText(String.valueOf(zombisRiesgo[2]));
        riesgo4ZTextField.setText(String.valueOf(zombisRiesgo[3]));

        // Top 3 zombis letales
        Map<String, Integer> topZombis = servidor.getTopZombisLetales();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : topZombis.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        topZombisTextArea.setText(sb.toString());
    }

    /**
     * Devuelve el botón de pausa/reanudación.
     *
     * @return
     */
    public JButton getPauseResumeButton() {
        return pauseResumeButton;
    }

    /**
     * Método principal que inicia la aplicación cliente.
     *
     * @param args
     * @throws RemoteException
     */
    public static void main (String[] args) throws RemoteException{
        java.awt.EventQueue.invokeLater(() -> {
            new ClienteGUI().setVisible(true);
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        refugioTextField = new javax.swing.JTextField();
        tunel1TextField = new javax.swing.JTextField();
        riesgo1HTextField = new javax.swing.JTextField();
        riesgo1ZTextField = new javax.swing.JTextField();
        riesgo2ZTextField = new javax.swing.JTextField();
        riesgo3ZTextField = new javax.swing.JTextField();
        riesgo4ZTextField = new javax.swing.JTextField();
        riesgo4HTextField = new javax.swing.JTextField();
        riesgo3HTextField = new javax.swing.JTextField();
        riesgo2HTextField = new javax.swing.JTextField();
        tunel4TextField = new javax.swing.JTextField();
        tunel3TextField = new javax.swing.JTextField();
        tunel2TextField = new javax.swing.JTextField();
        pauseResumeButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        topZombisTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(java.awt.Color.white);
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Humanos en el refugio");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 32, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Humanos en los túneles");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 84, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Zombis en las zonas de riesgo");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 188, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Humanos en las zonas de riesgo");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 136, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Zombis mas letales");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 242, -1, -1));

        refugioTextField.setEditable(false);
        refugioTextField.setForeground(new java.awt.Color(0, 0, 0));
        refugioTextField.setText("jTextField1");
        refugioTextField.setMaximumSize(new java.awt.Dimension(74, 26));
        refugioTextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(refugioTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 29, 75, -1));

        tunel1TextField.setEditable(false);
        tunel1TextField.setForeground(new java.awt.Color(0, 0, 0));
        tunel1TextField.setText("jTextField2");
        tunel1TextField.setMaximumSize(new java.awt.Dimension(74, 26));
        tunel1TextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(tunel1TextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 81, 75, -1));

        riesgo1HTextField.setEditable(false);
        riesgo1HTextField.setForeground(new java.awt.Color(0, 0, 0));
        riesgo1HTextField.setText("jTextField3");
        riesgo1HTextField.setMaximumSize(new java.awt.Dimension(74, 26));
        riesgo1HTextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(riesgo1HTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 133, 75, -1));

        riesgo1ZTextField.setEditable(false);
        riesgo1ZTextField.setForeground(new java.awt.Color(0, 0, 0));
        riesgo1ZTextField.setText("jTextField4");
        riesgo1ZTextField.setMaximumSize(new java.awt.Dimension(74, 26));
        riesgo1ZTextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(riesgo1ZTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 185, 75, -1));

        riesgo2ZTextField.setEditable(false);
        riesgo2ZTextField.setForeground(new java.awt.Color(0, 0, 0));
        riesgo2ZTextField.setText("jTextField5");
        riesgo2ZTextField.setMaximumSize(new java.awt.Dimension(74, 26));
        riesgo2ZTextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(riesgo2ZTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 185, 75, -1));

        riesgo3ZTextField.setEditable(false);
        riesgo3ZTextField.setForeground(new java.awt.Color(0, 0, 0));
        riesgo3ZTextField.setText("jTextField6");
        riesgo3ZTextField.setMaximumSize(new java.awt.Dimension(74, 26));
        riesgo3ZTextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(riesgo3ZTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(483, 185, 75, -1));

        riesgo4ZTextField.setEditable(false);
        riesgo4ZTextField.setForeground(new java.awt.Color(0, 0, 0));
        riesgo4ZTextField.setText("jTextField7");
        riesgo4ZTextField.setMaximumSize(new java.awt.Dimension(74, 26));
        riesgo4ZTextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(riesgo4ZTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(576, 185, 75, -1));

        riesgo4HTextField.setEditable(false);
        riesgo4HTextField.setForeground(new java.awt.Color(0, 0, 0));
        riesgo4HTextField.setText("jTextField7");
        riesgo4HTextField.setMaximumSize(new java.awt.Dimension(74, 26));
        riesgo4HTextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(riesgo4HTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(576, 133, 75, -1));

        riesgo3HTextField.setEditable(false);
        riesgo3HTextField.setForeground(new java.awt.Color(0, 0, 0));
        riesgo3HTextField.setText("jTextField6");
        riesgo3HTextField.setMaximumSize(new java.awt.Dimension(74, 26));
        riesgo3HTextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(riesgo3HTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(483, 133, 75, -1));

        riesgo2HTextField.setEditable(false);
        riesgo2HTextField.setForeground(new java.awt.Color(0, 0, 0));
        riesgo2HTextField.setText("jTextField5");
        riesgo2HTextField.setMaximumSize(new java.awt.Dimension(74, 26));
        riesgo2HTextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(riesgo2HTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 133, 75, -1));

        tunel4TextField.setEditable(false);
        tunel4TextField.setForeground(new java.awt.Color(0, 0, 0));
        tunel4TextField.setText("jTextField7");
        tunel4TextField.setMaximumSize(new java.awt.Dimension(74, 26));
        tunel4TextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(tunel4TextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(576, 81, 75, -1));

        tunel3TextField.setEditable(false);
        tunel3TextField.setForeground(new java.awt.Color(0, 0, 0));
        tunel3TextField.setText("jTextField6");
        tunel3TextField.setMaximumSize(new java.awt.Dimension(74, 26));
        tunel3TextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(tunel3TextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(483, 81, 75, -1));

        tunel2TextField.setEditable(false);
        tunel2TextField.setForeground(new java.awt.Color(0, 0, 0));
        tunel2TextField.setText("jTextField5");
        tunel2TextField.setMaximumSize(new java.awt.Dimension(74, 26));
        tunel2TextField.setMinimumSize(new java.awt.Dimension(74, 26));
        jPanel1.add(tunel2TextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 81, 75, -1));

        pauseResumeButton.setBackground(new java.awt.Color(204, 0, 0));
        pauseResumeButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pauseResumeButton.setForeground(new java.awt.Color(255, 255, 255));
        pauseResumeButton.setText("Pausar");
        pauseResumeButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pauseResumeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseResumeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(pauseResumeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 320, 112, 44));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        topZombisTextArea.setBackground(java.awt.Color.white);
        topZombisTextArea.setColumns(20);
        topZombisTextArea.setForeground(new java.awt.Color(0, 0, 0));
        topZombisTextArea.setRows(5);
        jScrollPane1.setViewportView(topZombisTextArea);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 276, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pauseResumeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseResumeButtonActionPerformed
        try {
            servidor.togglePausa();
            if (pauseResumeButton.getText().equals("Pausar")) {
                pauseResumeButton.setText("Reanudar");
            } else {
                pauseResumeButton.setText("Pausar");
            }
        } catch (RemoteException ex) {
        }
    }//GEN-LAST:event_pauseResumeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton pauseResumeButton;
    private javax.swing.JTextField refugioTextField;
    private javax.swing.JTextField riesgo1HTextField;
    private javax.swing.JTextField riesgo1ZTextField;
    private javax.swing.JTextField riesgo2HTextField;
    private javax.swing.JTextField riesgo2ZTextField;
    private javax.swing.JTextField riesgo3HTextField;
    private javax.swing.JTextField riesgo3ZTextField;
    private javax.swing.JTextField riesgo4HTextField;
    private javax.swing.JTextField riesgo4ZTextField;
    private javax.swing.JTextArea topZombisTextArea;
    private javax.swing.JTextField tunel1TextField;
    private javax.swing.JTextField tunel2TextField;
    private javax.swing.JTextField tunel3TextField;
    private javax.swing.JTextField tunel4TextField;
    // End of variables declaration//GEN-END:variables
}
