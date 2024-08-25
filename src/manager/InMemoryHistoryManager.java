package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> mapHistory = new HashMap<>();
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

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) head = newNode;
        else oldTail.next = newNode;
        mapHistory.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        if (!mapHistory.isEmpty()) {
            ArrayList tasks = new ArrayList<>();
            Node node = head;
            while (node != null) {
                tasks.add(node.data);
                node = node.next;
            }
            return tasks;
        } else return null;
    }

    @Override
    public void remove(int id) {

        removeNode(mapHistory.get(id));
        mapHistory.remove(id);

    }

    private void removeNode(Node node) {
        if (node.next != null) {
            node.next.prev = node.prev;
        } else tail = node.prev;
        if (node.prev != null) {
            node.prev.next = node.next;
        } else head = node.next;
    }
    public void removeAll() {
        mapHistory.clear();
        head = null;
        tail = null;
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
