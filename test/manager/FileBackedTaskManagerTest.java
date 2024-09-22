package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private Path testFilePath;
    private static final String HEADER = "id,type,name,status,description,duration,startTime,epic";
    private FileBackedTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        try {
            testFilePath = Files.createTempFile("temp", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskManager = new FileBackedTaskManager(testFilePath);
    }

    @Override
    protected FileBackedTaskManager createTaskManager() {
        Path filePath = Paths.get("test_tasks.csv");
        return new FileBackedTaskManager(filePath);
    }

    @Test
    void saveEmptyFile() throws IOException {
        taskManager.save();
        assertTrue(Files.exists(testFilePath));
        assertEquals(HEADER, Files.readString(testFilePath).trim());
    }

    @Test
    void loadEmptyFile() {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFilePath);
        assertTrue(loadedManager.getTasksList().isEmpty());
        assertTrue(loadedManager.getEpicsList().isEmpty());
        assertTrue(loadedManager.getSubtasksList().isEmpty());
    }

    @Test
    void saveAndLoadSeveralTasks() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Duration.ofNanos(125412), LocalDateTime.now());
        Epic epic1 = new Epic(2, "Эпик 1", TaskStatus.NEW, "Описание эпика 1", Duration.ofNanos(125412), LocalDateTime.now());
        Subtask subtask1 = new Subtask(3, "Подзадача 1", TaskStatus.NEW, "Описание подзадачи 1", epic1.getId(), Duration.ofNanos(125412), LocalDateTime.now());

        taskManager.addNewTask(task1);
        taskManager.addNewEpic(epic1);
        taskManager.addNewSubtask(subtask1);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFilePath);

        assertEquals(1, loadedManager.getTasksList().size());
        assertEquals(task1, loadedManager.getTasksList().get(0));

        assertEquals(1, loadedManager.getEpicsList().size());
        assertEquals(epic1, loadedManager.getEpicsList().get(0));

        assertEquals(1, loadedManager.getSubtasksList().size());
        assertEquals(subtask1, loadedManager.getSubtasksList().get(0));
    }

    @Test
    void saveAndUpdateTask() {
        Task task = new Task("Задача", "Описание", Duration.ofNanos(125412), LocalDateTime.now());
        taskManager.addNewTask(task);
        taskManager.save();

        task.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);
        taskManager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFilePath);

        assertEquals(1, loadedManager.getTasksList().size());
        assertEquals(TaskStatus.DONE, loadedManager.getTasksList().get(0).getStatus());
    }

    @Test
    void testSave() throws IOException {
        Task task = new Task(1, "Задача 1", TaskStatus.NEW, "Описание задачи 1", Duration.ofNanos(125412), LocalDateTime.now());
        Epic epic = new Epic(2, "Эпик 1", TaskStatus.NEW, "Описание эпика 1", Duration.ofNanos(125412), LocalDateTime.now());
        Subtask subtask = new Subtask(3, "Подзадача 1", TaskStatus.NEW, "Описание подзадачи 1", 2, Duration.ofNanos(125412), LocalDateTime.now());

        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);

        List<String> lines = Files.readAllLines(testFilePath);

        assertEquals(4, lines.size());
        assertTrue(lines.get(1).contains("TASK"));
        assertTrue(lines.get(2).contains("EPIC"));
        assertTrue(lines.get(3).contains("SUBTASK"));
    }

    @Test
    void testSaveAndLoadMultipleTasks() {
        FileBackedTaskManager manager = new FileBackedTaskManager(testFilePath);

        Task task1 = new Task(1, "Задача 1", TaskStatus.NEW, "Описание задачи 1", Duration.ofNanos(125412), LocalDateTime.now());
        Task task2 = new Task(2, "Задача 2", TaskStatus.DONE, "Описание задачи 2", Duration.ofNanos(111), LocalDateTime.now().plusDays(1));

        Epic epic1 = new Epic(3, "Эпик 1", TaskStatus.IN_PROGRESS, "Описание эпика 1", Duration.ofNanos(125412), LocalDateTime.now());
        Epic epic2 = new Epic(4, "Эпик 2", TaskStatus.NEW, "Описание эпика 2", Duration.ofNanos(111), LocalDateTime.now());

        Subtask subtask1 = new Subtask(5, "Подзадача 1", TaskStatus.DONE, "Описание подзадачи 1", 3, Duration.ofNanos(125412), LocalDateTime.now());
        Subtask subtask2 = new Subtask(6, "Подзадача 2", TaskStatus.IN_PROGRESS, "Описание подзадачи 2", 4, Duration.ofNanos(1111), LocalDateTime.now());

        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFilePath);

        assertEquals(manager.getTasksList(), loadedManager.getTasksList());
        assertEquals(manager.getEpicsList(), loadedManager.getEpicsList());
        assertEquals(manager.getSubtasksList(), loadedManager.getSubtasksList());
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
