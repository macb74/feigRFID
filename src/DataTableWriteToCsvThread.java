import java.io.FileWriter;
import java.io.IOException;


public class DataTableWriteToCsvThread implements Runnable {

	private static FileWriter outFile;
	private String[][] tableData;
	private String sTime;
	private String filename;
	private int vID;
	private int lID;
	
	@Override
	public void run() {
		openFile(filename);
		writeToCsvFile(tableData);
		closeFile();
	}

	public void writeToCsvFile(String[][] tableData) {

		int csvFormat = 0;
		
		if(ReadConfig.getConfig().getString("RESULT_FORMAT") != null) {
			if(ReadConfig.getConfig().getString("RESULT_FORMAT").equals("opentiming")) {
				csvFormat = 1;
			}
			if(ReadConfig.getConfig().getString("RESULT_FORMAT").equals("winlaufen")) {
				csvFormat = 2;
			}
			if(ReadConfig.getConfig().getString("RESULT_FORMAT").equalsIgnoreCase("StartuLaufzeit")) {
				csvFormat = 3;
			}
			if(ReadConfig.getConfig().getString("RESULT_FORMAT").equalsIgnoreCase("giveMeAll")) {
				csvFormat = 4;
			}
		}
		
		switch (csvFormat) {
		case 0: lapCountResult(tableData);
		        break;
		case 1: openTimingResult(tableData);
        		break;
		case 2: winlaufenResult(tableData);
				break;
		case 3: startulaufzeitResult(tableData);
				break;
		case 4: giveMeAll(tableData);
		break;
		}
				
	}

	private void lapCountResult(String[][] tableData) {
		for (int i = 0; i < tableData.length; i++) {
			String st = tableData[i][0];
			String rd = tableData[i][1];
			String rt = tableData[i][2];
			String sn = tableData[i][3];
			String zt = CalculateTime.calcTime(sTime, rt);
	
			try {
				outFile.append(Integer.toString(i) + ";" + st + ";" + rd + ";" + rt + ";" + zt + ";" + sn + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void winlaufenResult(String[][] tableData) {
		for (int i = 0; i < tableData.length; i++) {
			String st = tableData[i][0];
			String rt = tableData[i][2];
			//String zt = CalculateTime.calcTime(sTime, rt) + "." + tableData[i][4];
	
			try {
				outFile.append(st + ";" + rt.substring(11) +"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void openTimingResult(String[][] tableData) {
		for (int i = 0; i < tableData.length; i++) {
			String st = tableData[i][0];
			String rd = tableData[i][1];
			String rt = tableData[i][2];
			String sn = tableData[i][3];
			String zt = CalculateTime.calcTime(sTime, rt);
	
			try {
				outFile.append( vID + ";" + lID + ";" + st + ";" + zt + ";" + Integer.toString(i) + ";" + rd + ";" + rt + ";" + sn + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	private void startulaufzeitResult(String[][] tableData) {
		for (int i = 0; i < tableData.length; i++) {
			String st = tableData[i][0];
			String rt = tableData[i][2];
			String zt = CalculateTime.calcTime(sTime, rt) + "." + tableData[i][4];
	
			try {
				outFile.append(st + ";" + zt +"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void giveMeAll(String[][] tableData) {
		try {
			outFile.append("Startnummer;Seriennummer;Runden;ReaderZeit;Zielzeit;ZielzeitZehntel;Antennen;RSSI\n");
			
			for (int i = 0; i < tableData.length; i++) {
				String startnummer  = tableData[i][0];
				String runden       = tableData[i][1];
				String zeit         = tableData[i][2];
				String seriennummer = tableData[i][3];
				String antenna      = tableData[i][5];
				String rssi         = tableData[i][6];
				String zielZeit     = CalculateTime.calcTime(sTime, zeit);
				String zielZeitZ    = CalculateTime.calcTime(sTime, zeit) + "." + tableData[i][4];
	

				outFile.append(startnummer + ";" +
						seriennummer + ";" +
						runden + ";" +
						zeit + ";" +
						zielZeit + ";" +
						zielZeitZ + ";" +
						antenna + ";" +
						rssi + 
						"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void openFile(String filename) {
	  	try {
	  		outFile = new FileWriter(filename, false);
	  		outFile.write("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
	}
	
	public static void closeFile() {
        try {
        	outFile.flush();
        	outFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void setFileContent(String[][] tableData) {
		this.tableData = tableData;
	}

	public void setStartTime(String sTime) {
		this.sTime = sTime;
	}
	
	public void setFileName(String filename) {
		this.filename = filename;
	}
	
	public void setId(int[] id) {
		this.vID = id[0];
		this.lID = id[1];
	}
}
