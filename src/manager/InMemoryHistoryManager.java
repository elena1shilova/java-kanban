package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node> mapHistory = new HashMap<>();
    private Node tail;
    private Node head;

    @Override
    public void add(Task task) {
        if (task != null) {
            if (mapHistory.containsKey(task.getId())) {
                remove(task.getId());
            }
            linkLast(task);
        }
    }

    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) head = newNode;
        else oldTail.next = newNode;
        mapHistory.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {

        removeNode(mapHistory.get(id));
        mapHistory.remove(id);

    }

    public void removeNode(Node node) {
        if (node.next != null) {
            node.next.prev = node.prev;
        } else tail = node.prev;
        if (node.prev != null) {
            node.prev.next = node.next;
        } else head = node.next;
    }

    @Override
    public ArrayList<Task> getTasks() {
        if (!mapHistory.isEmpty()) {
            ArrayList<Task> tasks = new ArrayList<>();
            tasks.add(head.data);
            Node node = head;
            while (node.next != null) {
                node = node.next;
                tasks.add(node.data);
            }
            return tasks;
        } else return null;
    }

    static class Node {

        public Task data;
        public Node next;
        public Node prev;

        public Node(Task data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

    }

}
