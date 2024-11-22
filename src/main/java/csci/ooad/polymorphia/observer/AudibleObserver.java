package csci.ooad.polymorphia.observer;

import csci.ooad.polymorphia.EventType;
import csci.ooad.polymorphia.IObservable;
import csci.ooad.polymorphia.IObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AudibleObserver implements IObserver {
    private static final Logger logger = LoggerFactory.getLogger(AudibleObserver.class);
    private static Synthesizer synthesizer;

    // reference: https://www.geeksforgeeks.org/converting-text-speech-java/
    static {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        try {
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            // Create a Synthesizer
            synthesizer = Central.createSynthesizer(
                    new SynthesizerModeDesc(Locale.US)
            );
            // Allocate synthesizer
            synthesizer.allocate();
        } catch (EngineException e) {
            logger.error("Could not initialize the synthesizer: {}", e.getMessage());
            logger.warn("Falling back to using the say command, if on a Mac, otherwise I'll be silent");
        }
    }

    public AudibleObserver(IObservable observableGame, List<EventType> interestingEvents) {
        for (EventType eventType : interestingEvents) {
            observableGame.attach(this, eventType);
        }
    }

    public AudibleObserver(IObservable observableGame, EventType eventType) {
        observableGame.attach(this, eventType);
    }


    @Override
    public void update(String eventDescription) {
        speak(eventDescription);
    }

    private void speak(String message) {
        try {
            if (synthesizer == null && System.getProperty("os.name").contains("Mac")) {
                String[] cmd = {"say", message};
                Process sayProcess = Runtime.getRuntime().exec(cmd);
                sayProcess.waitFor();
            } else {
                synthesizer.speakPlainText(message, null);
                synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Could not speak message: {}", message);
        }
    }

}
