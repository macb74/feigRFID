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
		}
				
		switch (csvFormat) {
		case 0: lapCountResult(tableData);
		        break;
		case 1: openTimingResult(tableData);
        		break;
		case 2: winlaufenResult(tableData);
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
			String rd = tableData[i][1];
			String rt = tableData[i][2];
			String zt = CalculateTime.calcTime(sTime, rt) + ".0";
	
			try {
				outFile.append(st + ";" + zt + ";" + rd +"\n");
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
				outFile.append( vID + ";" + lID + ";" + st + ";" + rt + ";" + Integer.toString(i) + ";" + rd + ";" + zt + ";" + sn + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
