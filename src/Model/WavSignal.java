package Model;

import Vision.JWavRecorder;
import com.musicg.dsp.Resampler;
import com.musicg.wave.Wave;
import com.musicg.wave.WaveFileManager;
import com.musicg.wave.WaveHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javax.swing.Timer;
import org.jfree.data.xy.XYSeries;

/**
 *
 * @author alexcaranha
 */
public class WavSignal
        extends Signal
        implements IWavSignal {
    //--------------------------------------------------------------------------

    public static enum StateWavMode {
        FREE, READY_TO_LOADING, LOADING, PLAYING, READY_TO_RECORDING, RECORDING
    }
    //public static final String fileNameAudio8k = Menu.class.getResource("/Temp/audio8k.wav").getPath();
    public static final String fileNameAudio8k = System.getProperty("user.dir") + "/audio8k.wav";
    //--------------------------------------------------------------------------
    // Attributes
    private WavFile      _wavFile;
    private String       _pathWavFile;
    private TLoader      _loader;
    private StateWavMode _stateWavSignal;
    private XYSeries     _signalDecimated;
    private TRecorder    _recorder;
    //--------------------------------------------------------------------------
    // Constructor
    public WavSignal() {
        super("signal");

        setWavFile(null);
        setFullPath(fileNameAudio8k);        
        setStateWavSignal(StateWavMode.FREE);
        this._signalDecimated = new XYSeries("signal decimated by factor 5");
        this._loader = null;
    }

    public WavSignal(String fileName, String fullPath) {
        super(fileName);

        this._wavFile         = null;
        this._pathWavFile     = fullPath;
        this._loader          = null;
        this._stateWavSignal  = StateWavMode.FREE;
        this._signalDecimated = new XYSeries("signal decimated");
    }

    public static boolean isFileExists(String path) {
        //----------------------------------------------------------------------
        File soundFile = new File(path);
        boolean objReturn = soundFile.exists();
        //----------------------------------------------------------------------
        return (objReturn);
        //----------------------------------------------------------------------
    }
    //--------------------------------------------------------------------------
    // StateWavSignal
    public StateWavMode getStateWavSignal() {
        return _stateWavSignal;
    }

    public void setStateWavSignal(StateWavMode _stateWavSignal) {
        this._stateWavSignal = _stateWavSignal;
    }
    //--------------------------------------------------------------------------
    // Path
    @Override
    public String getFullPath() {
        return _pathWavFile;
    }

    @Override
    public void setFullPath(String path) {
        this._pathWavFile = path;
    }
    //--------------------------------------------------------------------------
    public static WavFile resampleAudio(int sampleRate,
            String pathWavFileIn,
            String pathWavFileOut) {

        try {
            Wave wave = new Wave(pathWavFileIn);
            // Resample to 
            Resampler resampler = new Resampler();
            int sourceRate = wave.getWaveHeader().getSampleRate();
            byte[] resampledWaveData = resampler.reSample(wave.getBytes(), wave.getWaveHeader().getBitsPerSample(), sourceRate, sampleRate);

            // Update the wave header
            WaveHeader resampledWaveHeader = wave.getWaveHeader();
            resampledWaveHeader.setSampleRate(sampleRate);

            // Make resampled wave
            Wave resampledWave = new Wave(resampledWaveHeader, resampledWaveData);

            WaveFileManager wfm = new WaveFileManager(resampledWave);
            wfm.saveWaveAsFile(pathWavFileOut);

            return WavFile.openWavFile(pathWavFileOut);

        } catch (IOException | WavFileException ex) {
            return null;
        }
    }
    //--------------------------------------------------------------------------
    // WavFile
    @Override
    public WavFile getWavFile() {
        return this._wavFile;
    }

    @Override
    public void setWavFile(WavFile wavFile) {
        this._wavFile = wavFile;
    }
    //--------------------------------------------------------------------------
    public static boolean load(String fileName, 
                               String fullPath, 
                               XYSeries signal, 
                               String error) {

        WavFile wavFile;

        try {
            signal.clear();
            // Open file
            wavFile = WavFile.openWavFile(fullPath);

            int framesRead;
            double[] buffer = new double[(int) wavFile.getSampleRate() / 10 * wavFile.getNumChannels()];

            int numChannels = wavFile.getNumChannels();
            long cont = -1;

            do {
                framesRead = wavFile.readFrames(buffer, (int) wavFile.getSampleRate() / 10);

                for (int sample = 0; sample < framesRead * numChannels; sample += numChannels) {
                    signal.add((double) ++cont / wavFile.getSampleRate(), buffer[sample]);
                }
            } while (framesRead != 0);

            wavFile.close();

        } catch (IOException | WavFileException ex) {
            error = ex.getMessage();
            signal.clear();
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String fileName, LoadFromFile option) {

        if (!WavSignal.isFileExists(fileName)){
            setError("File specified not exists.");
            return false;
        }
        
        if (option == LoadFromFile.CSV) {
            return super.load(fileName, option);
        }

        if (option != LoadFromFile.WAV) {
            return true;
        }

        try {
            _signal.clear();
            if (getWavFile() == null) {
                // Open file
                setWavFile(WavFile.openWavFile(getFullPath()));
            }
            setSampleRate(_wavFile.getSampleRate());

            int framesRead;
            double[] buffer = new double[(int) _wavFile.getSampleRate() / 10 * _wavFile.getNumChannels()];

            if (_state == State.FREE) {
                _state = State.LOCK;

                int numChannels = _wavFile.getNumChannels();
                long cont = -1;

                do {
                    framesRead = _wavFile.readFrames(buffer, (int) _wavFile.getSampleRate() / 10);

                    for (int sample = 0; sample < framesRead * numChannels; sample += numChannels) {
                        _signal.add((double) ++cont / _wavFile.getSampleRate(), buffer[sample]);
                    }
                } while (framesRead != 0);

                _wavFile.close();
                setState(State.FREE);
            }
        } catch (IOException | WavFileException ex) {
            setState(State.FREE);
            setWavFile(null);
            setError(ex.getMessage());
            _signal.clear();
            return false;
        }
        setState(State.FREE);
        return true;
    }
    //--------------------------------------------------------------------------
    public void loading(String pathWavFile) {
        this._loader = new TLoader(10);
        this._loader.start();
    }
    //--------------------------------------------------------------------------
    private double getPercent() {
        if (_wavFile == null) {
            return 0.0;
        }
        if (_wavFile.getNumFrames() == 0) {
            return 0.0;
        }

        return 100 - _wavFile.getFramesRemaining() * 100 / _wavFile.getNumFrames();
    }
    
    @Override
    public boolean readyToRecord(){
        boolean result;
        
        _recorder = new TRecorder();
        result = _recorder.readyToRecord();
        if (!result){
            _recorder = null;
        }
        return result;
    }
    
    @Override
    public void recordAudio() {
        if (_recorder != null){
            _stateWavSignal = StateWavMode.RECORDING;
            _recorder.startRecord();
        }
    }
    
    @Override
    public void playAudio() {
        _stateWavSignal = StateWavMode.PLAYING;
        
        Thread tocador = new TPlaying();
        tocador.start();
    }
        
    @Override
    public void stopProcess() {
        if (_stateWavSignal == StateWavMode.RECORDING){
            _recorder.stopRecord();
            _recorder = null;
            notifySpecificClassObservers("JWavRecorder", "State", JWavRecorder.State.LOAD);
            return;
        }
        
        this._stateWavSignal = StateWavMode.FREE;
        notifyObservers(JWavRecorder.BUTTON_STOP, true); 
    }
    
    @Override
    public boolean saveAudio(String fileName) {
        try {
            if (getWavFile() == null) {
                throw new Exception("Wavfile is null");
            }
            if (getFullPath().trim().length() <= 0) {
                throw new Exception("Wavfile not contains a path valid.");
            }

            Wave wave = new Wave(getFullPath());
            WaveFileManager wfm = new WaveFileManager(wave);

            wfm.saveWaveAsFile(fileName);
            setFullPath(fileName);
        } catch (Exception e) {
            setError(e.getMessage());
            return false;
        }
        return true;
    }
    
    @Override
    public String getInfo() {
        if (this._wavFile == null) {
            return "";
        }
        return this._wavFile.getInfo();
    }
    
    class TPlaying extends Thread {

        private final int EXTERNAL_BUFFER_SIZE = 256;
        ArrayList<Pair>   listData;
        int               iDataList;
        TPlayingPlotLine  line = new TPlayingPlotLine(10);
        
        @Override
        public void run() {
            //------------------------------------------------------------------
            File              soundFile;
            AudioInputStream  audioInputStream;
            DataLine.Info     info;
            AudioFormat       format;
            SourceDataLine    auline;
            int               nBytesRead;
            byte[]            abData;
            //------------------------------------------------------------------
            listData = new ArrayList();
            //------------------------------------------------------------------
            if (_pathWavFile.length() > 0) {

                try {
                    soundFile = new File(_pathWavFile);
                    audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                } catch (UnsupportedAudioFileException | IOException e) {
                    setError(e.getMessage());
                    return;
                }

                format = audioInputStream.getFormat();
                info   = new DataLine.Info(SourceDataLine.class, format);

                try {
                    auline = (SourceDataLine) AudioSystem.getLine(info);
                    auline.open(format);
                } catch (LineUnavailableException e) {
                    setError(e.getMessage());
                    return;
                } catch (Exception e) {
                    setError(e.getMessage());
                    return;
                }
                
                listData.clear();
                try {
                    while (true) {
                        abData = new byte[EXTERNAL_BUFFER_SIZE];
                        nBytesRead = audioInputStream.read(abData, 0, abData.length);
                        if (nBytesRead == -1) break;
                        listData.add(new Pair(abData, nBytesRead));
                    }
                } catch (IOException ex) {
                    setError(ex.getMessage());
                    listData.clear();
                }
                
                if (listData.size() > 0){
                    try{
                        this.line.start();
                        notifySpecificClassObservers("XYGraph", "Line", true);
                        
                        auline.start();
                        for (iDataList = 0 ; iDataList < listData.size() && _stateWavSignal == StateWavMode.PLAYING; iDataList += 1){
                            Pair pair = listData.get(iDataList);
                            abData     = (byte[]) pair.getOne();
                            nBytesRead = (int) pair.getTwo();
                            
                            auline.write(abData, 0, nBytesRead);
                        }
                    } catch(Exception ex) {
                        setError(ex.getMessage());
                        listData.clear();
                    } finally{
                        notifySpecificClassObservers("XYGraph", "Line", false);
                        this.line.stop();
                    }
                }
                listData.clear();
                
                auline.drain();
                auline.close();
                stopProcess();
            }
            //------------------------------------------------------------------
        }
        
        class TPlayingPlotLine extends Timer implements ActionListener {        
            TPlayingPlotLine(int interval) {
                super(interval, null);
                addActionListener(this);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                notifySpecificClassObservers("XYGraph", "Line", (double) iDataList * _signalDecimated.getMaxX() / listData.size());
            }
        }
    }    
    
    class TLoader extends Timer implements ActionListener {

        private int _framesRead;
        private double[] _buffer;
        private int _numChannels;
        private int _count;

        TLoader(int interval) {
            super(interval, null);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            switch (_stateWavSignal) {

                case READY_TO_LOADING:
                    _signal.clear();
                    _wavFile = null;
                    try {
                        //------------------------------------------------------
                        String error = null;
                        //------------------------------------------------------
                        setWavFile(resampleAudio(8000, getFullPath(), fileNameAudio8k));
                        setFullPath(fileNameAudio8k);
                        //------------------------------------------------------
                        _wavFile = WavFile.openWavFile(fileNameAudio8k);
                        _buffer = new double[(int) _wavFile.getSampleRate() / 10 * _wavFile.getNumChannels()];
                        _numChannels = _wavFile.getNumChannels();
                        //------------------------------------------------------
                    } catch (IOException | WavFileException ex) {
                        _error = ex.getMessage();
                        this.stop();
                        return;
                    }

                    _count = -1;
                    setStateWavSignal(StateWavMode.LOADING);
                    break;

                case LOADING:
                    setState(State.LOCK);
                    for (int i = 1; i <= 5; i++) {
                        try {
                            _framesRead = _wavFile.readFrames(_buffer, (int) _wavFile.getSampleRate() / 10);

                            if (_framesRead != 0) {
                                for (int sample = 0; sample < _framesRead * _numChannels; sample += _numChannels) {
                                    _signal.add((double) ++_count / _wavFile.getSampleRate(), _buffer[sample]);
                                }
                            } else {
                                _wavFile.close();
                                setStateWavSignal(StateWavMode.FREE);
                                
                                decimate(getSignal(), 5, _signalDecimated);
                                notifySpecificClassObservers("XYGraph", "XYSeries", _signalDecimated);
                                break;
                            }
                        } catch (IOException | WavFileException ex) {
                            _error = ex.getMessage();
                            this.stop();
                            return;
                        }
                    }

                    notifySpecificClassObservers("ProgressBar", getPercent());
                    break;

                case FREE:
                    setState(State.FREE);
                    //notifyObservers(null);
                    this.stop();
                    setStateWavSignal(StateWavMode.FREE);
                    break;
            }
        }
    }
    
    class Pair{
        private Object _one;
        private Object _two;
        
        public Pair(Object one, Object two){
            this._one = one;
            this._two = two;
        }
        
        public Object getOne(){
            return this._one;
        }
        
        public Object getTwo(){
            return this._two;
        }
    }
    
    public class TRecorder extends Thread {
        
        private TargetDataLine        m_line;
        private AudioFileFormat.Type  m_targetType;
        private AudioInputStream      m_audioInputStream;
        private File                  m_outputFile;

        private DataLine.Info         info;
        private AudioFormat           audioFormat;
        
        private final int SAMPLE_RATE = 8000; // Hz
        private final int RESOLUTION  = 16;   // BITS
        private final int CHANELS     = 1;    // MONO

        public TRecorder(){            
            m_outputFile  = new File(getFullPath());
        }
        
        public boolean readyToRecord(){
            audioFormat = new AudioFormat(SAMPLE_RATE,
                                          RESOLUTION,
                                          CHANELS,
                                          true,
                                          true);
            
            boolean result = true;
            
            info   = new DataLine.Info(TargetDataLine.class, audioFormat);
            m_line = null;
            
            try{
                m_line = (TargetDataLine) AudioSystem.getLine(info);
                m_line.open(audioFormat);
                m_audioInputStream = new AudioInputStream(m_line);
                m_targetType       = AudioFileFormat.Type.WAVE;
            } catch (LineUnavailableException e){
                setError("unable to get a recording line");
                result = false;
                m_line = null;
            }
            return result;
        }

        public void startRecord(){
            m_line.start();
            super.start();
        }

        public void stopRecord(){
            if (m_line != null){
                m_line.stop();
                m_line.close();
                m_line = null;
            }
        }

        @Override
        public void run(){
            try{
                AudioSystem.write(m_audioInputStream,
                                  m_targetType,
                                  m_outputFile);
            }catch (IOException e){
                e.printStackTrace();
            }
        }        
    }
}
