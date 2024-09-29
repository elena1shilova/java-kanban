package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        switch (exchange.getRequestMethod()) {
            case "GET": {
                String[] pathParts = exchange.getRequestURI().getPath().split("/");
                if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                    String result = getTasksList();
                    if (result.isEmpty()) {
                        sendNotFound(exchange, result);
                    } else {
                        sendText(exchange, result);
                    }
                    break;
                }
                if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
                    String result = getTask(pathParts[2]);
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
                Task task = gson.fromJson(requestBody.toString(), Task.class);
                if (task.getId() != null) {
                    try {
                        updateTask(task);
                        sendTextCreateOrUpdate(exchange, "Успешное изменение");

                    } catch (RuntimeException e) {
                        sendHasInteractions(exchange, "Пересечение");
                    }
                } else {
                    try {
                        sendTextCreateOrUpdate(exchange, addNewTask(task));

                    } catch (RuntimeException e) {
                        sendHasInteractions(exchange, "Пересечение");
                    }
                }
                break;
            }
            case "DELETE": {
                deleteTask(exchange);
                sendText(exchange, "Успешное удаление");
                break;
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private String getTasksList() {
        ArrayList<Task> tasks = manager.getTasksList();

        if (tasks.isEmpty()) {
            return "";
        } else {
            return gson.toJson(tasks);
        }
    }

    private String getTask(String id) {
        try {
            Task tasks = manager.getTask(Integer.parseInt(id));
            if (tasks == null) {
                return "";
            } else {
                return gson.toJson(tasks);
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ошибка идентификатора");
        }
    }

    private String addNewTask(Task task) {
        return gson.toJson(manager.addNewTask(task));
    }

    private void updateTask(Task task) {
        manager.updateTask(task);
    }

    private void deleteTask(HttpExchange exchange) {
        String[] requestQuery = exchange.getRequestURI().getQuery().split("=");

        try {
            manager.deleteTask(Integer.parseInt(requestQuery[1]));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ошибка идентификатора");
        }
    }
}

