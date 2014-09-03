import java.util.Date;

import de.feig.FePortDriverException;
import de.feig.FeReaderDriverException;
import de.feig.FedmException;
import de.feig.FedmIscReader;
import de.feig.FedmIscReaderID;


public class SetTime {
	
	private FedmIscReader fedm;

	public synchronized String run() {
		
		String time = getComputerTime();
		String timeArr[] = time.split(":");
		String returnString = "";
		
		// Durch probleme beim setzen der Zeit wenn die Sekunde kleiner 10 ist
		// wird gewartet bis die Sekunden auf jeden Fall zweistellig sind
		if(timeArr[2].substring(0, 1).equals("0")) {
			try {
				System.out.println("sorry, warte 10 sec, bis die Sekunde zweistellig ist.\n");
				Thread.sleep(10000);
				time = getComputerTime();
				String timeArr2[] = time.split(":");
				for (int i = 0; i < timeArr2.length; i++) {
					timeArr[i] = timeArr2[i];
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		String date = getComputerDate();
		String dateArr[] = date.split("-");

//		String rTime = getReaderTime();
		
//		System.out.println("Computer Date:   " + date);
//		System.out.println("Computer Time:   " + time);
//		System.out.println("Reader Time:     " + rTime);
		

		byte century = Byte.parseByte(dateArr[0].substring(0,2));
		byte year    = Byte.parseByte(dateArr[0].substring(2,4));
		//System.out.println((int)Byte.parseByte(timeArr[2]) * 1000);
		//System.out.println((byte)Byte.parseByte(timeArr[2]) * 1000);
		
		// Set Time
		try {	
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_DATE_CENTURY, (byte)century); // 20. Jahrhundert
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_DATE_YEAR, (byte)year); // Jahr 04 im Jahrhundert
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_DATE_MONTH, (byte)Byte.parseByte(dateArr[1])); // September
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_DATE_DAY, (byte)Byte.parseByte(dateArr[2])); // 15. September
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_DATE_TIMEZONE, (byte)0); // z.Zt. ungenutzt
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_DATE_HOUR, (byte)Byte.parseByte(timeArr[0])); // Stunden
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_DATE_MINUTE, (byte)Byte.parseByte(timeArr[1])); // Minuten
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_DATE_MILLISECOND, (int)Byte.parseByte(timeArr[2]) * 1000 + 500); // Millisekunden (inkl. Sekunden)
						
			fedm.sendProtocol((byte)0x87);	// Datum und Uhrzeit setzen
		} catch (FePortDriverException e) {
			e.printStackTrace();
		} catch (FeReaderDriverException e) {
			e.printStackTrace();
		} catch (FedmException e) {
			e.printStackTrace();
		} 
		
		String nrTime = getReaderTime();
//		System.out.println("new Reader Time: " + nrTime);
		returnString = "New Reader Time: " + nrTime.substring(0, 8);
		
		return returnString;

	}

	public String getReaderTime() {
		try
		{
			fedm.sendProtocol((byte)0x88);
		} catch (FePortDriverException e) {
			e.printStackTrace();
		} catch (FeReaderDriverException e) {
			e.printStackTrace();
		} catch (FedmException e) {
			e.printStackTrace();
		}
		
		int stunde = fedm.getIntegerData(FedmIscReaderID.FEDM_ISC_TMP_DATE_HOUR);
		int minute = fedm.getIntegerData(FedmIscReaderID.FEDM_ISC_TMP_DATE_MINUTE);
		int sekunde = fedm.getIntegerData(FedmIscReaderID.FEDM_ISC_TMP_DATE_MILLISECOND);
		
		String time = stunde + ":" + minute + ":" + sekunde;
		return(time);
	}

	
	public String getComputerDate() {
		Date now = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(now);
	}
	
	public String getComputerTime() {
		Date now = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
		return sdf.format(now);
	}
	
    public void setFedmIscReader(FedmIscReader fedm) {
        this.fedm = fedm;
    }	

}
