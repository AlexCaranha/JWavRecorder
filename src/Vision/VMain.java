/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Vision;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author alexcaranha
 */
public class VMain extends JFrame{
    
    public VMain(String title, int _width, int _height){
        //----------------------------------------------------------------------
        super(title);
        //----------------------------------------------------------------------
        this.setLayout(new BorderLayout());
        this.setSize(_width, _height);
        //----------------------------------------------------------------------
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }});
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //----------------------------------------------------------------------
    }
    
    public void addObject(Component objComponent, String layoutConstraint){
        this.add(objComponent, layoutConstraint);
    }
    
    private void exitApplication(){
        //----------------------------------------------------------------------
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        String message[] = {"Yes", "No"};
        int i = VFunctions.showMessage(this.getContentPane(),
                                       "Do you really want to exit?", 
                                       "Exit", 
                                       JOptionPane.YES_NO_OPTION,
                                       JOptionPane.QUESTION_MESSAGE, null, message, message[1]);
        //----------------------------------------------------------------------
        if (i == JOptionPane.YES_OPTION) {
            dispose();
        }else{
            repaint();
        }
        //----------------------------------------------------------------------
    }
    
    public void centering() {
        Container container = this;
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int componentWidth = container.getWidth();
        int componentHeight = container.getHeight();
        container.setBounds(
                                (screenSize.width - componentWidth) / 2, 
                                (screenSize.height - componentHeight) / 2, 
                                componentWidth, 
                                componentHeight
                            );
    }
    
    public static void main(String[] args) {    
        int width  = 1000;
        int height = 500;
              
	VMain objScreen = new VMain("JWavRecorder", width, height);
        objScreen.centering();
        objScreen.setVisible(false);
        objScreen.addObject(new JWavRecorder(width, height, objScreen.getContentPane()), BorderLayout.CENTER);
	objScreen.setResizable(false);
        objScreen.pack();        
        objScreen.setSize(width, height);
        objScreen.setVisible(true);
        objScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
