package manager;

import exeption.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path filePath;

    public FileBackedTaskManager(Path filePath) {
        this.filePath = filePath;
    }

    private enum TaskType {
        TASK, EPIC, SUBTASK
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(filePath)))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getTasksList()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getEpicsList()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasksList()) {
                writer.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                TaskType.TASK,
                task.getName(),
                task.getStatus(),
                task.getDetails(),
                "");
    }

    String toString(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,%s",
                epic.getId(),
                TaskType.EPIC,
                epic.getName(),
                epic.getStatus(),
                epic.getDetails(),
                "");
    }

    String toString(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%d",
                subtask.getId(),
                TaskType.SUBTASK,
                subtask.getName(),
                subtask.getStatus(),
                subtask.getDetails(),
                subtask.getEpicID());
    }

    @Override
    public void addNewTask(Task task) throws ManagerSaveException {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) throws ManagerSaveException {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void addNewSubtask(Subtask subtask) throws ManagerSaveException {
        super.addNewSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        super.updateEpic(epic);
        save();
    }

    public static Task fromString(String value) {
        String[] string = value.split(",");
        return switch (TaskType.valueOf(string[1])) {
            case TASK -> new Task(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4]);
            case EPIC -> new Epic(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4]);
            case SUBTASK ->
                    new Subtask(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4], Integer.parseInt(string[5]));
        };
    }

    public static FileBackedTaskManager loadFromFile(Path file) throws ManagerSaveException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = Files.newBufferedReader(file)) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);
                if (task instanceof Epic) {
                    manager.addNewEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addNewSubtask((Subtask) task);
                } else {
                    manager.addNewTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return manager;
    }

}
