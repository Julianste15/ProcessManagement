package co.unicauca.presentation.observer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicBoolean;

class ObserverPatternTest {
    static class TestSubject extends ObservableBase {
        @Override public void observersLoader() { /* no-op */ }
        public void fire(Object model) { notifyObservers(model); }
    }
    static class TestObserver implements iObserver {
        final AtomicBoolean called = new AtomicBoolean(false);
        @Override public void validateNotification(ObservableBase s, Object m) { called.set(true); }
    }
    @Test
    void addObserverAndNotify() {
        TestSubject subject = new TestSubject();
        TestObserver obs = new TestObserver();
        assertTrue(subject.hasNoObservers());
        subject.addObserver(obs);
        subject.fire("ping");
        assertTrue(obs.called.get());
    }
}
