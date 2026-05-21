package br.edu.ifsuldeminas.sd.chat.view;

import javax.swing.*;
import java.awt.*;

public class ConnectionView extends JFrame {

    private JTextField txtLocalPort;
    private JTextField txtRemotePort;
    private JTextField txtName;
    private JButton btnConnect;

    public ConnectionView() {

        setTitle("Conectar ao Chat");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(
                new Color(30,30,30)
        );

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
    	
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30,30,30));
        panel.setLayout(new GridLayout(4,2,10,10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(15,15,15,15)
        );

        JLabel lblLocal = new JLabel("Porta Local:");
        lblLocal.setForeground(Color.WHITE);
        
        JLabel lblRemote = new JLabel("Porta Remota:");
        lblRemote.setForeground(Color.WHITE);
        
        JLabel lblName = new JLabel("Nome:");
        lblName.setForeground(Color.WHITE);

        txtLocalPort = new JTextField();
        txtRemotePort = new JTextField();
        txtName = new JTextField();
        btnConnect = new JButton("Conectar");
        styleButton(btnConnect);
        
        panel.add(lblLocal);
        panel.add(txtLocalPort);
        panel.add(lblRemote);
        panel.add(txtRemotePort);
        panel.add(lblName);
        panel.add(txtName);
        panel.add(new JLabel());
        panel.add(btnConnect);

        add(panel);

        btnConnect.addActionListener(
                e -> connect()
        );
    }

    private void styleButton(JButton button) {

        button.setBackground(new Color(70,130,180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void connect() {

        try {

            int localPort =
                    Integer.parseInt(
                            txtLocalPort.getText()
                    );

            int remotePort =
                    Integer.parseInt(
                            txtRemotePort.getText()
                    );

            String name =
                    txtName.getText().trim();

            if (name.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Digite um nome."
                );

                return;
            }

            new ChatView(
                    localPort,
                    remotePort,
                    name
            );

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Dados inválidos."
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                ConnectionView::new
        );
    }
}