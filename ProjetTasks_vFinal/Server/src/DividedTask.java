
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DividedTask implements Serializable {
    private String clientId; // The identifier for the client
    private final UUID taskId; // This is the unique ID for each Task instance
    private OperationType operation;
    private List<Integer> taskIndex;
    private List<List<Integer>> listRowsA;
    private List<List<Integer>> listRowsOrColumnsB;
    private List<List<Integer>> taskResult;

    public DividedTask(UUID taskId, String clientId, OperationType opeartion, List<Integer> taskIndex,
            List<List<Integer>> listRowsA, List<List<Integer>> listRowsOrColumnsB) {
        this.clientId = clientId;
        this.taskId = taskId;
        this.operation = opeartion;
        this.taskIndex = taskIndex;
        this.listRowsA = listRowsA;
        this.listRowsOrColumnsB = listRowsOrColumnsB;
        this.taskResult = new ArrayList<>();
    }

    public void execute() {
        switch (operation) {
            case ADD:
                this.taskResult = AddOperation(listRowsA, listRowsOrColumnsB);
                break;

            case SUBTRACT:
                this.taskResult = SubtractOperation(listRowsA, listRowsOrColumnsB);
                break;

            case MULTIPLY:
                this.taskResult = MultiplyOperation(listRowsA, listRowsOrColumnsB);
                break;
        }
    }

    public List<List<Integer>> AddOperation(List<List<Integer>> listRowsA, List<List<Integer>> listRowsB) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < listRowsA.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < listRowsA.get(i).size(); j++) {
                row.add(listRowsA.get(i).get(j) + listRowsB.get(i).get(j));
            }
            result.add(row);
        }
        return result;
    }

    public List<List<Integer>> SubtractOperation(List<List<Integer>> listRowsA, List<List<Integer>> listRowsB) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < listRowsA.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < listRowsA.get(i).size(); j++) {
                row.add(listRowsA.get(i).get(j) - listRowsB.get(i).get(j));
            }
            result.add(row);
        }
        return result;
    }

    public List<List<Integer>> MultiplyOperation(List<List<Integer>> listRowsA, List<List<Integer>> listColumnsB) {

        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < listRowsA.size(); i++) {
            List<Integer> rowResult = new ArrayList<>();
            for (int j = 0; j < listColumnsB.size(); j++) {
                int sum = 0;
                for (int k = 0; k < listRowsA.get(i).size(); k++) {
                    sum += listRowsA.get(i).get(k) * listColumnsB.get(j).get(k);
                }
                rowResult.add(sum);
            }
            result.add(rowResult);
        }
        return result;
    }

    public String viewTasksAsString() {
        StringBuilder result = new StringBuilder("---------- View my tasks ---------\n");
        result.append("\n> List the rows from Matrix A\n");
        for (List<Integer> row : listRowsA) {
            result.append(getRowString(row)).append("\n");
        }

        if ("Multiplication".equals(operation.getOperationName()))
            result.append("\n> List the columns from Matrix B\n");
        else
            result.append("\n> List the rows from Matrix B\n");

        for (List<Integer> rowsOrColumns : listRowsOrColumnsB) {
            result.append(getRowString(rowsOrColumns)).append("\n");
        }

        return result.toString();
    }

    public String viewResultAsString() {
        StringBuilder result = new StringBuilder("\n--- View my results for my task \"")
                .append(operation.getOperationName()).append("\" ---\n");
        for (List<Integer> row : taskResult) {
            result.append(getRowString(row)).append("\n");
        }
        return result.toString();
    }

    private String getRowString(List<Integer> row) {
        StringBuilder rowString = new StringBuilder("[ ");
        for (Integer element : row) {
            rowString.append(element).append(" ");
        }
        rowString.append("]");
        return rowString.toString();
    }}

    public String getClientId() {
        return clientId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public List<Integer> getTaskIndex() {
        return taskIndex;
    }

    public OperationType getOperation() {
        return operation;
    }

    public List<List<Integer>> getListRowsA() {
        return listRowsA;
    }

    public List<List<Integer>> getListRowsOrColumnsB() {
        return listRowsOrColumnsB;
    }

    public List<List<Integer>> getTaskResult() {
        return taskResult;
    }

}
