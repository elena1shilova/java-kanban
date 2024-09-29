package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import tasks.Subtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    public SubtasksHandler(InMemoryTaskManager taskManager) {
        super();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        switch (exchange.getRequestMethod()) {
            case "GET": {
                String[] pathParts = exchange.getRequestURI().getPath().split("/");
                if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
                    String result = getSubtasksList();
                    if (result.isEmpty()) {
                        sendNotFound(exchange, result);
                    } else {
                        sendText(exchange, result);
                    }
                    break;
                }
                if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
                    String result = getSubtask(pathParts[2]);
                    if (result.isEmpty()) {
                        sendNotFound(exchange, result);
                    } else {
                        sendText(exchange, result);
                    }
                    break;
                }
            }
            case "POST": {
                InputStream bodyContent = exchange.getRequestBody();
                if (bodyContent == null) {
                    sendNotFound(exchange, "отсутсвует данные для сохранения/изменения");
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(bodyContent));
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line).append("\n");
                }
                reader.close();
                Subtask task = gson.fromJson(requestBody.toString(), Subtask.class);
                if (task.getId() != null) {
                    try {
                        updateSubtask(task);
                        sendTextCreateOrUpdate(exchange, "Успешное изменение");

                    } catch (RuntimeException e) {
                        sendHasInteractions(exchange, "Пересечение");
                    }
                } else {
                    try {
                        sendTextCreateOrUpdate(exchange, addNewSubtask(task));

                    } catch (RuntimeException e) {
                        sendHasInteractions(exchange, "Пересечение");
                    }
                }
                break;
            }
            case "DELETE": {
                deleteSubtask(exchange);
                sendText(exchange, "Успешное удаление");
                break;
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private String getSubtasksList() {
        ArrayList<Subtask> subtasks = manager.getSubtasksList();
        if (subtasks.isEmpty()) {
            return "";
        } else {
            return gson.toJson(subtasks);
        }
    }

    private String getSubtask(String id) {

        try {
            Subtask subtasks = manager.getSubtask(Integer.parseInt(id));
            if (subtasks == null) {
                return "";
            } else {
                return gson.toJson(subtasks);
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ошибка идентификатора");
        }
    }

    private String addNewSubtask(Subtask subtasks) {

        return gson.toJson(manager.addNewSubtask(subtasks));
    }

    private void updateSubtask(Subtask subtasks) {

        manager.updateSubtask(subtasks);
    }

    private void deleteSubtask(HttpExchange exchange) {

        String[] requestQuery = exchange.getRequestURI().getQuery().split("=");

        try {
            manager.deleteSubtask(Integer.parseInt(requestQuery[1]));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ошибка идентификатора");
        }
    }
}
