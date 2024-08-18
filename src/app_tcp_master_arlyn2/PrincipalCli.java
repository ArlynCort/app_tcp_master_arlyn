
package app_tcp_master_arlyn2;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PrincipalCli extends javax.swing.JFrame {

    private final int PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String nombreUsuario;

    private JTextArea mensajesTxt;
    private JTextField mensajeTxt;
    private JLabel jLabel2;
    private JButton btEnviar;

    public PrincipalCli(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        initComponents();
        conectar();
    }

    private void initComponents() {

        this.setTitle("Cliente: " + nombreUsuario);
        this.setSize(380, 470);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);

        JLabel jLabel1 = new JLabel("CLIENTE TCP: " + nombreUsuario);
        jLabel1.setFont(new Font("Tahoma", 1, 14));
        jLabel1.setForeground(new Color(204, 0, 0));
        jLabel1.setBounds(110, 20, 250, 17);
        this.getContentPane().add(jLabel1);

        
        
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("cliente.png")); 
        Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); 
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(130, 40, scaledIcon.getIconWidth(), scaledIcon.getIconHeight()); 
        this.getContentPane().add(imageLabel);

        mensajesTxt = new JTextArea();
        mensajesTxt.setColumns(20);
        mensajesTxt.setRows(5);
        mensajesTxt.setEnabled(false);
        mensajesTxt.setBackground(Color.WHITE);
        mensajesTxt.setForeground(Color.BLACK);
        JScrollPane jScrollPane1 = new JScrollPane(mensajesTxt);
        jScrollPane1.setBounds(30, 250, 300, 160);
        this.getContentPane().add(jScrollPane1);

        mensajeTxt = new JTextField();
        mensajeTxt.setFont(new Font("Verdana", 0, 14));
        mensajeTxt.setEnabled(false);
        mensajeTxt.setBounds(40, 140, 200, 30);
        this.getContentPane().add(mensajeTxt);

        jLabel2 = new JLabel("Mensaje:");
        jLabel2.setFont(new Font("Verdana", 0, 14));
        jLabel2.setEnabled(false);
        jLabel2.setBounds(20, 100, 100, 30);
        this.getContentPane().add(jLabel2);

        btEnviar = new JButton("Enviar");
        btEnviar.setFont(new Font("Verdana", 0, 14));
        btEnviar.setEnabled(false);
        btEnviar.setBounds(40, 180, 120, 27);
        btEnviar.addActionListener(evt -> {
            String mensaje = mensajeTxt.getText();
            if (!mensaje.isEmpty()) {
                out.println( mensaje);
                //mensajesTxt.append( "\n");
                mensajeTxt.setText("");
            }
        });
        this.getContentPane().add(btEnviar);
    }

    private void conectar() {
        try {
            socket = new Socket("localhost", PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
            out.println(nombreUsuario);
    
            new Thread(() -> {
                try {
                    String fromServer;
                    while ((fromServer = in.readLine()) != null) {
                      //se coloca el nombre de quien envia el mensaje
                        if (!fromServer.startsWith(nombreUsuario + ":")) {
                            mensajesTxt.append(fromServer + "\n");
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
    
            mensajeTxt.setEnabled(true);
            jLabel2.setEnabled(true);
            btEnviar.setEnabled(true);
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new PrincipalCli("User").setVisible(true);
        });
    }
}
