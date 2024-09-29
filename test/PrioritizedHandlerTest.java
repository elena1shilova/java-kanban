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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PrioritizedHandlerTest {

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
    public void testGetPrioritizedList() throws IOException, InterruptedException {

        Task task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now());
        taskManager.addNewTask(task);

        Task taskUpdate = new Task("Update Name", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now().plusDays(1));
        taskUpdate.setId(1);
        taskManager.updateTask(taskUpdate);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(response.body());
    }
}
