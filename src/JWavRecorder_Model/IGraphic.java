/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JWavRecorder_Model;

/**
 *
 * @author alexcaranha
 */
public interface IGraphic {
    
    public static enum State {
        DefaultXYDataset
    };
    
    public Object returnDataSet(IGraphic.State state);
}
