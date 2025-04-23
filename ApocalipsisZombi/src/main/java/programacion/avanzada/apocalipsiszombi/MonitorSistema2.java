/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package programacion.avanzada.apocalipsiszombi;

import java.awt.Color;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author adria
 */
public class MonitorSistema2 extends javax.swing.JFrame {
    
    private MapaZonas mapa;
    private Refugio ref;

    /**
     * Creates new form MonitorSistema2
     */
    public MonitorSistema2() {
        initComponents();
    }
    
    public MonitorSistema2(MapaZonas mapa, Refugio ref) {
        initComponents();
        this.mapa = mapa;
        this.ref=ref;
        actualizarTodas();
    }

    public void actualizarTodas() {
        // Actualizar zonas del refugio
        actualizarZonaRefugio(Zona.DESCANSO, scrollDescanso);
        actualizarZonaRefugio(Zona.COMEDOR, scrollComedor);
        actualizarZonaRefugio(Zona.ZONA_COMUN, scrollComun);

        // Actualizar túneles del refugio
        actualizarZonaRefugio(Zona.ESPERA_REFUGIO_1, refugioTunel1);
        actualizarZonaRefugio(Zona.ESPERA_REFUGIO_2, refugioTunel2);
        actualizarZonaRefugio(Zona.ESPERA_REFUGIO_3, refugioTunel3);
        actualizarZonaRefugio(Zona.ESPERA_REFUGIO_4, refugioTunel4);

        // Actualizar túneles de riesgo
        actualizarZonaRefugio(Zona.ESPERA_RIESGO_1, riesgoTunel1);
        actualizarZonaRefugio(Zona.ESPERA_RIESGO_2, riesgoTunel2);
        actualizarZonaRefugio(Zona.ESPERA_RIESGO_3, riesgoTunel3);
        actualizarZonaRefugio(Zona.ESPERA_RIESGO_4, riesgoTunel4);

        // Actualizar zonas de riesgo (humanos y zombis separados)
        actualizarZonaRiesgo(Zona.RIESGO_1, riesgoHumanos1, riesgoZombi1);
        actualizarZonaRiesgo(Zona.RIESGO_2, riesgoHumanos2, riesgoZombi2);
        actualizarZonaRiesgo(Zona.RIESGO_3, riesgoHumanos3, riesgoZombi3);
        actualizarZonaRiesgo(Zona.RIESGO_4, riesgoHumanos4, riesgoZombi4);
        
        // Actualizar el contador de comida del refugio
        actualizarComidaRefugio();
        
        //Actualizar los tuneles
        actualizarTunel(tunel1, Zona.TUNEL_1);
        actualizarTunel(tunel2, Zona.TUNEL_2);
        actualizarTunel(tunel3, Zona.TUNEL_3);
        actualizarTunel(tunel4, Zona.TUNEL_4);
    }
    
    private void actualizarComidaRefugio(){
        contadorComida.setText(String.valueOf(ref.getCantidadComida()));
    }
    
    private void actualizarTunel(javax.swing.JTextField tunelField, Zona tunel){
        List<Humano> humanos = mapa.humanosEnZona(tunel);
        if (humanos.isEmpty()){
            tunelField.setText("");
        }else if (humanos.size()== 1){
            tunelField.setText(humanos.get(0).getIdHumano());
        }
    }

    private void actualizarZonaRefugio(Zona zona, javax.swing.JScrollPane scrollPane) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Título de la zona
        JLabel titleLabel = new JLabel(zona.toString());
        titleLabel.setForeground(Color.BLUE);
        mainPanel.add(titleLabel);

        // Humanos en la zona
        List<Humano> humanos = mapa.humanosEnZona(zona);
        if (!humanos.isEmpty()) {
            for (Humano h : humanos) {
                JLabel itemLabel = new JLabel(h.getIdHumano());
                mainPanel.add(itemLabel);
            }
        } else {
            mainPanel.add(new JLabel("Zona vacía"));
        }

        scrollPane.setViewportView(mainPanel);
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    private void actualizarZonaRiesgo(Zona zona, javax.swing.JScrollPane scrollHumanos, javax.swing.JScrollPane scrollZombis) {
        // Panel para humanos
        JPanel humanPanel = new JPanel();
        humanPanel.setLayout(new BoxLayout(humanPanel, BoxLayout.Y_AXIS));
        humanPanel.setBackground(Color.WHITE);
        humanPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel humanTitle = new JLabel("Humanos");
        humanTitle.setForeground(Color.GREEN);
        humanPanel.add(humanTitle);

        List<Humano> humanos = mapa.humanosEnZona(zona);
        if (!humanos.isEmpty()) {
            for (Humano h : humanos) {
                humanPanel.add(new JLabel(h.getIdHumano()));
            }
        } else {
            humanPanel.add(new JLabel("Zona vacia"));
        }

        // Panel para zombis
        JPanel zombiePanel = new JPanel();
        zombiePanel.setLayout(new BoxLayout(zombiePanel, BoxLayout.Y_AXIS));
        zombiePanel.setBackground(new Color(255, 200, 200)); // Fondo rojo claro
        zombiePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel zombieTitle = new JLabel("Zombis");
        zombieTitle.setForeground(Color.RED);
        zombiePanel.add(zombieTitle);

        List<Zombi> zombis = mapa.zombisEnZona(zona);
        if (!zombis.isEmpty()) {
            for (Zombi z : zombis) {
                zombiePanel.add(new JLabel(z.getIdZombi()));
            }
        } else {
            zombiePanel.add(new JLabel("Zona vacia"));
        }

        scrollHumanos.setViewportView(humanPanel);
        scrollZombis.setViewportView(zombiePanel);

        scrollHumanos.revalidate();
        scrollHumanos.repaint();
        scrollZombis.revalidate();
        scrollZombis.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        scrollDescanso = new javax.swing.JScrollPane();
        scrollComedor = new javax.swing.JScrollPane();
        scrollComun = new javax.swing.JScrollPane();
        refugioTunel1 = new javax.swing.JScrollPane();
        refugioTunel2 = new javax.swing.JScrollPane();
        refugioTunel3 = new javax.swing.JScrollPane();
        riesgoTunel1 = new javax.swing.JScrollPane();
        riesgoHumanos1 = new javax.swing.JScrollPane();
        riesgoTunel2 = new javax.swing.JScrollPane();
        riesgoTunel3 = new javax.swing.JScrollPane();
        riesgoHumanos2 = new javax.swing.JScrollPane();
        riesgoHumanos3 = new javax.swing.JScrollPane();
        riesgoZombi3 = new javax.swing.JScrollPane();
        riesgoZombi2 = new javax.swing.JScrollPane();
        riesgoZombi1 = new javax.swing.JScrollPane();
        refugioTunel4 = new javax.swing.JScrollPane();
        riesgoTunel4 = new javax.swing.JScrollPane();
        riesgoHumanos4 = new javax.swing.JScrollPane();
        riesgoZombi4 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        contadorComida = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tunel1 = new javax.swing.JTextField();
        tunel2 = new javax.swing.JTextField();
        tunel3 = new javax.swing.JTextField();
        tunel4 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Apocalipsis Zombie");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(scrollDescanso, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 240, 100));
        jPanel1.add(scrollComedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 150, 100));
        jPanel1.add(scrollComun, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 460, 240, 100));
        jPanel1.add(refugioTunel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 140, 100, 100));
        jPanel1.add(refugioTunel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 290, 100, 100));
        jPanel1.add(refugioTunel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 450, 100, 100));
        jPanel1.add(riesgoTunel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 140, 120, 100));
        jPanel1.add(riesgoHumanos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 140, 110, 100));
        jPanel1.add(riesgoTunel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 290, 120, 100));
        jPanel1.add(riesgoTunel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 450, 120, 100));
        jPanel1.add(riesgoHumanos2, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 280, 110, 100));
        jPanel1.add(riesgoHumanos3, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 420, 110, 100));
        jPanel1.add(riesgoZombi3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 420, 110, 100));
        jPanel1.add(riesgoZombi2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 280, 110, 100));
        jPanel1.add(riesgoZombi1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 140, 110, 100));
        jPanel1.add(refugioTunel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 610, 100, 100));
        jPanel1.add(riesgoTunel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 610, 120, 100));
        jPanel1.add(riesgoHumanos4, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 560, 110, 100));
        jPanel1.add(riesgoZombi4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 560, 110, 100));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Refugio");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("Túneles");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 40, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("Zona de riesgo");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 40, -1, -1));

        jButton2.setText("Parar");
        jButton2.setActionCommand("pararButton");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1230, 730, -1, -1));

        contadorComida.setText("jTextField1");
        contadorComida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contadorComidaActionPerformed(evt);
            }
        });
        jPanel1.add(contadorComida, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 340, -1, -1));

        jLabel4.setText("Comida");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 310, -1, -1));

        tunel1.setText("jTextField1");
        jPanel1.add(tunel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 180, -1, -1));

        tunel2.setText("jTextField2");
        jPanel1.add(tunel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 330, -1, -1));

        tunel3.setText("jTextField3");
        jPanel1.add(tunel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 490, -1, -1));

        tunel4.setText("jTextField4");
        jPanel1.add(tunel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 650, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void contadorComidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contadorComidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contadorComidaActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MonitorSistema2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MonitorSistema2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MonitorSistema2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MonitorSistema2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MonitorSistema2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField contadorComida;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane refugioTunel1;
    private javax.swing.JScrollPane refugioTunel2;
    private javax.swing.JScrollPane refugioTunel3;
    private javax.swing.JScrollPane refugioTunel4;
    private javax.swing.JScrollPane riesgoHumanos1;
    private javax.swing.JScrollPane riesgoHumanos2;
    private javax.swing.JScrollPane riesgoHumanos3;
    private javax.swing.JScrollPane riesgoHumanos4;
    private javax.swing.JScrollPane riesgoTunel1;
    private javax.swing.JScrollPane riesgoTunel2;
    private javax.swing.JScrollPane riesgoTunel3;
    private javax.swing.JScrollPane riesgoTunel4;
    private javax.swing.JScrollPane riesgoZombi1;
    private javax.swing.JScrollPane riesgoZombi2;
    private javax.swing.JScrollPane riesgoZombi3;
    private javax.swing.JScrollPane riesgoZombi4;
    private javax.swing.JScrollPane scrollComedor;
    private javax.swing.JScrollPane scrollComun;
    private javax.swing.JScrollPane scrollDescanso;
    private javax.swing.JTextField tunel1;
    private javax.swing.JTextField tunel2;
    private javax.swing.JTextField tunel3;
    private javax.swing.JTextField tunel4;
    // End of variables declaration//GEN-END:variables
}
