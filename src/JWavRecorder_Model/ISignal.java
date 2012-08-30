package JWavRecorder_Model;

import JWavRecorder_Model.Signal.LoadFromFile;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author alexcaranha
 */
public interface ISignal {    
    public XYSeries     getSignal();
    public Signal.State getState();
    
    public long getSampleRate();
    public void setSampleRate(long _sampleRate);
    
    public abstract boolean load(String fileName, LoadFromFile option);
}
