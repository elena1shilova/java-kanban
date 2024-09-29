package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

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
    public void testTaskIntervalIntersection() {
        taskManager = createTaskManager();
        Task task1 = new Task("Task 1", "Description", Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 1", "Description", Duration.ofMinutes(60), LocalDateTime.now());

        taskManager.addNewTask(task1);
        assertThrows(RuntimeException.class, () -> taskManager.addNewTask(task2));
    }
}
