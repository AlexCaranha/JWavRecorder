package JWavRecorder_Model;

/**
 *
 * @author alexcaranha
 */
public interface IObservable {
    public void insertObserver(IObserver observer);
    public void removeObserver(IObserver observer);
    
    public void notifyObservers(Object ... parameter);
    public void notifySpecificObserver(String name, Object ... parameter);
    public void notifySpecificClassObservers(String name, Object ... parameters);
}
