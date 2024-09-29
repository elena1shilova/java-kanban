import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handler.BaseHttpHandler;
import handler.DurationAdapter;
import handler.LocalDateTimeAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EpicsHandlerTest {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    @BeforeEach
    public void setUp() {
        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }

    @Test
    public void testGetEpicList() throws IOException, InterruptedException {

        Epic epic = new Epic("Epic 1", "epic1details", Duration.ZERO, LocalDateTime.now());
        BaseHttpHandler.manager.addNewEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

    @Test
    public void testGetEpicsForId() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic 1", "epic1details", Duration.ZERO, LocalDateTime.now().minusDays(5));
        Epic epic2 = new Epic("Epic 2", "epic2details", Duration.ZERO, LocalDateTime.now());
        BaseHttpHandler.manager.addNewEpic(epic1);
        BaseHttpHandler.manager.addNewEpic(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + 2);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

    @Test
    public void testAddEpics() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic 1", "epic1details", Duration.ZERO, LocalDateTime.now().minusDays(5));
        String taskJson = gson.toJson(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Epic> tasksFromManager = BaseHttpHandler.manager.getEpicsList();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Epic 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateEpics() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic 1", "epic1details", Duration.ZERO, LocalDateTime.now().minusDays(5));
        BaseHttpHandler.manager.addNewEpic(epic1);

        Task epicUpdate = new Task("Update Name", "epic1details", Duration.ZERO, LocalDateTime.now().minusDays(5));
        epicUpdate.setId(1);
        String taskJson = gson.toJson(epicUpdate);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Epic> tasksFromManager = BaseHttpHandler.manager.getEpicsList();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Update Name", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubtasksList() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic 1", "epic1details", Duration.ZERO, LocalDateTime.now().minusDays(5));
        BaseHttpHandler.manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask(1, "Subtask 1", TaskStatus.NEW, "Details 1", epic1.getId(), Duration.ofHours(1), LocalDateTime.now());
        BaseHttpHandler.manager.addNewSubtask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

    @Test
    public void testGetSubtasksForId() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic 1", "epic1details", Duration.ZERO, LocalDateTime.now().minusDays(6));
        BaseHttpHandler.manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask(1, "Subtask 1", TaskStatus.NEW, "Details 1", epic1.getId(), Duration.ofHours(1), LocalDateTime.now().minusDays(5));
        Subtask subtask2 = new Subtask(2, "Subtask 2", TaskStatus.NEW, "Details 2", epic1.getId(), Duration.ofHours(1), LocalDateTime.now());
        BaseHttpHandler.manager.addNewSubtask(subtask1);

        BaseHttpHandler.manager.addNewSubtask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + 2);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic 1", "epic1details", Duration.ZERO, LocalDateTime.now().minusDays(6));
        BaseHttpHandler.manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", TaskStatus.NEW, "Details 1", epic1.getId(), Duration.ofHours(1), LocalDateTime.now().minusDays(5));
        String taskJson = gson.toJson(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Subtask> tasksFromManager = BaseHttpHandler.manager.getSubtasksList();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Subtask 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetHistoryList() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic 1", "epic1details", Duration.ZERO, LocalDateTime.now().minusDays(5));
        BaseHttpHandler.manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask(1, "Subtask 1", TaskStatus.NEW, "Details 1", epic1.getId(), Duration.ofHours(1), LocalDateTime.now());
        BaseHttpHandler.manager.addNewSubtask(subtask1);
        BaseHttpHandler.manager.getEpic(1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

    @Test
    public void testGetPrioritizedList() throws IOException, InterruptedException {

        Task task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now());
        BaseHttpHandler.manager.addNewTask(task);

        Task taskUpdate = new Task("Update Name", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now().plusDays(1));
        taskUpdate.setId(1);
        BaseHttpHandler.manager.updateTask(taskUpdate);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

}
