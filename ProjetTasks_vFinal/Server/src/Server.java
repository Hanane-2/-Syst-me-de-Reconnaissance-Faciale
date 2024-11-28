import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ServerGUI extends JFrame {

    private ServerClient serverClient;
    private ServerSlave serverSlave;
    private JTextArea statusTextArea;

    public ServerGUI(int port1, int port2) {
        super("Server");

        serverClient = new ServerClient(port1);
        serverSlave = new ServerSlave(port2);

        // Set layout manager
        setLayout(new BorderLayout());

        // Create Swing components
        statusTextArea = new JTextArea(20, 40);
        statusTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(statusTextArea);

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER);

        // Set frame properties
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Start server threads
        new Thread(serverClient).start();
        new Thread(serverSlave).start();

        new WorkerSchedulerServer(
                serverClient.getTaskQueue(),
                serverSlave.getListSlaveConnected(),
                serverSlave.getUnavailableSlaves(),
                serverSlave.getTaskResult(),
                serverClient.getClientMap(),
                this::updateStatus
        ).start();
    }

    // Method to update the status displayed in the JTextArea
    public void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> statusTextArea.append(status + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (args.length != 2) {
                JOptionPane.showMessageDialog(null, "Usage: java ServerGUI <port1> <port2>");
                System.exit(1);
            }

            try {
                int port1 = Integer.parseInt(args[0]);
                int port2 = Integer.parseInt(args[1]);
                new ServerGUI(port1, port2);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid port numbers. Please provide valid integers.");
                System.exit(1);
            }
        });
    }
}
