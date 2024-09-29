import handler.BaseHttpHandler;
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
}
