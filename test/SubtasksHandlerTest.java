import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handler.BaseHttpHandler;
import handler.DurationAdapter;
import handler.LocalDateTimeAdapter;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubtasksHandlerTest {

    private HttpTaskServer server;
    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    @BeforeEach
    public void setUp() throws IOException {
        server = new HttpTaskServer(taskManager);
        server.start();

    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    @Test
    public void testGetSubtasksList() throws IOException, InterruptedException {

        Epic epic1 = new Epic("Epic 1", "epic1details", Duration.ZERO, LocalDateTime.now().minusDays(5));
        taskManager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask(1, "Subtask 1", TaskStatus.NEW, "Details 1", epic1.getId(), Duration.ofHours(1), LocalDateTime.now());
        taskManager.addNewSubtask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }
}
