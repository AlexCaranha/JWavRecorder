/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JWavRecorder_Model;

/**
 *
 * @author alexcaranha
 */
public interface IWavSignal {
    public boolean readyToRecord();
    public void    recordAudio();
    public void    playAudio();
    public void    stopProcess();
    public boolean saveAudio(String fileName);
    
    public String getInfo();
    
    public String getFullPath();
    public void   setFullPath(String path);
    
    public WavFile getWavFile();
    public void    setWavFile(WavFile wavFile);
}
