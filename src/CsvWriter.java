import java.io.FileWriter;
import java.io.IOException;


public class CsvWriter {

	private static FileWriter file;
	
	void write(String[] csvFileContent) {
		
		String csvOutput = csvFileContent[0] + ";" + csvFileContent[1] + ";" + csvFileContent[2] + ";" + csvFileContent[3] + ";" + csvFileContent[4] + ";" + csvFileContent[5] + ";" + csvFileContent[6] + ";\n";
		
		try {
                file.append(csvOutput);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	void openFile(String fileName) {
	  	try {
	  		file = new FileWriter(fileName, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
	}
	
	void closeFile() {
        try {
        	file.flush();
        	file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
