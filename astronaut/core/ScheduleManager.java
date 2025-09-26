package com.jitesh.astronaut.core;

import com.jitesh.astronaut.model.Task;
import com.jitesh.astronaut.observer.Notifier;
import java.util.*;

public class ScheduleManager {
    private static ScheduleManager instance;
    private final List<Task> tasks = new ArrayList<>();
    private final Notifier notifier = new Notifier();

    private ScheduleManager() {}

    public static synchronized ScheduleManager getInstance() {
        if (instance == null) instance = new ScheduleManager();
        return instance;
    }

    public static synchronized void resetForTests() { instance = null; }

    public void registerObserver(com.jitesh.astronaut.observer.Observer o) { notifier.register(o); }

    public boolean addTask(Task task) {
        Objects.requireNonNull(task);
        for (Task t : tasks) {
            boolean overlap = task.getStart().isBefore(t.getEnd()) && task.getEnd().isAfter(t.getStart());
            if (overlap) {
                notifier.notifyAllObservers("Conflict with task: " + t);
                return false;
            }
        }
        tasks.add(task);
        tasks.sort(Comparator.comparing(Task::getStart));
        notifier.notifyAllObservers("Task added: " + task);
        return true;
    }

    public List<Task> listTasks() { return List.copyOf(tasks); }

    public boolean removeTask(String id) {
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));
        if (removed) notifier.notifyAllObservers("Task removed: " + id);
        return removed;
    }

    public void markCompleted(String id) {
        for (Task t : tasks) {
            if (t.getId().equals(id)) {
                t.markCompleted();
                notifier.notifyAllObservers("Task completed: " + id);
                return;
            }
        }
        notifier.notifyAllObservers("Task not found: " + id);
    }
}