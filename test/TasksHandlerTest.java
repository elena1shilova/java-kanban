import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handler.BaseHttpHandler;
import handler.DurationAdapter;
import handler.LocalDateTimeAdapter;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

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

public class TasksHandlerTest {
    private HttpTaskServer server;
    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();

    private Task task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now());

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    @BeforeEach
    public void setUp() throws IOException {
        server = new HttpTaskServer(taskManager);
        taskManager.addNewTask(task);
        server.start();

    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    @Test
    public void testGetTasksList() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

    @Test
    public void testGetTasksForId() throws IOException, InterruptedException {
        Task task2 = new Task("Test 3", "Testing task 3", Duration.ofMinutes(5), LocalDateTime.now());
        BaseHttpHandler.manager.addNewTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + 2);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }

    @Test
    public void testAddTasks() throws IOException, InterruptedException {
        task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now().minusDays(10));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = taskManager.getTasksList();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTasks() throws IOException, InterruptedException {

        Task task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now());
        BaseHttpHandler.manager.addNewTask(task);

        Task taskUpdate = new Task("Update Name", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now().plusDays(1));
        taskUpdate.setId(1);
        String taskJson = gson.toJson(taskUpdate);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = BaseHttpHandler.manager.getTasksList();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Update Name", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }


}
