package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
    }

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    public void testAddAndRetrieveTasks() {
        Task task = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        taskManager.addNewTask(task);

        Task retrievedTask = taskManager.getTask(1);
        assertEquals(task, retrievedTask);
    }

    @Test
    public void testTaskImmutability() {
        Task task = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        taskManager.addNewTask(task);

        Task retrievedTask = taskManager.getTask(1);
        assertEquals("Task 1", retrievedTask.getName());
        assertEquals("task1details", retrievedTask.getDetails());
    }

    @Test
    public void testHistoryManager() {
        Task task = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        taskManager.addNewTask(task);

        taskManager.getTask(1);

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }


    @Test
    public void testTaskIntervalIntersection() {
        taskManager = createTaskManager();
        Task task1 = new Task("Task 1", "Description", Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 1", "Description", Duration.ofMinutes(60), LocalDateTime.now());

        taskManager.addNewTask(task1);
        assertThrows(RuntimeException.class, () -> taskManager.addNewTask(task2));
    }
}
