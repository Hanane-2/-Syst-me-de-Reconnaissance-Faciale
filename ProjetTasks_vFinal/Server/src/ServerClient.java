import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerClientGUI extends JFrame {

    private ServerClient serverClient;
    private JTextArea logTextArea;

    public ServerClientGUI(ServerClient serverClient) {
        this.serverClient = serverClient;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Server Client GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton startButton = new JButton("Start Server");
        JButton stopButton = new JButton("Stop Server");
        logTextArea = new JTextArea();

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(logTextArea), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void startServer() {
        // Add logic to start the server
        logTextArea.append("Server started on port " + serverClient.getPort() + "\n");
        // Update the GUI accordingly
    }

    private void stopServer() {
        // Add logic to stop the server
        logTextArea.append("Server stopped\n");
        // Update the GUI accordingly
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ServerClient serverClient = new ServerClient(12345);
                ServerClientGUI serverClientGUI = new ServerClientGUI(serverClient);
                serverClientGUI.setVisible(true);
            }
        });
    }
}
