package manager;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManager();

    @Test
    public void testAddNewTask() {
        taskManager = createTaskManager();
        Task task = new Task("Test Task", "Description", Duration.ofMinutes(60), LocalDateTime.now());
        Integer taskId = taskManager.addNewTask(task);
        assertNotNull(taskId);
        assertEquals(task, taskManager.getTask(taskId));
    }

    @Test
    public void testAddNewEpic() {
        taskManager = createTaskManager();
        Epic epic = new Epic("Test Epic", "Epic Description", Duration.ofMinutes(30), LocalDateTime.now());
        Integer epicId = taskManager.addNewEpic(epic);
        assertNotNull(epicId);
        assertEquals(epic, taskManager.getEpic(epicId));
    }

    @Test
    public void testAddNewSubtask() {
        taskManager = createTaskManager();
        Epic epic = new Epic("Test Epic", "Epic Description", Duration.ofMinutes(30), LocalDateTime.now());
        Integer epicId = taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Subtask Description", epicId, Duration.ofMinutes(31), LocalDateTime.now());
        Integer subtaskId = taskManager.addNewSubtask(subtask);
        assertNotNull(subtaskId);
        assertEquals(subtask, taskManager.getSubtask(subtaskId));
    }

    @Test
    public void testEpicStatusCalculation() {
        taskManager = createTaskManager();
        Epic epic = new Epic("Test Epic", "Epic Description", Duration.ofMinutes(30), LocalDateTime.now());
        Integer epicId = taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epicId, Duration.ofMinutes(30), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epicId, Duration.ofMinutes(30), LocalDateTime.now().plusDays(1));
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);

        Epic savedEpic = taskManager.getEpic(epicId);
        assertEquals(TaskStatus.DONE, savedEpic.getStatus());
    }
}
