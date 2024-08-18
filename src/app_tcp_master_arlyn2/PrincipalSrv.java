package app_tcp_master_arlyn2;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PrincipalSrv extends javax.swing.JFrame {
    private final int PORT = 12345;
    private ServerSocket serverSocket;
    private final Set<PrintWriter> clientWriters = new HashSet<>();
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JProgressBar progressBar;
    private JLabel progressLabel;
    private JLabel userListTitle;

    public PrincipalSrv() {
        initComponents();
    }

    private void initComponents() {
        this.setTitle("Servidor ...");
        this.setSize(510, 500);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);

        bIniciar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        mensajesTxt = new JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        progressBar = new JProgressBar();
        progressLabel = new JLabel();
        userListTitle = new JLabel();

        bIniciar.setFont(new java.awt.Font("Segoe UI", 0, 18));
        bIniciar.setText("INICIAR SERVIDOR");
        bIniciar.addActionListener(evt -> {
            bIniciarActionPerformed(evt);
            bIniciar.setEnabled(false);
        });
        getContentPane().add(bIniciar);
        bIniciar.setBounds(100, 150, 250, 40);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel1.setForeground(new java.awt.Color(204, 0, 0));
        jLabel1.setText("SERVIDOR TCP");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(170, 40, 160, 17);

        mensajesTxt.setColumns(25);
        mensajesTxt.setRows(5);
        jScrollPane1.setViewportView(mensajesTxt);
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(50, 200, 350, 70);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setBounds(50, 300, 350, 100);

        userListTitle.setText("Usuarios Conectados:");
        userListTitle.setFont(new java.awt.Font("Verdana", 1, 14));
        userListTitle.setBounds(50, 270, 200, 25);

        getContentPane().add(userListTitle);
        getContentPane().add(userScrollPane);

        // Barra de proceso de carga
        progressBar.setBounds(50, 80, 300, 25);
        progressBar.setIndeterminate(true);
        progressLabel.setBounds(220, 120, 200, 25);
        progressLabel.setFont(new java.awt.Font("Verdana", 0, 14));

        getContentPane().add(progressBar);
        getContentPane().add(progressLabel);

        // Inicialmente oculta el progreso
        progressBar.setVisible(false);
        progressLabel.setVisible(false);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new PrincipalSrv().setVisible(true));
    }

    private void bIniciarActionPerformed(java.awt.event.ActionEvent evt) {
        iniciarServidor();
    }

    private void iniciarServidor() {
        // Muestra la barra de progreso
        progressBar.setVisible(true);
        progressLabel.setVisible(true);

        new Thread(() -> {
            try {
                InetAddress addr = InetAddress.getLocalHost();
                serverSocket = new ServerSocket(PORT);
                mensajesTxt.append("Servidor TCP en ejecución: " + addr + " ,Puerto " + serverSocket.getLocalPort() + "\n");

                // Simulación de progreso (puedes ajustar el tiempo de espera)
                for (int i = 0; i <= 100; i += 10) {
                    final int progress = i;
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(progress);
                        progressLabel.setText("Conectando " + progress + "%");
                    });
                    try {
                        TimeUnit.MILLISECONDS.sleep(200); // Espera para simular el progreso
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                progressBar.setIndeterminate(false);
                progressBar.setVisible(false);
                progressLabel.setVisible(false);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    synchronized (clientWriters) {
                        clientWriters.add(out);
                    }

                    new Thread(new ClienteHilo(clientSocket)).start();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                mensajesTxt.append("Error en el servidor: " + ex.getMessage() + "\n");
            }
        }).start();
    }
    //se crea un socket por cada usuario registrado
    private class ClienteHilo implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private String userName;

        public ClienteHilo(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                userName = in.readLine(); // Obtén el nombre del usuario
                agregarUsuario(userName);

                String linea;
                while ((linea = in.readLine()) != null) {
                    mensajesTxt.append("Cliente " + userName + ": " + linea + "\n");
                    synchronized (clientWriters) {
                        for (PrintWriter writer : clientWriters) {
                            writer.println(userName + ": " + linea);
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                mensajesTxt.append("Error con cliente: " + ex.getMessage() + "\n");
            } finally {
                eliminarUsuario(userName);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
         //se agregan los usuarios que se conectan con el servidor 
        private void agregarUsuario(String userName) {
            SwingUtilities.invokeLater(() -> {
                userListModel.addElement(userName);
            });
        }
        //se eliminan los usuarios que se desconectan con el servidor
        private void eliminarUsuario(String userName) {
            SwingUtilities.invokeLater(() -> {
                userListModel.removeElement(userName);
            });
        }
    }

    private javax.swing.JButton bIniciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextArea mensajesTxt;
    private javax.swing.JScrollPane jScrollPane1;
}


