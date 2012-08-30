package JWavRecorder_Vision;

import JWavRecorder_Model.IObserver;
import JWavRecorder_Model.JWavRecorder_Model;
import JWavRecorder_Model.WavSignal;
import JWavRecorder_Vision.Components.FilterFileChooser;
import JWavRecorder_Vision.Components.ProgressBar;
import JWavRecorder_Vision.Components.XYGraph;
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
    private final String PROGRESS_BAR    = "progressBar";
    private final String PANEL_GRAPH     = "panelGraph";
    private final String PANEL_BUTTONS   = "panelButtons";
    
    public static final String BUTTON_REC    = "buttonRec";
    public static final String BUTTON_OPEN   = "buttonOpen";
    public static final String BUTTON_INFO   = "buttonInfo";
    public static final String BUTTON_STOP   = "buttonStop";
    public static final String BUTTON_PLAY   = "buttonPlay";
    public static final String BUTTON_SAVE   = "buttonSave";
    
    public static final String BUTTON_REC_TOOLTIP    = "Record a wav signal from microphone.";
    public static final String BUTTON_OPEN_TOOLTIP   = "Open signal from wav file.";
    public static final String BUTTON_INFO_TOOLTIP   = "Show informations about the wav file.";
    public static final String BUTTON_STOP_TOOLTIP   = "Stop record/play audio signal.";
    public static final String BUTTON_PLAY_TOOLTIP   = "Play wav file.";
    public static final String BUTTON_SAVE_TOOLTIP   = "Save wav file.";
    
    public static enum State{
        REC, PLAY, STOP, LOAD
    }
    //--------------------------------------------------------------------------
    private Container containerFather = null;
    
    private JWavRecorder_ActionListener   _objActionListener = null;
    private ArrayList<Component>          _components        = null;
    private WavSignal                     _wavSignal         = null;
    private JWavRecorder_Model            _model             = null;
    
    public JWavRecorder(int width, int height, Container containerFather/*, 
                         JWavRecorder_Model model*/){
        //----------------------------------------------------------------------
        
        this._model = new JWavRecorder_Model();
        this._model.insertObserver(this);
        
        //this._model = model;
        
        this._wavSignal = new WavSignal();
        this._wavSignal.insertObserver(this);
        
        this.setLayout(null);
        this.setSize(width, height);
        this.containerFather = containerFather;
        
        _objActionListener = new JWavRecorder_ActionListener(this);
        //----------------------------------------------------------------------
        _components = new ArrayList();
        
        JLabel  objLabel;
        JPanel  objPanel, objPanelButtons, objPanelGraph;
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
        objPanel.setSize((int)(width * 9 / 10), height);
        objPanel.setLocation(0, 0);
        objPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        _components.add(objPanel);
        _wavSignal.insertObserver((IObserver)objPanel);
        
        objLabel = new JLabel();
        objLabel.setName(LABEL_RECORDING);
        objLabel.setText("RECORDING ...");
        objLabel.setHorizontalAlignment(SwingConstants.CENTER);
        objLabel.setVerticalAlignment(SwingConstants.CENTER);
        objLabel.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 30));
        objLabel.setForeground(Color.RED);
        objLabel.setOpaque(true); 
        objLabel.setSize((int)(width * 9 / 10), height);
        objLabel.setLocation(0, 0);
        objLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        _components.add(objLabel);
        //----------------------------------------------------------------------
        objPanelButtons = new JPanel();
        objPanelButtons.setLayout(null);
        objPanelButtons.setName(PANEL_BUTTONS);
        objPanelButtons.setOpaque(true); 
        objPanelButtons.setSize((int)(width * 1 / 10), height);
        objPanelButtons.setLocation((int)(width * 9 / 10), 0);
        objPanelButtons.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        _components.add(objPanelButtons);
        
        int iButton      = 0;
        int qtdButtons   = 6;
        int whiteSpace   = objPanel.getHeight() / 50;
        int heightButton = (int)((objPanelButtons.getHeight() - (qtdButtons + 1) * whiteSpace) / (qtdButtons));        
   
        iButton += 1;
        objButton = new JButton();
        objButton.setName(BUTTON_REC);
        objButton.setToolTipText(BUTTON_REC_TOOLTIP);
        objButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        objButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        objButton.setSize((int)(objPanelButtons.getWidth() * 90 / 100), heightButton);
        objButton.setLocation((int)(objPanelButtons.getWidth() * 5 / 100), 
                              iButton * whiteSpace + (iButton-1) * heightButton);
        objButton.setIcon(VFunctions.getImageRescaledSquare("/JWavRecorder_Figures/microphone.png", objButton.getSize()));
        objButton.addActionListener(_objActionListener);
        objPanelButtons.add(objButton);
                
        objButton = new JButton();
        iButton += 1;
        objButton.setName(BUTTON_OPEN);
        objButton.setToolTipText(BUTTON_OPEN_TOOLTIP);
        objButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        objButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        objButton.setHorizontalAlignment(SwingConstants.CENTER);
        objButton.setVerticalAlignment(SwingConstants.CENTER);
        objButton.setSize((int)(objPanelButtons.getWidth() * 90 / 100), heightButton);
        objButton.setLocation((int)(objPanelButtons.getWidth() * 5 / 100), 
                              iButton * whiteSpace + (iButton-1) * heightButton);
        objButton.setIcon(VFunctions.getImageRescaledSquare("/JWavRecorder_Figures/open.png", objButton.getSize()));
        objButton.addActionListener(_objActionListener);
        objPanelButtons.add(objButton);
        
        objButton = new JButton();
        iButton += 1;
        objButton.setName(BUTTON_INFO);
        objButton.setToolTipText(BUTTON_INFO_TOOLTIP);
        objButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        objButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        objButton.setHorizontalAlignment(SwingConstants.CENTER);
        objButton.setVerticalAlignment(SwingConstants.CENTER);
        objButton.setSize((int)(objPanelButtons.getWidth() * 90 / 100), heightButton);
        objButton.setLocation((int)(objPanelButtons.getWidth() * 5 / 100), 
                              iButton * whiteSpace + (iButton-1) * heightButton);
        objButton.setIcon(VFunctions.getImageRescaledSquare("/JWavRecorder_Figures/info.png", objButton.getSize()));
        objButton.addActionListener(_objActionListener);
        objPanelButtons.add(objButton);
        
        objButton = new JButton();
        iButton += 1;
        objButton.setName(BUTTON_SAVE);
        objButton.setToolTipText(BUTTON_SAVE_TOOLTIP);
        objButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        objButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        objButton.setHorizontalAlignment(SwingConstants.CENTER);
        objButton.setVerticalAlignment(SwingConstants.CENTER);
        objButton.setSize((int)(objPanelButtons.getWidth() * 90 / 100), 
                          heightButton);
        objButton.setLocation((int)(objPanelButtons.getWidth() * 5 / 100), 
                              iButton * whiteSpace + (iButton-1) * heightButton);
        objButton.setIcon(VFunctions.getImageRescaledSquare("/JWavRecorder_Figures/save.png", objButton.getSize()));
        objButton.addActionListener(_objActionListener);
        objPanelButtons.add(objButton);
        
        objButton = new JButton();
        iButton += 1;
        objButton.setName(BUTTON_PLAY);
        objButton.setToolTipText(BUTTON_PLAY_TOOLTIP);
        objButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        objButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        objButton.setHorizontalAlignment(SwingConstants.CENTER);
        objButton.setVerticalAlignment(SwingConstants.CENTER);
        objButton.setSize((int)(objPanelButtons.getWidth() * 90 / 100), heightButton);
        objButton.setLocation((int)(objPanelButtons.getWidth() * 5 / 100), 
                              iButton * whiteSpace + (iButton-1) * heightButton);
        objButton.setIcon(VFunctions.getImageRescaledSquare("/JWavRecorder_Figures/play.png", objButton.getSize()));
        objButton.addActionListener(_objActionListener);
        objPanelButtons.add(objButton);
        
        objButton = new JButton();
        iButton += 1;
        objButton.setName(BUTTON_STOP);
        objButton.setToolTipText(BUTTON_STOP_TOOLTIP);
        objButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        objButton.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 20));
        objButton.setHorizontalAlignment(SwingConstants.CENTER);
        objButton.setVerticalAlignment(SwingConstants.CENTER);
        objButton.setSize((int)(objPanelButtons.getWidth() * 90 / 100), heightButton);
        objButton.setLocation((int)(objPanelButtons.getWidth() * 5 / 100), 
                              iButton * whiteSpace + (iButton-1) * heightButton);
        objButton.setIcon(VFunctions.getImageRescaledSquare("/JWavRecorder_Figures/stop.png", objButton.getSize()));
        objButton.addActionListener(_objActionListener);
        objPanelButtons.add(objButton);
        //----------------------------------------------------------------------        
        addComponents();
        //----------------------------------------------------------------------
    }
    
    public JWavRecorder_Model getModel(){
        return this._model;
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
            JButton objButtonInfo = (JButton) getComponentByName(BUTTON_INFO);
            if (objButtonInfo != null){                
                objButtonInfo.setEnabled(objButton.isEnabled());
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
            JButton objButtonInfo = (JButton) getComponentByName(BUTTON_INFO);
            if (objButtonInfo != null){                
                objButtonInfo.setEnabled(objButton.isEnabled());
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
            JButton objButtonInfo = (JButton) getComponentByName(BUTTON_INFO);
            if (objButtonInfo != null){                
                objButtonInfo.setEnabled(objButton.isEnabled());
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
            if (objSource.getName().equalsIgnoreCase(BUTTON_INFO)){
                btnInfo_ActionListener();
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
        
        private void btnInfo_ActionListener(){            
            //------------------------------------------------------------------
            String message[] = {"Ok"};
            
            boolean bolInfo = true;
            
            if (_wavSignal == null)  bolInfo = false;
            else
            if (_wavSignal.getWavFile() == null)  bolInfo = false;
            
            if (bolInfo) {
                VFunctions.showMessage(objMainClass.getContainerFather(), 
                                    _wavSignal.getInfo(), 
                                    "Information", 
                                    JOptionPane.YES_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE, 
                                    null,
                                    message, 
                                    "Ok");
            }else{
                VFunctions.showMessage(objMainClass.getContainerFather(), 
                                    "There is no signal informated.", 
                                    "Information", 
                                    JOptionPane.YES_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE, 
                                    null,
                                    message, 
                                    "Ok");
            }
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
            boolean validated = true;
            
            if(_wavSignal == null) {
                validated = false;
            }else
            if(_wavSignal.getFullPath().trim().length() == 0) {
                validated = false;
            }else
            if(_wavSignal.getSignal() == null) {
                validated = false;
            }else
            if(_wavSignal.getSignal().getItemCount() == 0) {
                validated = false;
            }
            
            if (!validated){
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
