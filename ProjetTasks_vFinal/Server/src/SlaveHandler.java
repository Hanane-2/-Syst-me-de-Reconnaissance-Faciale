import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SlaveHandler implements Runnable {

    private Socket socket;
    private ConcurrentHashMap<TaskResult, UUID> taskResult;
    private ConcurrentHashMap<Socket, Boolean> unavailableSlaves;
    private ObjectInputStream objectInputStream;
    private ConcurrentHashMap<Socket, ObjectOutputStream> listSlaveConnected;

    public SlaveHandler(Socket socket, ConcurrentHashMap<Socket, Boolean> unavailableSlaves,
                        ConcurrentHashMap<TaskResult, UUID> taskResult,
                        ConcurrentHashMap<Socket, ObjectOutputStream> listSlaveConnected) throws IOException {

        this.unavailableSlaves = unavailableSlaves;
        this.socket = socket;
        this.taskResult = taskResult;
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.listSlaveConnected = listSlaveConnected;
    }

    @Override
    public void run() {
        try {
            TaskResult result;
            while ((result = (TaskResult) objectInputStream.readObject()) != null) {
                handleTaskResult(result);
            }
        } catch (Exception e) {
            handleException(e);
        } finally {
            closeClientConnection();
        }
    }

    private void handleTaskResult(TaskResult result) {
        // Process and handle the received task result
        unavailableSlaves.remove(socket);
        taskResult.put(result, result.getTaskId());
    }

    private void handleException(Exception e) {
        // Handle the exception (e.g., log it)
        System.err.println("Exception in SlaveHandler: " + e.getMessage());
    }

    private void closeClientConnection() {
        try {
            if (listSlaveConnected.containsKey(socket)) {
                listSlaveConnected.get(socket).close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (socket != null) {
                socket.close();
            }

            listSlaveConnected.remove(socket);

            System.out.println("The slave has been disconnected from the server");
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
