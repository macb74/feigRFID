import java.io.FileWriter;
import java.io.IOException;


public class CsvWriter {

	private static FileWriter file;
	
	public static void write(String[] csvFileContent1) {
		
		String csvOutput = csvFileContent1[0] + ";" + csvFileContent1[1] + ";" + csvFileContent1[2] + ";" + csvFileContent1[3].substring(0, 8) + ";" + csvFileContent1[4] + ";" + csvFileContent1[5] + ";" + csvFileContent1[6] + ";\n";
		
		try {
                file.append(csvOutput);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	public static void openFile(String fileName) {
	  	try {
	  		file = new FileWriter(fileName, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
	}
	
	public static void closeFile() {
        try {
        	file.flush();
        	file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
