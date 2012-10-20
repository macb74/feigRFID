import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadConfig {
    
    private static ReadConfig instance = null;
    
    private Properties props;
    
    private ReadConfig(){
    	this.doReadConfig();
    }
       
    public static ReadConfig getConfig(){
    	if (instance == null){
    		instance = new ReadConfig();
    	}
    	return instance;
    	
    }
    private void doReadConfig() {
		props = new Properties();
	    try {
	       props.loadFromXML(new FileInputStream("config.xml"));
	       //System.out.println("gelesen");
	    } catch (FileNotFoundException e) {
	       e.printStackTrace();
	    } catch (IOException e) {
	       e.printStackTrace();
	    }
	}

    public String getString(String key){
    	return props.getProperty(key);
    }
    
    public int getInt(String key){
    	return Integer.parseInt(props.getProperty(key));
    }
    
    public boolean getBoolean(String key){
    	return Boolean.parseBoolean(props.getProperty(key));
    }

/*
    public static void main( String[] args){
    	String prop = ReadConfig.getConfig().getString("vorname");
    	String x = ReadConfig.getConfig().getString("name");
    	System.out.println(prop);
    	System.out.println(x);

    }
*/
    
//    public void writeConfig() {
//	    Properties p = new Properties();
//		p.setProperty("vorname", "Hans-Dieter Mustermann");
//		
//	    // Speichern der Daten in die XML-Datei
//	    try {
//	       p.storeToXML(new FileOutputStream("config.xml"), null);
//	    } catch (FileNotFoundException e) {
//	       e.printStackTrace();
//	    } catch (IOException e) {
//	       e.printStackTrace();
//	    }
//
//	}

}
