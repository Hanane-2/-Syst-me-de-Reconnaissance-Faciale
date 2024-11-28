import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class ClientGUI extends JFrame {

    private JTextField serverHostField;
    private JTextField portField;

    public ClientGUI() {
        super("Matrix Client");

        // Set layout manager
        setLayout(new FlowLayout());

        // Create Swing components
        JLabel serverHostLabel = new JLabel("Server Host:");
        JLabel portLabel = new JLabel("Port:");

        serverHostField = new JTextField(15);
        portField = new JTextField(5);

        JButton connectButton = new JButton("Connect");

        // Add components to the frame
        add(serverHostLabel);
        add(serverHostField);
        add(portLabel);
        add(portField);
        add(connectButton);

        // Add action listener to the Connect button
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        // Set frame properties
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void connectToServer() {
        String serverHost = serverHostField.getText();
        String portStr = portField.getText();

        if (serverHost.isEmpty() || portStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both server host and port number.");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Port number must be an integer.");
            return;
        }

        Socket socket = null;

        try {
            socket = new Socket(serverHost, port);
            MatrixClientApp matrixClient = new MatrixClientApp(socket);
            matrixClient.MatrixTask();
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error connecting to the server: " + ex.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }
}
