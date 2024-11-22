package csci.ooad.polymorphia;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class PolymorphiaEventAppender extends AppenderBase<ILoggingEvent> {

    private ConcurrentMap<String, ILoggingEvent> eventMap
            = new ConcurrentHashMap<>();

    @Override
    protected void append(ILoggingEvent event) {
        eventMap.put(String.valueOf(System.currentTimeMillis()), event);
    }

    public Map<String, ILoggingEvent> getEventMap() {
        return eventMap;
    }

}
