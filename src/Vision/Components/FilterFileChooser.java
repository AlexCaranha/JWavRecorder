package Vision.Components;

import java.io.File;
import java.util.Hashtable;
import javax.swing.filechooser.*;

/**
 *
 * @author Alex Libório Caranha
 */
public class FilterFileChooser extends FileFilter {   
    private Hashtable filters;  
    private String fullDescription;  
    private String description;  
  
    public FilterFileChooser( String extensions, String description ){  
        if( extensions != null )  
            addExtension( extensions );  
        if( description != null )  
            setDescription( description );        
    }  
    
    public String getDescription(){   
        return description;   
    }   

    public void setDescription( String description ){   
        this.description = description;   
    }   
   
    public String getExtension( File f ){   
        if( f != null ){   
            String fileName = f.getName();   
            int i = fileName.lastIndexOf( '.' );   
            if( i > 0 && i < fileName.length() - 1 ){   
                return fileName.substring( i + 1 ).toLowerCase();   
            }   
        }         
        return null;   
    }   
    
    @Override
    public boolean accept(File f){
        if(f != null){
            if(f.isDirectory()){
                return true;
            }
            String extension = getExtension(f);
            if( extension != null)
                if (filters.get(extension) != null)
                    return true;
        }
        return false;
    }

    public void addExtension(String extension){
        if( filters == null ){
            filters = new Hashtable(5);
        }
        filters.put(extension.toLowerCase(), this);
        fullDescription = null;
    }  
}