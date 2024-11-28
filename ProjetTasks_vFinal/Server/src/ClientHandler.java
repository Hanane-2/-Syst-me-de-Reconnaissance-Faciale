import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JOptionPane;

public class ClientHandler implements Runnable {

    private Socket socket;
    private String clientId;
    private ObjectInputStream objectInputStream;
    private ConcurrentHashMap<String, ObjectOutputStream> clientMap;
    private TaskQueue taskQueue;

    public ClientHandler(Socket socket, String clientId, ConcurrentHashMap<String, ObjectOutputStream> clientMap,
                         TaskQueue taskQueue) throws IOException {
        this.socket = socket;
        this.clientId = clientId;

        // Then create ObjectInputStream
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.clientMap = clientMap;

        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        try {
            Task task;
            // Read the task from the client
            while ((task = (Task) objectInputStream.readObject()) != null) {
                // Add the task to the task stack
                ClientTask clientTask = new ClientTask(task, clientId);
                taskQueue.enqueueTask(clientTask);
                showInfoMessage("Task added to the queue with client reference.");
            }

            closeClientConnection(clientId);
        } catch (IOException | ClassNotFoundException e) {
            showErrorDialog("Error handling client task: " + e.getMessage());
            closeClientConnection(clientId);
        }
    }

    private void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void closeClientConnection(String clientId) {
        try {
            if (clientMap.get(clientId) != null) {
                clientMap.get(clientId).close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
            clientMap.remove(clientId);
            showInfoMessage("The Client " + clientId + " is disconnected from the server");
        } catch (IOException e) {
            showErrorDialog("Error closing resources: " + e.getMessage());
        }
    }
}
