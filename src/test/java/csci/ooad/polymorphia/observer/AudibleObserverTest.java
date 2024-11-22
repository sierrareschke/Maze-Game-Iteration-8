package csci.ooad.polymorphia.observer;

import csci.ooad.polymorphia.EventType;
import csci.ooad.polymorphia.IObservable;
import csci.ooad.polymorphia.IObserver;
import org.junit.jupiter.api.Disabled;

class AudibleObserverTest {

    @Disabled
    void update() {
        IObservable observable = new IObservable() {
            @Override
            public void attach(IObserver observer, EventType eventType) {
                IObservable.super.attach(observer, eventType);
            }
        };
        AudibleObserver audibleObserver = new AudibleObserver(observable, EventType.FightOutcome);
        audibleObserver.update("AudibleObserver has been notified");
    }

}