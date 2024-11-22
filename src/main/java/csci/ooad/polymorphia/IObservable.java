package csci.ooad.polymorphia;

import java.util.List;

public interface IObservable {
    default void attach(IObserver observer, EventType eventType) {
        EventBus.getInstance().attach(observer, eventType);
    }

    default void attach(IObserver observer, List<EventType> eventTypes) {
        for (EventType eventType : eventTypes) {
            attach(observer, eventType);
        }
    }

    default void detach(IObserver observer) {
        EventBus.getInstance().detach(observer);
    }
}
