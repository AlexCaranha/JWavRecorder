package JWavRecorder_Model;

import JWavRecorder_Vision.JWavRecorder;
import JWavRecorder_Vision.JWavRecorder.State;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author alexcaranha
 */
public class JWavRecorder_Model implements IObservable{
    private JWavRecorder.State _state;
    protected ArrayList        _observers;
    
    public JWavRecorder_Model(){
        this._state     = JWavRecorder.State.STOP;
        this._observers = new ArrayList();
    }
    
    public State getState() {
        return _state;
    }

    public void setState(State _state) {
        this._state = _state;
    }

    @Override
    public void insertObserver(IObserver observer) {
        this._observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        this._observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object ... parameters) {
        Iterator<IObserver> iterator = _observers.iterator();
        while(iterator.hasNext()){
            IObserver observer = iterator.next();
            
            observer.update(parameters);
        }
    }

    @Override
    public void notifySpecificObserver(String name, Object ... parameters) {
        Iterator<IObserver> iterator = _observers.iterator();
        while (iterator.hasNext()) {
            IObserver observer = iterator.next();
            if (observer.getName().equalsIgnoreCase(name)){
                observer.update(this, parameters);
            }
        }
    }
    
    @Override
    public void notifySpecificClassObservers(String name, Object ... parameters) {        
        Iterator<IObserver> iterator = _observers.iterator();
        while (iterator.hasNext()) {
            IObserver observer = iterator.next();
            if (observer.getClass().getCanonicalName().contains(name)){
                observer.update(parameters);
            }
        }
    }
}
