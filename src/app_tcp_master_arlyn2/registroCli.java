
package app_tcp_master_arlyn2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class registroCli extends JFrame {

    private JTextField nombreTxt;
    private JButton bConectar;

    public registroCli() {
        initComponents();
    }

    private void initComponents() {

        this.setTitle("Conexión al Servidor");
        this.setSize(400, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);

        
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("cliente.png")); // Asegúrate de que la imagen esté en la misma carpeta que este archivo .java
        Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Redimensionar a 100x100 píxeles
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(270, 40, scaledIcon.getIconWidth(), scaledIcon.getIconHeight()); // Ajusta el tamaño y posición
        this.getContentPane().add(imageLabel);

        JLabel jLabelNombre = new JLabel("Ingrese su nombre:");
        jLabelNombre.setFont(new Font("Verdana", 0, 14));
        jLabelNombre.setBounds(40, 30, 200, 30);
        this.getContentPane().add(jLabelNombre);

        nombreTxt = new JTextField();
        nombreTxt.setFont(new Font("Verdana", 0, 14));
        nombreTxt.setBounds(40, 70, 200, 30);
        this.getContentPane().add(nombreTxt);

        bConectar = new JButton("CONECTAR CON SERVIDOR");
        bConectar.setFont(new Font("Segoe UI", 0, 14));
        bConectar.setBounds(40, 110, 200, 40);
        bConectar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                conectarActionPerformed(evt);
            }
        });
        this.getContentPane().add(bConectar);
    }

    private void conectarActionPerformed(ActionEvent evt) {
        String nombreUsuario = nombreTxt.getText().trim();
        if (nombreUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre.");
            return;
        }
        // Aquí puedes intentar conectar al servidor, si la conexión es exitosa:
        this.setVisible(false);
        new PrincipalCli(nombreUsuario).setVisible(true);
        this.dispose(); // Cierra la ventana de conexión
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new registroCli().setVisible(true);
            }
        });
    }
}

