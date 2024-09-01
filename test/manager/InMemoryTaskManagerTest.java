package manager;

import exeption.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void testAddAndRetrieveTasks() throws ManagerSaveException {
        Task task = new Task("Task 1", "task1details");
        taskManager.addNewTask(task);

        Task retrievedTask = taskManager.getTask(1);
        assertEquals(task, retrievedTask);
    }

    @Test
    public void testTaskImmutability() throws ManagerSaveException {
        Task task = new Task("Task 1", "task1details");
        taskManager.addNewTask(task);

        Task retrievedTask = taskManager.getTask(1);
        assertEquals("Task 1", retrievedTask.getName());
        assertEquals("task1details", retrievedTask.getDetails());
    }

    @Test
    public void testHistoryManager() throws ManagerSaveException {
        Task task = new Task("Task 1", "task1details");
        taskManager.addNewTask(task);

        taskManager.getTask(1);

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }
}
