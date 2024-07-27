package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> viewHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        if(task != null) {
            if (viewHistory.size() == 10) {
                viewHistory.removeFirst();
            }
            viewHistory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(viewHistory);
    }
}
