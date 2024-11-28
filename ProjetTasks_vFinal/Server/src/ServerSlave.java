import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.UUID;

public class ServerSlaveGUI extends JFrame {

    private ServerSlave serverSlave;
    private JTextArea logTextArea;

    public ServerSlaveGUI(ServerSlave serverSlave) {
        this.serverSlave = serverSlave;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Server Slave GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton stopButton = new JButton("Stop Slave Server");
        logTextArea = new JTextArea();

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopSlaveServer();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(stopButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(logTextArea), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void stopSlaveServer() {
        // Add logic to stop the slave server
        logTextArea.append("Slave Server stopped\n");
        // Update the GUI accordingly
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ServerSlave serverSlave = new ServerSlave(6789);
                ServerSlaveGUI serverSlaveGUI = new ServerSlaveGUI(serverSlave);
                serverSlaveGUI.setVisible(true);

                new Thread(serverSlave).start();
            }
        });
    }
}
