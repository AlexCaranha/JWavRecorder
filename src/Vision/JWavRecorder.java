package Vision;

import Model.IObserver;
import Model.JWavRecorder_Model;
import Model.WavSignal;
import Vision.Components.FilterFileChooser;
import Vision.Components.ProgressBar;
import Vision.Components.XYGraph;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author alexcaranha
 */
public class JWavRecorder extends JComponent implements IObserver{
    //--------------------------------------------------------------------------
    private final String LABEL_RECORDING = "labelRecording";
    private final String PROGRESS_BAR    = "barraProgresso";
    private final String PANEL_GRAPH     = "panelGraph";
    private final String PANEL_BUTTONS   = "panelButtons";
    
    public static final String BUTTON_REC    = "buttonRec";
    public static final String BUTTON_OPEN   = "buttonOpen";
    public static final String BUTTON_STOP   = "buttonStop";
    public static final String BUTTON_PLAY   = "buttonPlay";
    public static final String BUTTON_SAVE   = "buttonSave";
    
    private Container containerFather = null;
    
    public static enum State{
        REC, PLAY, STOP, LOAD
    }
    //--------------------------------------------------------------------------
    private JWavRecorder_ActionListener   _objActionListener = null;
    private ArrayList<Component>          _components        = null;
    private WavSignal                     _wavSignal         = null;
    private JWavRecorder_Model            _model             = null;
    
    public JWavRecorder(int width, int height, Container containerFather){
        //----------------------------------------------------------------------
        this._model = new JWavRecorder_Model();
        this._model.insertObserver(this);
        
        this._wavSignal = new WavSignal();
        this._wavSignal.insertObserver(this);
        
        this.setLayout(null);
        this.setSize(width, height);
        this.containerFather = containerFather;
        
        _objActionListener = new JWavRecorder_ActionListener(this);
        //----------------------------------------------------------------------
        _components = new ArrayList();
        
        JLabel  objLabel;
        JPanel  objPanel;
        JButton objButton;
        //----------------------------------------------------------------------
        objPanel = new ProgressBar(0, 0, width, height);
        objPanel.setName(PROGRESS_BAR);
        objPanel.setVisible(false);
        _components.add(objPanel);
        _wavSignal.insertObserver((IObserver)objPanel);
                
        objPanel = new XYGraph("Waveform", "time (in seconds)", "amplitude", this._wavSignal.getSignal());
        objPanel.setName(PANEL_GRAPH);
        objPanel.setOpaque(true); 
        objPanel.setSize((int)(width * 4 / 5), height);
        objPanel.setLocation(0, 0);
        objPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        _components.add(objPanel);
        _wavSignal.insertObserver((IObserver)objPanel);
        
        objLabel = new JLabel("RECORDING ...", JLabel.CENTER);
        objLabel.setName(LABEL_RECORDING);
        objLabel.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 30));
        objLabel.setForeground(Color.RED);
        objLabel.setOpaque(true); 
        objLabel.setSize((int)(width * 4 / 5), height);
        objLabel.setLocation(0, 0);
        objLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        _components.add(objLabel);
        //----------------------------------------------------------------------
        objPanel = new JPanel();
        objPanel.setLayout(null);
        objPanel.setName(PANEL_BUTTONS);
        objPanel.setOpaque(true); 
        objPanel.setSize((int)(width * 1 / 5), height);
        objPanel.setLocation((int)(width * 4 / 5), 0);
        objPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        _components.add(objPanel);
        
        int iButton      = 0;
        int qtdButtons   = 6;
        int whiteSpace   = objPanel.getHeight() / 50;
        int heightButton = (int)((objPanel.getHeight() - qtdButtons * whiteSpace) / (qtdButtons - 1));
        
        objButton = new JButton();
        iButton += 1;
        objButton.setName(BUTTON_REC);
        objButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        objButton.setText("Rec");
        objButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        objButton.setHorizontalAlignment(SwingConstants.CENTER);
        objButton.setVerticalAlignment(SwingConstants.CENTER);
        objButton.setOpaque(true);
        objButton.setSize((int)(objPanel.getWidth() * 90 / 100), 
                          heightButton);
        objButton.setLocation((int)(objPanel.getWidth() * 5 / 100), 
                              iButton * whiteSpace + (iButton-1)*heightButton);
        objButton.addActionListener(_objActionListener);
        objPanel.add(objButton);
        
        
        objButton = new JButton();
        iButton += 1;
        objButton.setName(BUTTON_OPEN);
        objButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        objButton.setText("Open");
        objButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        objButton.setHorizontalAlignment(SwingConstants.CENTER);
        objButton.setVerticalAlignment(SwingConstants.CENTER);
        objButton.setOpaque(true); 
        objButton.setSize((int)(objPanel.getWidth() * 90 / 100), 
                          heightButton);
        objButton.setLocation((int)(objPanel.getWidth() * 5 / 100), 
                              iButton * whiteSpace + (iButton-1)*heightButton);
        objButton.addActionListener(_objActionListener);
        objPanel.add(objButton);
        
        objButton = new JButton();
        iButton += 1;
        objButton.setName(BUTTON_SAVE);
        objButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        objButton.setText("Save");
        objButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        objButton.setHorizontalAlignment(SwingConstants.CENTER);
        objButton.setVerticalAlignment(SwingConstants.CENTER);
        objButton.setOpaque(true); 
        objButton.setSize((int)(objPanel.getWidth() * 90 / 100), 
                          heightButton);
        objButton.setLocation((int)(objPanel.getWidth() * 5 / 100), 
                              iButton * whiteSpace + (iButton-1)*heightButton);
        objButton.addActionListener(_objActionListener);
        objPanel.add(objButton);
        
        objButton = new JButton();
        iButton += 1;
        objButton.setName(BUTTON_PLAY);
        objButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        objButton.setText("Play");
        objButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        objButton.setHorizontalAlignment(SwingConstants.CENTER);
        objButton.setVerticalAlignment(SwingConstants.CENTER);
        objButton.setOpaque(true); 
        objButton.setSize((int)(objPanel.getWidth() * 90 / 100), 
                          heightButton);
        objButton.setLocation((int)(objPanel.getWidth() * 5 / 100), 
                              iButton * whiteSpace + (iButton-1)*heightButton);
        objButton.addActionListener(_objActionListener);
        objPanel.add(objButton);
        
        objButton = new JButton();
        iButton += 1;
        objButton.setName(BUTTON_STOP);
        objButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        objButton.setText("Stop");
        objButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        objButton.setHorizontalAlignment(SwingConstants.CENTER);
        objButton.setVerticalAlignment(SwingConstants.CENTER);
        objButton.setOpaque(true); 
        objButton.setSize((int)(objPanel.getWidth() * 90 / 100), 
                          heightButton);
        objButton.setLocation((int)(objPanel.getWidth() * 5 / 100), 
                              iButton * whiteSpace + (iButton-1)*heightButton);
        objButton.addActionListener(_objActionListener);
        objPanel.add(objButton);
        //----------------------------------------------------------------------        
        addComponents();
        //----------------------------------------------------------------------
    }
    
    public Container getContainerFather(){
        return containerFather;
    }
    
    private void addComponents(){
        if (_components.isEmpty()) return;
        
        for (Component objComponent : _components) {
            this.add(objComponent);
        }
    }
    
    private Component getComponentByName(String name, Container source){
        if (source == null) 
            return null;
        
        for (Component objComponent : source.getComponents()){
            if (objComponent.getName() != null)
            if (objComponent.getName().compareTo(name) == 0)
                return objComponent;
            
            if (objComponent instanceof Container) {
                Component objReturned = getComponentByName(name, (Container) objComponent);
                if (objReturned != null) 
                    return objReturned;
            }
                
        }
        return null;
    }
    
    private Component getComponentByName(String name){
        if (_components == null) 
            return null;
        
        if (_components.isEmpty()) 
            return null;
        
        for (Component objComponent : _components) {
            if (objComponent.getName().compareTo(name) == 0)
                return objComponent;
            
            if (objComponent instanceof Container) {
                Component objComponentSearch = getComponentByName(name, (Container) objComponent);
                if (objComponentSearch != null)
                    return objComponentSearch;
            }
        }   
        return null;
    }
    
    @Override
    public void update(Object ... parameters) {
        if (parameters == null) return;
        if (parameters.length > 2) return;
        
        JButton objButton;
        
        if (((String) parameters[0]).equalsIgnoreCase(BUTTON_REC)){
            objButton = (JButton) getComponentByName((String) parameters[0]);            
            if (objButton != null){
                objButton.setEnabled((boolean) parameters[1]);
            }
            
            JPanel panel = (JPanel) getComponentByName(PANEL_GRAPH);
            if (panel != null) {
                panel.setVisible(objButton.isEnabled());
            }
            
            JButton objButtonOpen = (JButton) getComponentByName(BUTTON_OPEN);
            if (objButtonOpen != null){                
                objButtonOpen.setEnabled(objButton.isEnabled());
            }
            JButton objButtonSave = (JButton) getComponentByName(BUTTON_SAVE);
            if (objButtonSave != null){                
                objButtonSave.setEnabled(objButton.isEnabled());
            }
            JButton objButtonPlay = (JButton) getComponentByName(BUTTON_PLAY);
            if (objButtonPlay != null){                
                objButtonPlay.setEnabled(objButton.isEnabled());
            }
        }else
        if (((String) parameters[0]).equalsIgnoreCase(BUTTON_PLAY)){
            objButton = (JButton) getComponentByName((String) parameters[0]);
            if (objButton != null){
                objButton.setEnabled((boolean) parameters[1]);
            }
            JButton objButtonRec = (JButton) getComponentByName(BUTTON_REC);
            if (objButtonRec != null){                
                objButtonRec.setEnabled(objButton.isEnabled());
            }
            JButton objButtonOpen = (JButton) getComponentByName(BUTTON_OPEN);
            if (objButtonOpen != null){                
                objButtonOpen.setEnabled(objButton.isEnabled());
            }
            JButton objButtonSave = (JButton) getComponentByName(BUTTON_SAVE);
            if (objButtonSave != null){                
                objButtonSave.setEnabled(objButton.isEnabled());
            }
        }else
        if (((String) parameters[0]).equalsIgnoreCase(BUTTON_STOP)){
            objButton = (JButton) getComponentByName((String) parameters[0]);
            if (objButton != null){
                objButton.setEnabled((boolean) parameters[1]);
            }
            
            JPanel panel = (JPanel) getComponentByName(PANEL_GRAPH);
            if (panel != null) {
                panel.setVisible(objButton.isEnabled());
            }
            
            JButton objButtonRec = (JButton) getComponentByName(BUTTON_REC);
            if (objButtonRec != null){                
                objButtonRec.setEnabled(objButton.isEnabled());
            }
            JButton objButtonOpen = (JButton) getComponentByName(BUTTON_OPEN);
            if (objButtonOpen != null){                
                objButtonOpen.setEnabled(objButton.isEnabled());
            }
            JButton objButtonSave = (JButton) getComponentByName(BUTTON_SAVE);
            if (objButtonSave != null){                
                objButtonSave.setEnabled(objButton.isEnabled());
            }
            JButton objButtonPlay = (JButton) getComponentByName(BUTTON_PLAY);
            if (objButtonPlay != null){                
                objButtonPlay.setEnabled(objButton.isEnabled());
            }
        }else
        if (((String) parameters[0]).equalsIgnoreCase("State")){
            if ((State) parameters[1] == State.LOAD){
                loadSignalFromFile();
            }
        }
    }
    
    private void loadSignalFromFile(){
        ProgressBar progressBar = (ProgressBar) getComponentByName(PROGRESS_BAR);
        if (progressBar == null) return;
        progressBar.setVisible(true);

        _wavSignal.setStateWavSignal(WavSignal.StateWavMode.READY_TO_LOADING);
        _wavSignal.loading(_wavSignal.getFullPath());
    }
    
    private class JWavRecorder_ActionListener implements ActionListener
    {
        private JWavRecorder objMainClass;
        private JFileChooser objFileChooser;
        
        public JWavRecorder_ActionListener(JWavRecorder objMainClass){
            this.objMainClass = objMainClass;
            
            initializeFileChooser();
        }
        
        private void initializeFileChooser(){
            this.objFileChooser = new JFileChooser();
            
            FileFilter[] arrayFileFilters = this.objFileChooser.getChoosableFileFilters();
            for (FileFilter objFileFilter : arrayFileFilters) {
                this.objFileChooser.removeChoosableFileFilter(objFileFilter);
            }
            this.objFileChooser.addChoosableFileFilter(new FilterFileChooser("WAV", "WAV Audio File"));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            Component objSource = (Component) e.getSource();
            
            if (objSource.getName().equalsIgnoreCase(BUTTON_REC)){
                _model.setState(State.REC);
                _model.notifyObservers(BUTTON_REC, false);
                btnRec_ActionListener();
                return;
            }
            if (objSource.getName().equalsIgnoreCase(BUTTON_OPEN)){
                btnOpen_ActionListener();
                return;
            }
            if (objSource.getName().equalsIgnoreCase(BUTTON_SAVE)){
                btnSave_ActionListener();
                return;
            }
            if (objSource.getName().equalsIgnoreCase(BUTTON_PLAY)){
                btnPlay_ActionListener();
                return;
            }
            if (objSource.getName().equalsIgnoreCase(BUTTON_STOP)){                
                btnStop_ActionListener();
            }
        }
        
        private void btnRec_ActionListener(){
            if(_wavSignal == null) return;
            if(_wavSignal.getFullPath().trim().length() == 0) return;
                        
            if (_wavSignal.readyToRecord()){
                _wavSignal.recordAudio();
                _model.setState(State.REC);
                _model.notifyObservers(BUTTON_REC, false);
            }else{
                String message[] = {"Ok"};
                
                VFunctions.showMessage(objMainClass.getContainerFather(), 
                                       _wavSignal.getError(), 
                                       "Error", 
                                       JOptionPane.YES_OPTION,
                                       JOptionPane.INFORMATION_MESSAGE, 
                                       null,
                                       message, 
                                       "Ok");
                _wavSignal.clearError();
            }
        }
        
        private void btnOpen_ActionListener(){            
            int    value;
            File   fileSelected;
            
            value = this.objFileChooser.showOpenDialog(objMainClass);

            if (value == JFileChooser.APPROVE_OPTION){
                fileSelected = objFileChooser.getSelectedFile();
                
                if (fileSelected == null) return;
                if (!WavSignal.isFileExists(fileSelected.getPath())) return;
                
                _wavSignal.setFullPath(fileSelected.getAbsolutePath());
                _wavSignal.getSignal().setKey(fileSelected.getName());
            }else return;
            //------------------------------------------------------------------            
            loadSignalFromFile();
            //------------------------------------------------------------------
        }
        
        private void btnPlay_ActionListener(){
            boolean validation = true;
                    
            if(_wavSignal == null) validation = false;
            else
            if(_wavSignal.getFullPath().trim().length() == 0) validation = false;
            else
            if(_wavSignal.getSignal() == null) validation = false;
            else
            if(_wavSignal.getSignal().getItemCount() == 0) validation = false;
            else
            if (!WavSignal.isFileExists(_wavSignal.getFullPath())) validation = false;
            
            if (!validation){
                String message[] = {"Ok"};
                
                VFunctions.showMessage(objMainClass.getContainerFather(), 
                                       "There is no signal informated.", 
                                       "Information", 
                                       JOptionPane.YES_OPTION,
                                       JOptionPane.INFORMATION_MESSAGE, 
                                       null,
                                       message, 
                                       "Ok");
                return;
            }
            
            _wavSignal.playAudio();
            _model.setState(State.PLAY);
            _model.notifyObservers(BUTTON_PLAY, false);
        }
        
        private void btnStop_ActionListener(){
            _model.setState(State.STOP);
            _model.notifyObservers(BUTTON_STOP, true);
                        
            if(_wavSignal == null) return;
            if(_wavSignal.getFullPath().trim().length() == 0) return;
            
            _wavSignal.stopProcess();
        }
        
        private void btnSave_ActionListener(){
            if(_wavSignal == null) return;
            if(_wavSignal.getFullPath().trim().length() == 0) return;
            
            int value = this.objFileChooser.showSaveDialog(objMainClass);

            if (value == JFileChooser.APPROVE_OPTION){
                File fileSelected = objFileChooser.getSelectedFile();
                String message[] = {"Ok"};
                
                if (fileSelected == null) return;
                
                if (_wavSignal.saveAudio(fileSelected.getAbsolutePath())){
                    VFunctions.showMessage(objMainClass.getContainerFather(), 
                                        "File saved successfully.", 
                                        "Information", 
                                        JOptionPane.YES_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE, 
                                        null,
                                        message, 
                                        "Ok");
                }
                else
                {
                    VFunctions.showMessage(objMainClass.getContainerFather(), 
                                           _wavSignal.getError(), 
                                           "ERRO", 
                                           JOptionPane.YES_OPTION,
                                           JOptionPane.INFORMATION_MESSAGE, 
                                           null,
                                           message, 
                                           "Ok");                    
                    _wavSignal.clearError();
                }
            }
        }
    }
    //----------------------------------------------------------------------
}
