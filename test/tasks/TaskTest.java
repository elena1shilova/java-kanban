package tasks;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {
    @Test
    public void testTasksEquality() {
        Task task1 = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        task1.setId(1);

        Task task2 = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        task2.setId(1);

        assertEquals(task1.getId(), task2.getId());
    }

    @Test
    public void testTaskInequality() {
        Task task1 = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        task1.setId(1);

        Task task2 = new Task("Task 2", "task2details", Duration.ofNanos(125412), LocalDateTime.now());
        task2.setId(2);

        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    public void testTaskUpdate() {
        Task task1 = new Task("Task 1", "task1details", Duration.ofNanos(125412), LocalDateTime.now());
        task1.setId(1);

        Task task2 = new Task("Task 2", "task2details", Duration.ofNanos(125412), LocalDateTime.now());
        task2.setId(2);
        task2.setId(3);

        assertEquals(2, task2.getId());
    }
}
