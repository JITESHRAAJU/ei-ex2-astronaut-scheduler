package com.jitesh.astronaut.observer;

import java.util.ArrayList;
import java.util.List;

public class Notifier {
    private final List<Observer> observers = new ArrayList<>();
    public void register(Observer o) { observers.add(o); }
    public void notifyAllObservers(String message) {
        for (Observer o : observers) o.update(message);
    }
}