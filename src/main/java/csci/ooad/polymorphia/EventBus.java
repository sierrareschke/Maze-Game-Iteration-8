package csci.ooad.polymorphia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EventBus {
    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);

    Map<EventType, List<IObserver>> observerMap = new HashMap<>(Map.of(EventType.All, new ArrayList<>()));

    private static final EventBus instance;

    // Eagerly initialized singleton with a static block (so that I can put a breakpoint on the code)
    //    private static EventBus instance;
    static {
        instance = new EventBus();
    }

    private EventBus() {
    }

    public static EventBus getInstance() {
        return instance;
    }

    public void attach(IObserver observer, EventType eventType) {
        if (!observerMap.containsKey(eventType)) {
            observerMap.put(eventType, new ArrayList<>());
        }
        observerMap.get(eventType).add(observer);
    }

    public void detach(IObserver observer) {
        for (EventType eventType : observerMap.keySet()) {
            observerMap.get(eventType).remove(observer);
        }
    }

    public void postMessage(EventType eventType, String eventDescription) {
        Set<IObserver> observersToNotify = new HashSet<>(observerMap.get(EventType.All));
        observersToNotify.addAll(observerMap.getOrDefault(eventType, new ArrayList<>()));

        for (IObserver observer : observersToNotify) {
            observer.update(eventDescription);
        }
    }

    static public void post(EventType eventType, String eventDescription) {
        logger.info("{}: {}", eventType, eventDescription);
        getInstance().postMessage(eventType, eventDescription);
    }
}
