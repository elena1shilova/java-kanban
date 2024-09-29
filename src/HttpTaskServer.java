import com.sun.net.httpserver.HttpServer;
import handler.EpicsHandler;
import handler.HistoryHandler;
import handler.PrioritizedHandler;
import handler.SubtasksHandler;
import handler.TasksHandler;
import manager.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final InMemoryTaskManager taskManager;

    public HttpTaskServer(InMemoryTaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        initializeContexts();
    }

    private void initializeContexts() {
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public static void main(String[] args) {

        try {
            InMemoryTaskManager taskManager = new InMemoryTaskManager();
            HttpTaskServer server = new HttpTaskServer(taskManager);
            server.start();
            System.out.println("Server is running on port " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }
}
