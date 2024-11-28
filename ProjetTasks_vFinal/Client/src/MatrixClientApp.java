import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class MatrixClientAppGUI {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream objectInputStream;
    private int[][] matrixA;
    private int[][] matrixB;
    private JTextField rowsFieldA, colsFieldA, rowsFieldB, colsFieldB;
    private JTextArea resultArea;
    private JComboBox<String> operationComboBox;

    public MatrixClientAppGUI(Socket socket) throws IOException {
        this.socket = socket;
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.matrixA = null;
        this.matrixB = null;

        JFrame frame = new JFrame("Matrix Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel matrixPanelA = createMatrixPanel("Matrix A", "Rows A:", "Cols A:");
        JPanel matrixPanelB = createMatrixPanel("Matrix B", "Rows B:", "Cols B:");

        operationComboBox = new JComboBox<>(new String[]{"Addition", "Subtraction", "Multiplication"});
        JButton calculateButton = new JButton("Calculate");
        resultArea = new JTextArea(10, 30);

        mainPanel.add(matrixPanelA);
        mainPanel.add(matrixPanelB);
        mainPanel.add(new JLabel("Select Operation:"));
        mainPanel.add(operationComboBox);
        mainPanel.add(calculateButton);
        mainPanel.add(resultArea);

        calculateButton.addActionListener(e -> {
            try {
                handleCalculateButton();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        });

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel createMatrixPanel(String label, String rowsLabel, String colsLabel) {
        JPanel matrixPanel = new JPanel();
        matrixPanel.setLayout(new BoxLayout(matrixPanel, BoxLayout.Y_AXIS));

        matrixPanel.add(new JLabel(label));

        matrixPanel.add(new JLabel(rowsLabel));
        rowsFieldA = new JTextField();
        matrixPanel.add(rowsFieldA);

        matrixPanel.add(new JLabel(colsLabel));
        colsFieldA = new JTextField();
        matrixPanel.add(colsFieldA);

        return matrixPanel;
    }

    private void handleCalculateButton() throws IOException, ClassNotFoundException {
        // Validate input fields
        if (!validateInput()) {
            JOptionPane.showMessageDialog(null, "Please enter valid dimensions for matrices.");
            return;
        }

        int rowsA = Integer.parseInt(rowsFieldA.getText());
        int colsA = Integer.parseInt(colsFieldA.getText());

        int rowsB = Integer.parseInt(rowsFieldB.getText());
        int colsB = Integer.parseInt(colsFieldB.getText());

        matrixA = createRandomMatrix(rowsA, colsA);
        matrixB = createRandomMatrix(rowsB, colsB);

        OperationType operation = getOperationType();
        createAndSendTask(matrixA, matrixB, operation);
    }

    private boolean validateInput() {
        return isNumeric(rowsFieldA.getText()) && isNumeric(colsFieldA.getText()) &&
                isNumeric(rowsFieldB.getText()) && isNumeric(colsFieldB.getText());
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private OperationType getOperationType() {
        String selectedOperation = (String) operationComboBox.getSelectedItem();
        switch (selectedOperation) {
            case "Addition":
                return OperationType.ADD;
            case "Subtraction":
                return OperationType.SUBTRACT;
            case "Multiplication":
                return OperationType.MULTIPLY;
            default:
                throw new IllegalArgumentException("Invalid operation type");
        }
    }

    private void createAndSendTask(int[][] matrixA, int[][] matrixB, OperationType operation)
            throws IOException, ClassNotFoundException {
        Task task = new Task(matrixA, matrixB, operation);
        oos.writeObject(task);

        listenForMessage(task.getOperation());
    }

    private void listenForMessage(OperationType operationType) throws ClassNotFoundException {
        try {
            int[][] matrixResult;
            if ((matrixResult = (int[][]) objectInputStream.readObject()) != null) {
                displayResult(operationType, matrixResult);
            }
        } catch (IOException e) {
            System.out.println("Server is Closed!");
            close();
        }
    }

    private void displayResult(OperationType operationType, int[][] matrixResult) {
        resultArea.append("\nYour Task is successfully processed.\n");

        displayMatrix("A", matrixA);
        displayMatrix("B", matrixB);

        resultArea.append("\n---- The result of " + operationType.getOperationName() + " ----\n");
        for (int[] row : matrixResult) {
            resultArea.append("[");
            for (int element : row) {
                String formattedElement = String.format("%3d", element);
                resultArea.append(formattedElement + " ");
            }
            resultArea.append("]\n");
        }
        resultArea.append("\n");
    }

    private void displayMatrix(String label, int[][] matrix) {
        resultArea.append("\n---- Matrice " + label + " ----\n");
        for (int[] row : matrix) {
            resultArea.append("[ ");
            for (int element : row) {
                resultArea.append(element + " ");
            }
            resultArea.append("]\n");
        }
    }

    private int[][] createRandomMatrix(int rows, int columns) {
        Random random = new Random();
        int[][] matrix = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }
        return matrix;
    }

    public void close() {
        try {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (oos != null) {
                oos.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MatrixClientAppGUI(new Socket("localhost", 12345));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
