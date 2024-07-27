package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    /*
    методы запроса инф-ии по ид
     */
    Task getTask(Integer taskID);

    Subtask getSubtask(Integer taskID) ;

    Epic getEpic(Integer taskID);

    /*
    методы запроса всей инф-ии в виде списка
     */
    ArrayList<Task> getTasksList();
    ArrayList<Subtask> getSubtasksList();
    ArrayList<Epic> getEpicsList();

    /*
    создание новой таски/эпика/сабтаски
     */
    Integer addNewTask(Task task);

    Integer addNewEpic(Epic epic);

    Integer addNewSubtask(Subtask subtask);

    /*
    обновление таски/эпика/сабтаски
     */
    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    /*
    удаление таски/эпика/сабтаски по ид (или всех)
     */
    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();


    void deleteTask(Integer id) ;

    void deleteSubtask(Integer id);

    void deleteEpic(Integer id);

    List<Subtask> getSubtasksOfEpic(Integer epicID);

    List<Task> getHistory();
}
