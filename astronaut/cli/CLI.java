package com.jitesh.astronaut.cli;

import com.jitesh.astronaut.core.ScheduleManager;
import com.jitesh.astronaut.factory.TaskFactory;
import com.jitesh.astronaut.model.Task;
import com.jitesh.astronaut.observer.Observer;
import com.jitesh.astronaut.persistence.JsonPersistence;

import java.io.File;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CLI {
    private final ScheduleManager manager = ScheduleManager.getInstance();
    private final Scanner sc = new Scanner(System.in);

    public CLI() {
        manager.registerObserver(new Observer() {
            @Override
            public void update(String message) {
                System.out.println("[NOTIFY] " + message);
            }
        });
    }

    public void start() {
        System.out.println("=== Astronaut Scheduler CLI ===");
        System.out.println("Commands: add | list | remove | complete | save | load | exit");
        while (true) {
            System.out.print("> ");
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) break;
            try {
                if (line.equalsIgnoreCase("add")) {
                    System.out.print("Description: ");
                    String desc = sc.nextLine();
                    System.out.print("Start (HH:mm): ");
                    String st = sc.nextLine();
                    System.out.print("End (HH:mm): ");
                    String et = sc.nextLine();
                    Task t = TaskFactory.create(desc, st, et);
                    boolean ok = manager.addTask(t);
                    if (!ok) System.out.println("Add failed due to conflict.");
                } else if (line.equalsIgnoreCase("list")) {
                    manager.listTasks().forEach(System.out::println);
                } else if (line.startsWith("remove")) {
                    System.out.print("Task ID: ");
                    String id = sc.nextLine();
                    if (!manager.removeTask(id)) System.out.println("Not found.");
                } else if (line.equalsIgnoreCase("complete")) {
                    System.out.print("Task ID: ");
                    String id = sc.nextLine();
                    manager.markCompleted(id);
                } else if (line.equalsIgnoreCase("save")) {
                    System.out.print("Filename: ");
                    String fn = sc.nextLine();
                    try { JsonPersistence.save(manager.listTasks(), new File(fn)); System.out.println("Saved."); }
                    catch (Exception ex) { System.out.println("Save failed: " + ex.getMessage()); }
                } else if (line.equalsIgnoreCase("load")) {
                    System.out.print("Filename: ");
                    String fn = sc.nextLine();
                    try {
                        Task[] arr = JsonPersistence.load(new File(fn));
                        for (Task t : arr) manager.addTask(t);
                        System.out.println("Loaded tasks.");
                    } catch (Exception ex) { System.out.println("Load failed: " + ex.getMessage()); }
                } else {
                    System.out.println("Unknown command.");
                }
            } catch (DateTimeParseException dte) {
                System.out.println("Invalid time format. Use HH:mm");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Bye.");
    }
}