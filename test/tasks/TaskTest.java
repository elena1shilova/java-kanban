package tasks;

import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void testTasksEquality() {
        Task task1 = new Task("Task 1", "task1details");
        task1.setId(1);

        Task task2 = new Task("Task 1", "task1details");
        task2.setId(1);

        assertEquals(task1, task2);
    }

    @Test
    public void testTaskInequality() {
        Task task1 = new Task("Task 1", "task1details");
        task1.setId(1);

        Task task2 = new Task("Task 2", "task2details");
        task2.setId(2);

        assertNotEquals(task1, task2);
    }
}
