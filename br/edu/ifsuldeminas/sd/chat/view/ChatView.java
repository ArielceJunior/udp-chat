package br.edu.ifsuldeminas.sd.chat.view;

import br.edu.ifsuldeminas.sd.chat.ChatException;
import br.edu.ifsuldeminas.sd.chat.ChatFactory;
import br.edu.ifsuldeminas.sd.chat.MessageContainer;
import br.edu.ifsuldeminas.sd.chat.Sender;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;

public class ChatView extends JFrame implements MessageContainer {

	private JTextField txtLocalPort;
    private JTextField txtRemotePort;
    private JTextField txtName;

    private JTextPane txtMessages;

    private JTextField txtMessage;

    private JButton btnConnect;
    private JButton btnSend;

    private Sender sender;

    public ChatView() {

        setTitle("UDP Chat");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(new Color(30, 30, 30));

        initializeComponents();
        configureLayout();
        configureEvents();

        setVisible(true);
    }

    private void initializeComponents() {

        Font font = new Font("Arial", Font.PLAIN, 14);

        txtLocalPort = new JTextField(5);
        txtLocalPort.setFont(font);

        txtRemotePort = new JTextField(5);
        txtRemotePort.setFont(font);

        txtName = new JTextField(10);
        txtName.setFont(font);

        txtMessages = new JTextPane();
        txtMessages.setEditable(false);
        txtMessages.setBackground(new Color(40, 40, 40));
        txtMessages.setForeground(Color.WHITE);
        txtMessages.setFont(font);

        txtMessage = new JTextField();
        txtMessage.setFont(font);
        txtMessage.setBackground(new Color(50, 50, 50));
        txtMessage.setForeground(Color.WHITE);
        txtMessage.setCaretColor(Color.WHITE);

        btnConnect = new JButton("Conectar");
        btnSend = new JButton("Enviar");

        styleButton(btnConnect);
        styleButton(btnSend);
    }

    private void styleButton(JButton button) {

        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setFont(new Font("Arial", Font.BOLD, 13));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configureLayout() {

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        topPanel.setBackground(new Color(30, 30, 30));

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JLabel lblLocal = new JLabel("Porta Local:");
        lblLocal.setForeground(Color.WHITE);

        JLabel lblRemote = new JLabel("Porta Remota:");
        lblRemote.setForeground(Color.WHITE);

        JLabel lblName = new JLabel("Nome:");
        lblName.setForeground(Color.WHITE);

        topPanel.add(lblLocal);
        topPanel.add(txtLocalPort);

        topPanel.add(lblRemote);
        topPanel.add(txtRemotePort);

        topPanel.add(lblName);
        topPanel.add(txtName);

        topPanel.add(btnConnect);

        JScrollPane scrollPane = new JScrollPane(txtMessages);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.setBackground(new Color(30, 30, 30));

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        bottomPanel.add(txtMessage, BorderLayout.CENTER);
        bottomPanel.add(btnSend, BorderLayout.EAST);

        setLayout(new BorderLayout());

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void configureEvents() {

        btnConnect.addActionListener(e -> connect());

        btnSend.addActionListener(e -> sendMessage());

        txtMessage.addActionListener(e -> sendMessage());
    }

    private void connect() {

        try {

            int localPort =
                    Integer.parseInt(txtLocalPort.getText());

            int remotePort =
                    Integer.parseInt(txtRemotePort.getText());

            sender = ChatFactory.build(
                    "localhost",
                    remotePort,
                    localPort,
                    this
            );

            appendColoredMessage(
                    "Sistema",
                    "Chat conectado.",
                    Color.ORANGE
            );

            btnConnect.setEnabled(false);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao conectar."
            );
        }
    }

    private void sendMessage() {

        if (sender == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Conecte primeiro."
            );

            return;
        }

        String message =
                txtMessage.getText().trim();

        if (message.isEmpty()) {
            return;
        }

        String from =
                txtName.getText().trim();

        String formattedMessage =
                String.format(
                        "%s%s%s",
                        message,
                        MessageContainer.FROM,
                        from
                );

        try {

            sender.send(formattedMessage);

            appendColoredMessage(
                    "Eu",
                    message,
                    Color.GREEN
            );

            txtMessage.setText("");

        } catch (ChatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao enviar mensagem."
            );
        }
    }

    @Override
    public void newMessage(String message) {

        SwingUtilities.invokeLater(() -> {

            if (message.contains(MessageContainer.FROM)) {

                String[] parts =
                        message.split(MessageContainer.FROM);

                String text = parts[0];

                String from = parts[1];

                appendColoredMessage(
                        from,
                        text,
                        Color.CYAN
                );

            } else {

                appendColoredMessage(
                        "Sistema",
                        message,
                        Color.ORANGE
                );
            }
        });
    }

    private void appendColoredMessage(
            String senderName,
            String message,
            Color senderColor
    ) {

        StyledDocument doc =
                txtMessages.getStyledDocument();

        Style style =
                txtMessages.addStyle(
                        "Style",
                        null
                );

        try {

            StyleConstants.setForeground(
                    style,
                    senderColor
            );

            StyleConstants.setBold(
                    style,
                    true
            );

            doc.insertString(
                    doc.getLength(),
                    senderName + ": ",
                    style
            );

            StyleConstants.setForeground(
                    style,
                    Color.WHITE
            );

            StyleConstants.setBold(
                    style,
                    false
            );

            doc.insertString(
                    doc.getLength(),
                    message + "\n",
                    style
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(ChatView::new);
    }
}