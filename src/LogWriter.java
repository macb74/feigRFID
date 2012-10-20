import java.io.FileWriter;
import java.io.IOException;


public class LogWriter {

	private static FileWriter logFile;
	
	public static void write(String logText) {
		if(ReadConfig.getConfig().getInt("LOG") == 1) {
			try {
				openLogFile();
				logFile.append(logText);
				closeLogFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void openLogFile() {
	  	try {
	  		logFile = new FileWriter("openTiming.log", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
	}
	
	public static void closeLogFile() {
        try {
        	logFile.flush();
        	logFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
