package com.jitesh.astronaut.model;

import java.time.LocalTime;
import java.util.Objects;

public class Task {
    private final String id;
    private final String description;
    private final LocalTime start;
    private final LocalTime end;
    private boolean completed;

    public Task(String id, String description, LocalTime start, LocalTime end) {
        if (end.isBefore(start)) throw new IllegalArgumentException("End before start");
        this.id = id;
        this.description = description;
        this.start = start;
        this.end = end;
        this.completed = false;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
    public boolean isCompleted() { return completed; }
    public void markCompleted() { this.completed = true; }

    @Override
    public String toString() {
        return "[" + id + "] " + description + " (" + start + "-" + end + ") " + (completed ? "DONE" : "TODO");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task t = (Task) o;
        return Objects.equals(id, t.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}