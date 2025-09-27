package co.unicauca.presentation.observer;
import java.util.LinkedList;
import java.util.List;
public abstract class ObservableBase {
    private List<iObserver> observers;
    public boolean hasNoObservers()
    {
        return (observers== null || observers.isEmpty());
    }
    public void addObserver(iObserver observer)
    {
        if(observers == null){
            observers = new LinkedList<>();
        }
        observers.add(observer);
    }
    public void notifyOnly(Class<? extends iObserver> observerClass, Object model)
    {
        if(observers == null) return;
        for (iObserver observer : observers) {
            if (observer.getClass().equals(observerClass)) {
                observer.validateNotification(this, model);
            }
        }
    }
    public void notifyObservers(Object model)
    {
        if (observers == null) return;
        for (iObserver observer : observers) {
            observer.validateNotification(this, model);
        }
    }
    public abstract void observersLoader();
}
