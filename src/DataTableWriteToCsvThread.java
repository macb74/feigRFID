import java.io.FileWriter;
import java.io.IOException;


public class DataTableWriteToCsvThread implements Runnable {

	private static FileWriter outFile;
	private String[][] tableData;
	private String sTime;
	
	@Override
	public void run() {
		openFile();
		writeToCsvFile(tableData);
		closeFile();
	}

	public void writeToCsvFile(String[][] tableData) {

		for (int i = 0; i < tableData.length; i++) {
			String st = tableData[i][0];
			String rd = tableData[i][1];
			String rt = tableData[i][2];
			String sn = tableData[i][3];
			String lt = CalculateTime.calcTime(sTime, rt);

			try {
				outFile.append(Integer.toString(i) + ";" + st + ";" + rd + ";" + rt + ";" + lt + ";" + sn + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(Integer.toString(i) + ";" + st + ";" + rd + ";" + rt + ";" + lt + ";" + sn + "\n");
		}

	}

	public void openFile() {
	  	try {
	  		outFile = new FileWriter(ReadConfig.getConfig().getString("RESULT_FILENAME"), false);
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
}
