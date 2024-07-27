package manager;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void testAddAndRetrieveTasks() {
        Task task = new Task("Task 1", "task1details");
        int id = taskManager.addNewTask(task);

        Task retrievedTask = taskManager.getTask(id);
        assertEquals(task, retrievedTask);
    }

    @Test
    public void testTaskImmutability() {
        Task task = new Task("Task 1", "task1details");
        int id = taskManager.addNewTask(task);

        Task retrievedTask = taskManager.getTask(id);
        assertEquals("Task 1", retrievedTask.getName());
        assertEquals("task1details", retrievedTask.getDetails());
    }

    @Test
    public void testHistoryManager() {
        Task task = new Task("Task 1", "task1details");
        int id = taskManager.addNewTask(task);

        taskManager.getTask(id);

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }
}
