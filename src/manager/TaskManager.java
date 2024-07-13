package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager implements ITaskManager{

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    private Integer taskId = 1;
    @Override
    public Task getTask(Integer taskId) {
        return tasks.get(taskId);
    }

    @Override
    public Subtask getSubtask(Integer taskId) {
        return subtasks.get(taskId);
    }

    @Override
    public Epic getEpic(Integer taskId) {
        return epics.get(taskId);
    }

    @Override
    public ArrayList<String> getTasksList() {
        ArrayList <String> taskList = new ArrayList<>();
        for (Task task : tasks.values()) {
            taskList.add(task.getName());
        }
        return taskList;
    }

    @Override
    public ArrayList<String> getSubtasksList() {
        ArrayList <String> taskList = new ArrayList<>();
        for (Task task : subtasks.values()) {
            taskList.add(task.getName());
        }
        return taskList;
    }

    @Override
    public ArrayList<String> getEpicsList() {
        ArrayList <String> taskList = new ArrayList<>();
        for (Task task : epics.values()) {
            taskList.add(task.getName());
        }
        return taskList;
    }

    @Override
    public Integer addNewTask(Task task) {
        task.setId(taskId);
        tasks.put(taskId, task);
        return taskId++;
    }

    @Override
    public Integer addNewEpic(Epic epic) {
        epic.setId(taskId);
        epics.put(taskId, epic);
        return taskId++;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask, Integer epicID) {
        subtask.setId(taskId);
        subtask.setEpicID(epicID);
        subtasks.put(taskId, subtask);
        (epics.get(epicID)).subtasksList.put(taskId,subtask);                                                                //передаем эпику информацию о подзадаче
        (epics.get(epicID)).updateStatus();                                                                                         //меняем статус эпика
        return taskId++;
    }

    @Override
    public void updateTask(Task task, Integer id) {
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void updateSubtask(Subtask subtask, Integer id) {
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicID()).updateStatus();
    }

    @Override
    public void updateEpic(Epic epic, Integer id) {
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void delete() {
        tasks.clear();
        subtasks.clear();
        epics.clear();
        taskId = 1;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            if ((subtask.getEpicID()!=null)&&(epics.get(subtask.getEpicID())!=null)) {
                epics.get(subtask.getEpicID()).subtasksList.remove(subtask.getId());
            }
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            for (Integer id : epic.subtasksList.keySet()){
                (subtasks.get(id)).setEpicID(0);
            }
        }
        epics.clear();
    }

    @Override
    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubtask(Integer id) {
        epics.get((subtasks.get(id)).getEpicID()).subtasksList.remove(id);
        subtasks.remove(id);
    }

    @Override
    public void deleteEpic(Integer id) {
        for (Integer subtaskID : (epics.get(id)).subtasksList.keySet()){
            (subtasks.get(subtaskID)).setEpicID(0);
        }
        epics.remove(id);
    }
}
