package Vision;

import java.awt.Container;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 *
 * @author alexcaranha
 */
public class VFunctions {
    
    public static int showMessage(Container container,
                                   String    message, 
                                   String    title, 
                                   int       optionType,
                                   int       messageType,
                                   Icon      icon,
                                   Object[]  options,
                                   Object    optionDefault){
                
        return JOptionPane.showInternalOptionDialog(container,
                                                    message,
                                                    title,
                                                    optionType,
                                                    messageType, 
                                                    icon, 
                                                    options, 
                                                    optionDefault);
    }
}
