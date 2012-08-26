package Model;

import com.csvreader.CsvReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author alexcaranha
 */
public class Signal implements ISignal, IObservable, IError{
    //--------------------------------------------------------------------------
    // Attributes
    protected XYSeries  _signal;
    protected long      _sampleRate;
    protected State     _state;
    protected String    _error;
    protected Type      _type;
    protected ArrayList _observers;
    //--------------------------------------------------------------------------
    // Constructor
    public Signal(String key){
        this._signal       = new XYSeries(key);
        this._sampleRate   = 0;
        this._state        = State.FREE;
        this._error        = "";
        this._observers    = new ArrayList();
        this._type         = Type.NORMAL;
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
    //--------------------------------------------------------------------------    
    public static enum LoadFromFile{
        CSV, WAV, MIDI
    }
    
    public static enum State {
        FREE,
        LOCK
    };
        
    public static enum Type {
        NORMAL,
        PLOT
    };
    //--------------------------------------------------------------------------
    public String getError() {
        return _error;
    }

    public void setError(String _error) {
        this._error = _error;
    }
    
    public void clearError() {
        this._error = "";
    }
    //--------------------------------------------------------------------------
    // Type
    public Type getType() {
        return _type;
    }

    public void setType(Type _type) {
        this._type = _type;
    }
    //--------------------------------------------------------------------------
    @Override
    public long getSampleRate() {
        return _sampleRate;
    }

    @Override
    public void setSampleRate(long _sampleRate) {
        this._sampleRate = _sampleRate;
    }
    
    // Series
    @Override
    public XYSeries getSignal(){
        return this._signal;
    }
    
    public void setSignal(XYSeries signal){
        this._signal = signal;
    }
    //--------------------------------------------------------------------------
    // State
    @Override
    public State getState(){
        return this._state;
    }
    
    public void setState(State state){
        this._state = state;
    }
    //--------------------------------------------------------------------------
    @Override
    public boolean load(String fileName, LoadFromFile option) {        
        if (option != LoadFromFile.CSV)return true;
        
        double valueX, valueY;
        
        _signal.clear();
        try {
            CsvReader data = new CsvReader(fileName);

            if (data.getHeaderCount() > 0)
                data.readHeaders();
            
            while (data.readRecord()) {
                valueX = Double.valueOf(data.get(0));
                valueY = Double.valueOf(data.get(1));

                _signal.add(valueX, valueY);
            }                        
            data.close();

        } catch (FileNotFoundException e) {
            _error = e.getMessage();
        } catch (IOException e) {
            _error = e.getMessage();
        }
        return isErrorExists();
    }
    //--------------------------------------------------------------------------
    // ErrorMessage
    @Override
    public String getErrorMessage() {
        return (this._error);
    }

    @Override
    public boolean isErrorExists() {
        return (_error.trim().length() > 0);
    }
    //--------------------------------------------------------------------------
    public static void decimate(XYSeries signal, int n, XYSeries signalDecimated){
        if (signal == null) return;
        if (signalDecimated == null) return;
        if (n == 0) return;
        
        signalDecimated.clear();
        List<XYDataItem> dataList = signal.getItems();
        for (int i = 0; i < signal.getItemCount(); i += 1){
            if (i % n == 0){
                signalDecimated.add(dataList.get(i));
            }
        }
    }
    //--------------------------------------------------------------------------
}
