package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class InMemoryHistoryManagerTest {

    private HistoryManager manager;

    @BeforeEach
    public void setUp() {
        manager = Managers.getDefaultHistory();
    }
    @Test
    void historyAdd() {
        Task task1 = new Task("Task 1", "task1details");
        task1.setId(1);
        manager.add(task1);

        Task task2 = new Task("Task 2", "task2details");
        task2.setId(2);
        manager.add(task2);

        Task task3 = new Task("Task 3", "task3details");
        task3.setId(3);
        manager.add(task3);
        manager.add(task2);

        Task task4 = new Task("Task 4", "task4details");
        task4.setId(4);
        manager.add(task4);

        ArrayList<Task> tasksBeforeDelete = manager.getTasks();
        assertEquals(2, (int) tasksBeforeDelete.get(2).getId());

        manager.remove(task1.getId());

        ArrayList<Task> tasksAfterDelete = manager.getTasks();
        assertEquals(2, (int) tasksAfterDelete.get(1).getId());
        assertFalse(tasksAfterDelete.contains(task1));
    }
}
