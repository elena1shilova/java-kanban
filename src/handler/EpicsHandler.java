package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Epic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        switch (exchange.getRequestMethod()) {
            case "GET": {
                String[] pathParts = exchange.getRequestURI().getPath().split("/");
                if (pathParts.length == 2 && pathParts[1].equals("epics")) {
                    String result = getEpicsList();
                    if (result.isEmpty()) {
                        sendNotFound(exchange, result);
                    } else {
                        sendText(exchange, result);
                    }
                    break;
                }
                if (pathParts.length == 3 && pathParts[1].equals("epics")) {
                    String result = getEpic(pathParts[2]);
                    if (result.isEmpty()) {
                        sendNotFound(exchange, result);
                    } else {
                        sendText(exchange, result);
                    }
                    break;
                }
                if (pathParts.length == 4 && pathParts[1].equals("epics")) {
                    String result = getEpicSubtask(exchange);
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
                Epic epic = gson.fromJson(requestBody.toString(), Epic.class);
                if (epic.getId() != null) {
                    try {
                        updateEpic(epic);
                        sendTextCreateOrUpdate(exchange, "Успешное изменение");

                    } catch (RuntimeException e) {
                        sendHasInteractions(exchange, "Пересечение");
                    }
                } else {
                    try {
                        sendTextCreateOrUpdate(exchange, addNewEpic(epic));

                    } catch (RuntimeException e) {
                        sendHasInteractions(exchange, "Пересечение");
                    }
                }
                break;
            }
            case "DELETE": {
                deleteEpic(exchange);
                sendText(exchange, "Успешное удаление");
                break;
            }
            default:
                sendNotFound(exchange, "Такого эндпоинта не существует");
        }
    }

    private String getEpicsList() {
        ArrayList<Epic> epics = manager.getEpicsList();
        if (epics.isEmpty()) {
            return "";
        } else {
            return gson.toJson(epics);
        }
    }

    private String getEpic(String id) {

        try {

            Epic epic = manager.getEpic(Integer.parseInt(id));
            if (epic == null) {
                return "";
            } else {
                return gson.toJson(epic);
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ошибка идентификатора");
        }
    }

    private String getEpicSubtask(HttpExchange exchange) {

        String[] requestQuery = exchange.getRequestURI().getQuery().split("=");
        try {
            Epic epic = manager.getEpic(Integer.parseInt(requestQuery[1]));
            if (epic == null) {
                return "";
            } else {
                return gson.toJson(epic.getSubtasksList());
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ошибка идентификатора");
        }
    }

    private String addNewEpic(Epic epic) {

        return gson.toJson(manager.addNewEpic(epic));
    }

    private void updateEpic(Epic epic) {

        manager.updateEpic(epic);
    }

    private void deleteEpic(HttpExchange exchange) {

        String[] requestQuery = exchange.getRequestURI().getQuery().split("=");

        try {
            manager.deleteEpic(Integer.parseInt(requestQuery[1]));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ошибка идентификатора");
        }
    }
}
