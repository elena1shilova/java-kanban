package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET": {
                String[] pathParts = exchange.getRequestURI().getPath().split("/");
                if (pathParts.length == 2 && pathParts[1].equals("prioritized")) {

                    sendText(exchange, getPrioritizedTasks());

                    break;
                }
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private String getPrioritizedTasks() {
        List<Task> tasks = manager.getPrioritizedTasks();
        if (tasks == null || tasks.isEmpty()) {
            return "";
        } else {
            return gson.toJson(tasks);
        }
    }
}
