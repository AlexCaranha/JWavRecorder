package JWavRecorder_Vision;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Menu;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author alexcaranha
 */
public class VFunctions {
    
    public static java.net.URL getFullPathFromResource(String path) {
        java.net.URL result = Menu.class.getResource(path);
        return result;
    }
    
    public static ImageIcon getImageIconFromPath(String path) {
        URL imgURL = VFunctions.getFullPathFromResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return null;
        }
    }
    
    public static ImageIcon getImageRescaled(String path, int width, int height){
        ImageIcon icon = getImageIconFromPath(path);
        if (icon == null) return icon;
        
        Image img = icon.getImage();
        Image newImage = img.getScaledInstance( width, height,  java.awt.Image.SCALE_SMOOTH ) ;  
        
        return new ImageIcon(newImage);
    }
    
    public static ImageIcon getImageRescaled(String path, Dimension dimension){
        ImageIcon icon = getImageIconFromPath(path);
        if (icon == null) return null;
        
        Image img = icon.getImage();
        
        if (img == null) return null;
        Image newImage = img.getScaledInstance((int) dimension.getWidth(),
                                               (int) dimension.getHeight(),
                                               java.awt.Image.SCALE_SMOOTH) ;
        
        return new ImageIcon(newImage);
    }
    
    public static ImageIcon getImageRescaledSquare(String path, Dimension dimension){
        ImageIcon icon = getImageIconFromPath(path);
        if (icon == null) return null;
        
        Image img = icon.getImage();
        
        if (img == null) return null;
        
        int value = Math.min((int) dimension.getWidth(), (int) dimension.getHeight());
        
        Image newImage = img.getScaledInstance(value,
                                               value,
                                               java.awt.Image.SCALE_SMOOTH) ;
        
        return new ImageIcon(newImage);
    }
    
    public static ImageIcon getImageRescaled(ImageIcon icon, int width, int height){
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( width, height,  java.awt.Image.SCALE_SMOOTH ) ;  
        return new ImageIcon( newimg );
    }
    
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
