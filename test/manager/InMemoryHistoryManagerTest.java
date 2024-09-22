package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
        Task task1 = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        task1.setId(1);
        manager.add(task1);

        Task task2 = new Task("Task 2", "task2details", Duration.ofNanos(125412), LocalDateTime.now());
        task2.setId(2);
        manager.add(task2);

        Task task3 = new Task("Task 3", "task3details", Duration.ofNanos(125412), LocalDateTime.now());
        task3.setId(3);
        manager.add(task3);
        manager.add(task2);

        Task task4 = new Task("Task 4", "task4details", Duration.ofNanos(125412), LocalDateTime.now());
        task4.setId(4);
        manager.add(task4);

        List<Task> tasksBeforeDelete = manager.getHistory();
        assertEquals(2, (int) tasksBeforeDelete.get(2).getId());

        manager.remove(task1.getId());

        List<Task> tasksAfterDelete = manager.getHistory();
        assertEquals(2, (int) tasksAfterDelete.get(1).getId());
        assertFalse(tasksAfterDelete.contains(task1));
    }

    @Test
    void testAddDuplicateTask() {
        Task task = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        task.setId(1);
        manager.add(task);
        manager.add(task);

        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void testRemoveFromBeginning() {
        HistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        Task task2 = new Task("Task 2", "task2details", Duration.ofNanos(125412), LocalDateTime.now());
        task1.setId(1);
        task2.setId(2);
        manager.add(task1);
        manager.add(task2);

        manager.remove(task1.getId());

        assertEquals(1, manager.getHistory().size());
        assertEquals("Task 2", manager.getHistory().get(0).getName());
    }

    @Test
    void testRemoveFromMiddle() {
        HistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        Task task2 = new Task("Task 2", "task2details", Duration.ofNanos(125412), LocalDateTime.now());
        Task task3 = new Task("Task 3", "task3details", Duration.ofNanos(125412), LocalDateTime.now());
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(task2.getId());

        assertEquals(2, manager.getHistory().size());
        assertEquals("Task 1", manager.getHistory().get(0).getName());
        assertEquals("Task 3", manager.getHistory().get(1).getName());
    }

    @Test
    void testRemoveFromEnd() {
        HistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        Task task2 = new Task("Task 2", "task2details", Duration.ofNanos(125412), LocalDateTime.now());
        Task task3 = new Task("Task 3", "task3details", Duration.ofNanos(125412), LocalDateTime.now());
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        manager.remove(task3.getId());

        assertEquals(2, manager.getHistory().size());
        assertEquals("Task 1", manager.getHistory().get(0).getName());
        assertEquals("Task 2", manager.getHistory().get(1).getName());
    }
}
