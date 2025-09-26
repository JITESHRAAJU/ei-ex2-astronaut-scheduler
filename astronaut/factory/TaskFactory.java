package com.jitesh.astronaut.factory;

import com.jitesh.astronaut.model.Task;
import java.time.LocalTime;
import java.util.UUID;

public class TaskFactory {
    public static Task create(String description, String startTime, String endTime) {
        LocalTime s = LocalTime.parse(startTime);
        LocalTime e = LocalTime.parse(endTime);
        return new Task(UUID.randomUUID().toString(), description, s, e);
    }
}