package manager;

import exception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    private Path TestFilePath;
    private static final String HEDER = "id,type,name,status,description,epic";
    private FileBackedTaskManager taskManager;

    @BeforeEach
    void setUp() throws IOException {
        TestFilePath = Files.createTempFile("temp", ".csv");
        taskManager = new FileBackedTaskManager(TestFilePath);
    }

    @Test
    void saveEmptyFile() throws IOException {
        taskManager.save();
        assertTrue(Files.exists(TestFilePath));
        assertEquals(HEDER, Files.readString(TestFilePath).trim());
    }

    @Test
    void loadEmptyFile() throws ManagerSaveException {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(TestFilePath);
        assertTrue(loadedManager.getTasksList().isEmpty());
        assertTrue(loadedManager.getEpicsList().isEmpty());
        assertTrue(loadedManager.getSubtasksList().isEmpty());
    }

    @Test
    void saveAndLoadSeveralTasks() throws ManagerSaveException {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Epic epic1 = new Epic(2, "Эпик 1", TaskStatus.NEW, "Описание эпика 1");
        Subtask subtask1 = new Subtask(3, "Подзадача 1", TaskStatus.NEW, "Описание подзадачи 1", epic1.getId());

        taskManager.addNewTask(task1);
        taskManager.addNewEpic(epic1);
        taskManager.addNewSubtask(subtask1);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(TestFilePath);

        assertEquals(1, loadedManager.getTasksList().size());
        assertEquals(task1, loadedManager.getTasksList().get(0));

        assertEquals(1, loadedManager.getEpicsList().size());
        assertEquals(epic1, loadedManager.getEpicsList().get(0));

        assertEquals(1, loadedManager.getSubtasksList().size());
        assertEquals(subtask1, loadedManager.getSubtasksList().get(0));
    }

    @Test
    void saveAndUpdateTask() throws ManagerSaveException {
        Task task = new Task("Задача", "Описание");
        taskManager.addNewTask(task);
        taskManager.save();

        task.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);
        taskManager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(TestFilePath);

        assertEquals(1, loadedManager.getTasksList().size());
        assertEquals(TaskStatus.DONE, loadedManager.getTasksList().get(0).getStatus());
    }

    @Test
    void testSave() throws ManagerSaveException, IOException {
        Task task = new Task(1, "Задача 1", TaskStatus.NEW, "Описание задачи 1");
        Epic epic = new Epic(2, "Эпик 1", TaskStatus.NEW, "Описание эпика 1");
        Subtask subtask = new Subtask(3, "Подзадача 1", TaskStatus.NEW, "Описание подзадачи 1", 2);

        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);

        List<String> lines = Files.readAllLines(TestFilePath);

        assertEquals(4, lines.size());
        assertTrue(lines.get(1).contains("TASK"));
        assertTrue(lines.get(2).contains("EPIC"));
        assertTrue(lines.get(3).contains("SUBTASK"));
    }

    @Test
    void testSaveAndLoadMultipleTasks() throws ManagerSaveException {
        FileBackedTaskManager manager = new FileBackedTaskManager(TestFilePath);

        Task task1 = new Task(1, "Задача 1", TaskStatus.NEW, "Описание задачи 1");
        Task task2 = new Task(2, "Задача 2", TaskStatus.DONE, "Описание задачи 2");

        Epic epic1 = new Epic(3, "Эпик 1", TaskStatus.IN_PROGRESS, "Описание эпика 1");
        Epic epic2 = new Epic(4, "Эпик 2", TaskStatus.NEW, "Описание эпика 2");

        Subtask subtask1 = new Subtask(5, "Подзадача 1", TaskStatus.DONE, "Описание подзадачи 1", 3);
        Subtask subtask2 = new Subtask(6, "Подзадача 2", TaskStatus.IN_PROGRESS, "Описание подзадачи 2", 4);

        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(TestFilePath);

        assertEquals(manager.getTasksList(), loadedManager.getTasksList());
        assertEquals(manager.getEpicsList(), loadedManager.getEpicsList());
        assertEquals(manager.getSubtasksList(), loadedManager.getSubtasksList());
    }

}
