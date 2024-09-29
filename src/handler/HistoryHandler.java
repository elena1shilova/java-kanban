package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(InMemoryTaskManager taskManager) {
        super();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET": {
                String[] pathParts = exchange.getRequestURI().getPath().split("/");
                if (pathParts.length == 2 && pathParts[1].equals("history")) {

                    sendText(exchange, getHistory());

                    break;
                }
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private String getHistory() {
        List<Task> tasks = manager.getHistory();
        if (tasks == null || tasks.isEmpty()) {
            return "";
        } else {
            return gson.toJson(tasks);
        }
    }
}
