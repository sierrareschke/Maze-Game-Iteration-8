package csci.ooad.polymorphia.observer;

import csci.ooad.polymorphia.IObserver;

import java.util.ArrayList;
import java.util.List;

public class TestObserver implements IObserver {
    public List<String> events = new ArrayList<>();

    @Override
    public void update(String eventDescription) {
        events.add(eventDescription);
        System.out.println(eventDescription);
    }

    public int numberOfEventsReceived() {
        return events.size();
    }
}
