import com.sun.net.httpserver.HttpServer;
import handler.EpicsHandler;
import handler.HistoryHandler;
import handler.PrioritizedHandler;
import handler.SubtasksHandler;
import handler.TasksHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final HttpServer httpServer;

    static {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);

            httpServer.createContext("/tasks", new TasksHandler());
            httpServer.createContext("/subtasks", new SubtasksHandler());
            httpServer.createContext("/epics", new EpicsHandler());
            httpServer.createContext("/history", new HistoryHandler());
            httpServer.createContext("/prioritized", new PrioritizedHandler());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        start();
    }

    public static void start() {

            httpServer.start();

    }

    public static void stop() {

            httpServer.stop(1);

    }
}
