package UI;

import Helpers.PausaGlobal;

public class ApocalipsisGUI extends javax.swing.JFrame{
    private boolean running = true;

    public ApocalipsisGUI() {
        initComponents();
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void pausarOSeguir() {
        running = !running;
        if (running) {
            notifyAll();
        }
    }
    public javax.swing.JTextArea getTextDescanso() { return textDescanso; }
    public javax.swing.JTextArea getTextComedor() { return textComedor; }
    public javax.swing.JTextArea getTextComun() { return textComun; }

    public javax.swing.JTextArea getTextRiesgoHumanos1() { return textRiesgoHumanos1; }
    public javax.swing.JTextArea getTextRiesgoZombis1() { return textRiesgoZombis1; }
    public javax.swing.JTextArea getTextRiesgoHumanos2() { return textRiesgoHumanos2; }
    public javax.swing.JTextArea getTextRiesgoZombis2() { return textRiesgoZombis2; }
    public javax.swing.JTextArea getTextRiesgoHumanos3() { return textRiesgoHumanos3; }
    public javax.swing.JTextArea getTextRiesgoZombis3() { return textRiesgoZombis3; }
    public javax.swing.JTextArea getTextRiesgoHumanos4() { return textRiesgoHumanos4; }
    public javax.swing.JTextArea getTextRiesgoZombis4() { return textRiesgoZombis4; }

    public javax.swing.JTextField getTunel1() { return tunel1; }
    public javax.swing.JTextField getTunel2() { return tunel2; }
    public javax.swing.JTextField getTunel3() { return tunel3; }
    public javax.swing.JTextField getTunel4() { return tunel4; }

    public javax.swing.JTextArea getTextEsperaRefugio1() { return textEsperaRefugio1; }
    public javax.swing.JTextArea getTextEsperaRefugio2() { return textEsperaRefugio2; }
    public javax.swing.JTextArea getTextEsperaRefugio3() { return textEsperaRefugio3; }
    public javax.swing.JTextArea getTextEsperaRefugio4() { return textEsperaRefugio4; }

    public javax.swing.JTextArea getTextEsperaRiesgo1() { return textEsperaRiesgo1; }
    public javax.swing.JTextArea getTextEsperaRiesgo2() { return textEsperaRiesgo2; }
    public javax.swing.JTextArea getTextEsperaRiesgo3() { return textEsperaRiesgo3; }
    public javax.swing.JTextArea getTextEsperaRiesgo4() { return textEsperaRiesgo4; }

    public javax.swing.JTextField getContadorComida() { return contadorComida; }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        scrollDescanso = new javax.swing.JScrollPane();
        textDescanso = new javax.swing.JTextArea();
        scrollComedor = new javax.swing.JScrollPane();
        textComedor = new javax.swing.JTextArea();
        scrollComun = new javax.swing.JScrollPane();
        textComun = new javax.swing.JTextArea();
        refugioTunel1 = new javax.swing.JScrollPane();
        textEsperaRefugio1 = new javax.swing.JTextArea();
        refugioTunel2 = new javax.swing.JScrollPane();
        textEsperaRefugio2 = new javax.swing.JTextArea();
        refugioTunel3 = new javax.swing.JScrollPane();
        textEsperaRefugio3 = new javax.swing.JTextArea();
        riesgoTunel1 = new javax.swing.JScrollPane();
        textEsperaRiesgo1 = new javax.swing.JTextArea();
        riesgoHumanos1 = new javax.swing.JScrollPane();
        textRiesgoHumanos1 = new javax.swing.JTextArea();
        riesgoTunel2 = new javax.swing.JScrollPane();
        textEsperaRiesgo2 = new javax.swing.JTextArea();
        riesgoTunel3 = new javax.swing.JScrollPane();
        textEsperaRiesgo3 = new javax.swing.JTextArea();
        riesgoHumanos2 = new javax.swing.JScrollPane();
        textRiesgoHumanos2 = new javax.swing.JTextArea();
        riesgoHumanos3 = new javax.swing.JScrollPane();
        textRiesgoHumanos3 = new javax.swing.JTextArea();
        riesgoZombi3 = new javax.swing.JScrollPane();
        textRiesgoZombis3 = new javax.swing.JTextArea();
        riesgoZombi2 = new javax.swing.JScrollPane();
        textRiesgoZombis2 = new javax.swing.JTextArea();
        riesgoZombi1 = new javax.swing.JScrollPane();
        textRiesgoZombis1 = new javax.swing.JTextArea();
        refugioTunel4 = new javax.swing.JScrollPane();
        textEsperaRefugio4 = new javax.swing.JTextArea();
        riesgoTunel4 = new javax.swing.JScrollPane();
        textEsperaRiesgo4 = new javax.swing.JTextArea();
        riesgoHumanos4 = new javax.swing.JScrollPane();
        textRiesgoHumanos4 = new javax.swing.JTextArea();
        riesgoZombi4 = new javax.swing.JScrollPane();
        textRiesgoZombis4 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        botonParar = new javax.swing.JButton();
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

        scrollDescanso.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDescanso.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollDescanso.setFocusable(false);

        textDescanso.setEditable(false);
        textDescanso.setColumns(20);
        textDescanso.setLineWrap(true);
        textDescanso.setRows(5);
        textDescanso.setWrapStyleWord(true);
        textDescanso.setFocusable(false);
        scrollDescanso.setViewportView(textDescanso);

        jPanel1.add(scrollDescanso, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 240, 100));

        scrollComedor.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollComedor.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollComedor.setFocusable(false);

        textComedor.setEditable(false);
        textComedor.setColumns(20);
        textComedor.setLineWrap(true);
        textComedor.setRows(5);
        textComedor.setWrapStyleWord(true);
        textComedor.setFocusable(false);
        scrollComedor.setViewportView(textComedor);

        jPanel1.add(scrollComedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 150, 100));

        scrollComun.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollComun.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollComun.setFocusable(false);

        textComun.setEditable(false);
        textComun.setColumns(20);
        textComun.setLineWrap(true);
        textComun.setRows(5);
        textComun.setWrapStyleWord(true);
        textComun.setFocusable(false);
        scrollComun.setViewportView(textComun);

        jPanel1.add(scrollComun, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 460, 240, 100));

        refugioTunel1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        refugioTunel1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        refugioTunel1.setFocusable(false);

        textEsperaRefugio1.setEditable(false);
        textEsperaRefugio1.setColumns(20);
        textEsperaRefugio1.setLineWrap(true);
        textEsperaRefugio1.setRows(5);
        textEsperaRefugio1.setWrapStyleWord(true);
        textEsperaRefugio1.setFocusable(false);
        refugioTunel1.setViewportView(textEsperaRefugio1);

        jPanel1.add(refugioTunel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 140, 140, 100));

        refugioTunel2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        refugioTunel2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        refugioTunel2.setFocusable(false);

        textEsperaRefugio2.setEditable(false);
        textEsperaRefugio2.setColumns(20);
        textEsperaRefugio2.setLineWrap(true);
        textEsperaRefugio2.setRows(5);
        textEsperaRefugio2.setWrapStyleWord(true);
        textEsperaRefugio2.setFocusable(false);
        refugioTunel2.setViewportView(textEsperaRefugio2);

        jPanel1.add(refugioTunel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 280, 140, 100));

        refugioTunel3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        refugioTunel3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        refugioTunel3.setFocusable(false);

        textEsperaRefugio3.setEditable(false);
        textEsperaRefugio3.setColumns(20);
        textEsperaRefugio3.setLineWrap(true);
        textEsperaRefugio3.setRows(5);
        textEsperaRefugio3.setWrapStyleWord(true);
        textEsperaRefugio3.setFocusable(false);
        refugioTunel3.setViewportView(textEsperaRefugio3);

        jPanel1.add(refugioTunel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 420, 140, 100));

        riesgoTunel1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoTunel1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoTunel1.setFocusable(false);

        textEsperaRiesgo1.setEditable(false);
        textEsperaRiesgo1.setColumns(20);
        textEsperaRiesgo1.setLineWrap(true);
        textEsperaRiesgo1.setRows(5);
        textEsperaRiesgo1.setWrapStyleWord(true);
        textEsperaRiesgo1.setFocusable(false);
        riesgoTunel1.setViewportView(textEsperaRiesgo1);

        jPanel1.add(riesgoTunel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 140, 140, 100));

        riesgoHumanos1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoHumanos1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoHumanos1.setFocusable(false);

        textRiesgoHumanos1.setEditable(false);
        textRiesgoHumanos1.setColumns(20);
        textRiesgoHumanos1.setLineWrap(true);
        textRiesgoHumanos1.setRows(5);
        textRiesgoHumanos1.setWrapStyleWord(true);
        textRiesgoHumanos1.setFocusable(false);
        riesgoHumanos1.setViewportView(textRiesgoHumanos1);

        jPanel1.add(riesgoHumanos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 140, 110, 100));

        riesgoTunel2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoTunel2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoTunel2.setFocusable(false);

        textEsperaRiesgo2.setEditable(false);
        textEsperaRiesgo2.setColumns(20);
        textEsperaRiesgo2.setLineWrap(true);
        textEsperaRiesgo2.setRows(5);
        textEsperaRiesgo2.setWrapStyleWord(true);
        textEsperaRiesgo2.setFocusable(false);
        riesgoTunel2.setViewportView(textEsperaRiesgo2);

        jPanel1.add(riesgoTunel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 280, 140, 100));

        riesgoTunel3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoTunel3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoTunel3.setFocusable(false);

        textEsperaRiesgo3.setEditable(false);
        textEsperaRiesgo3.setColumns(20);
        textEsperaRiesgo3.setLineWrap(true);
        textEsperaRiesgo3.setRows(5);
        textEsperaRiesgo3.setWrapStyleWord(true);
        textEsperaRiesgo3.setFocusable(false);
        riesgoTunel3.setViewportView(textEsperaRiesgo3);

        jPanel1.add(riesgoTunel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 420, 140, 100));

        riesgoHumanos2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoHumanos2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoHumanos2.setFocusable(false);

        textRiesgoHumanos2.setEditable(false);
        textRiesgoHumanos2.setColumns(20);
        textRiesgoHumanos2.setLineWrap(true);
        textRiesgoHumanos2.setRows(5);
        textRiesgoHumanos2.setWrapStyleWord(true);
        textRiesgoHumanos2.setFocusable(false);
        riesgoHumanos2.setViewportView(textRiesgoHumanos2);

        jPanel1.add(riesgoHumanos2, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 280, 110, 100));

        riesgoHumanos3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoHumanos3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoHumanos3.setFocusable(false);

        textRiesgoHumanos3.setEditable(false);
        textRiesgoHumanos3.setColumns(20);
        textRiesgoHumanos3.setLineWrap(true);
        textRiesgoHumanos3.setRows(5);
        textRiesgoHumanos3.setWrapStyleWord(true);
        textRiesgoHumanos3.setFocusable(false);
        riesgoHumanos3.setViewportView(textRiesgoHumanos3);

        jPanel1.add(riesgoHumanos3, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 420, 110, 100));

        riesgoZombi3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoZombi3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoZombi3.setFocusable(false);

        textRiesgoZombis3.setEditable(false);
        textRiesgoZombis3.setColumns(20);
        textRiesgoZombis3.setLineWrap(true);
        textRiesgoZombis3.setRows(5);
        textRiesgoZombis3.setWrapStyleWord(true);
        textRiesgoZombis3.setFocusable(false);
        riesgoZombi3.setViewportView(textRiesgoZombis3);

        jPanel1.add(riesgoZombi3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 420, 110, 100));

        riesgoZombi2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoZombi2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoZombi2.setFocusable(false);

        textRiesgoZombis2.setEditable(false);
        textRiesgoZombis2.setColumns(20);
        textRiesgoZombis2.setLineWrap(true);
        textRiesgoZombis2.setRows(5);
        textRiesgoZombis2.setWrapStyleWord(true);
        textRiesgoZombis2.setFocusable(false);
        riesgoZombi2.setViewportView(textRiesgoZombis2);

        jPanel1.add(riesgoZombi2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 280, 110, 100));

        riesgoZombi1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoZombi1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoZombi1.setFocusable(false);

        textRiesgoZombis1.setEditable(false);
        textRiesgoZombis1.setColumns(20);
        textRiesgoZombis1.setLineWrap(true);
        textRiesgoZombis1.setRows(5);
        textRiesgoZombis1.setWrapStyleWord(true);
        textRiesgoZombis1.setFocusable(false);
        riesgoZombi1.setViewportView(textRiesgoZombis1);

        jPanel1.add(riesgoZombi1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 140, 110, 100));

        refugioTunel4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        refugioTunel4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        refugioTunel4.setFocusable(false);

        textEsperaRefugio4.setEditable(false);
        textEsperaRefugio4.setColumns(20);
        textEsperaRefugio4.setLineWrap(true);
        textEsperaRefugio4.setRows(5);
        textEsperaRefugio4.setWrapStyleWord(true);
        textEsperaRefugio4.setFocusable(false);
        refugioTunel4.setViewportView(textEsperaRefugio4);

        jPanel1.add(refugioTunel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 560, 140, 100));

        riesgoTunel4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoTunel4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoTunel4.setFocusable(false);

        textEsperaRiesgo4.setEditable(false);
        textEsperaRiesgo4.setColumns(20);
        textEsperaRiesgo4.setLineWrap(true);
        textEsperaRiesgo4.setRows(5);
        textEsperaRiesgo4.setWrapStyleWord(true);
        textEsperaRiesgo4.setFocusable(false);
        riesgoTunel4.setViewportView(textEsperaRiesgo4);

        jPanel1.add(riesgoTunel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 560, 140, 100));

        riesgoHumanos4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoHumanos4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoHumanos4.setFocusable(false);

        textRiesgoHumanos4.setEditable(false);
        textRiesgoHumanos4.setColumns(20);
        textRiesgoHumanos4.setLineWrap(true);
        textRiesgoHumanos4.setRows(5);
        textRiesgoHumanos4.setWrapStyleWord(true);
        textRiesgoHumanos4.setFocusable(false);
        riesgoHumanos4.setViewportView(textRiesgoHumanos4);

        jPanel1.add(riesgoHumanos4, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 560, 110, 100));

        riesgoZombi4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        riesgoZombi4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        riesgoZombi4.setFocusable(false);

        textRiesgoZombis4.setEditable(false);
        textRiesgoZombis4.setColumns(20);
        textRiesgoZombis4.setLineWrap(true);
        textRiesgoZombis4.setRows(5);
        textRiesgoZombis4.setWrapStyleWord(true);
        textRiesgoZombis4.setFocusable(false);
        riesgoZombi4.setViewportView(textRiesgoZombis4);

        jPanel1.add(riesgoZombi4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 560, 110, 100));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Refugio");
        jLabel1.setFocusable(false);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("Túneles");
        jLabel2.setFocusable(false);
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 40, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("Zona de riesgo");
        jLabel3.setFocusable(false);
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 40, -1, -1));

        botonParar.setText("Parar");
        botonParar.setActionCommand("pararButton");
        botonParar.setFocusable(false);
        botonParar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPararActionPerformed(evt);
            }
        });
        jPanel1.add(botonParar, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 50, -1, -1));

        contadorComida.setEditable(false);
        contadorComida.setText("0   ");
        contadorComida.setFocusable(false);
        contadorComida.setMaximumSize(null);
        contadorComida.setName(""); // NOI18N
        contadorComida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contadorComidaActionPerformed(evt);
            }
        });
        jPanel1.add(contadorComida, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 340, -1, -1));

        jLabel4.setText("Comida");
        jLabel4.setFocusable(false);
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 310, -1, -1));

        tunel1.setEditable(false);
        tunel1.setText("                  ");
        tunel1.setToolTipText("");
        tunel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tunel1.setFocusable(false);
        tunel1.setMaximumSize(new java.awt.Dimension(71, 22));
        tunel1.setMinimumSize(new java.awt.Dimension(71, 22));
        tunel1.setPreferredSize(new java.awt.Dimension(71, 22));
        jPanel1.add(tunel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 180, -1, -1));

        tunel2.setEditable(false);
        tunel2.setText("                     ");
        tunel2.setToolTipText("");
        tunel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tunel2.setFocusable(false);
        tunel2.setMaximumSize(null);
        tunel2.setPreferredSize(new java.awt.Dimension(71, 22));
        jPanel1.add(tunel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 320, -1, -1));

        tunel3.setEditable(false);
        tunel3.setText("                     ");
        tunel3.setToolTipText("");
        tunel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tunel3.setFocusable(false);
        tunel3.setMaximumSize(null);
        tunel3.setPreferredSize(new java.awt.Dimension(71, 22));
        jPanel1.add(tunel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 460, -1, -1));

        tunel4.setEditable(false);
        tunel4.setText("                      ");
        tunel4.setToolTipText("");
        tunel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tunel4.setFocusable(false);
        tunel4.setMaximumSize(null);
        tunel4.setPreferredSize(new java.awt.Dimension(71, 22));
        jPanel1.add(tunel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 600, -1, -1));

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

    private void botonPararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPararActionPerformed
        pausarOSeguir();
        if (running) {
            botonParar.setText("Parar");
            PausaGlobal.getInstance().reanudar();
        } else {
            botonParar.setText("Reanudar");
            PausaGlobal.getInstance().pausar();
        }
    }//GEN-LAST:event_botonPararActionPerformed

    /**
     * @param args the command line arguments
     */
    /*
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ApocalipsisGUI().setVisible(true);
            }
        }); 
   }
    */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonParar;
    private javax.swing.JTextField contadorComida;
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
    private javax.swing.JTextArea textComedor;
    private javax.swing.JTextArea textComun;
    private javax.swing.JTextArea textDescanso;
    private javax.swing.JTextArea textEsperaRefugio1;
    private javax.swing.JTextArea textEsperaRefugio2;
    private javax.swing.JTextArea textEsperaRefugio3;
    private javax.swing.JTextArea textEsperaRefugio4;
    private javax.swing.JTextArea textEsperaRiesgo1;
    private javax.swing.JTextArea textEsperaRiesgo2;
    private javax.swing.JTextArea textEsperaRiesgo3;
    private javax.swing.JTextArea textEsperaRiesgo4;
    private javax.swing.JTextArea textRiesgoHumanos1;
    private javax.swing.JTextArea textRiesgoHumanos2;
    private javax.swing.JTextArea textRiesgoHumanos3;
    private javax.swing.JTextArea textRiesgoHumanos4;
    private javax.swing.JTextArea textRiesgoZombis1;
    private javax.swing.JTextArea textRiesgoZombis2;
    private javax.swing.JTextArea textRiesgoZombis3;
    private javax.swing.JTextArea textRiesgoZombis4;
    private javax.swing.JTextField tunel1;
    private javax.swing.JTextField tunel2;
    private javax.swing.JTextField tunel3;
    private javax.swing.JTextField tunel4;
    // End of variables declaration//GEN-END:variables
}