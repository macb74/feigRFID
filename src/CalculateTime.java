import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class CalculateTime {

	public static String calcTime(String startZeit, String zielZeit) {
		
		long laufZeitSec;
		String laufZeit;
		
		laufZeitSec = getSeconds(zielZeit) - getSeconds(startZeit);
		laufZeit = sec2Time(laufZeitSec);
		
		return laufZeit;
	}
	
	private static long getSeconds(String s) {
		
		long sec = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			sec = df.parse(s).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
//		String delimiter = ":";
//		String[] sArray = new String[3];
//		
//		if(s.contains(".")) { s = s.substring(0, s.length()-4); }
//		sArray = s.split(delimiter);
//		int sec = Integer.parseInt(sArray[0]) * 3600 + Integer.parseInt(sArray[1]) * 60 + Integer.parseInt(sArray[2]);
		return sec / 1000;
	}

	private static String sec2Time(long laufZeitSec) {
		String[] val = new String[3];

		if(laufZeitSec >= 3600) {
			val[0] = String.valueOf(Math.floor(laufZeitSec/3600));
			val[0] = val[0].substring(0, val[0].length()-2);
			if(val[0].length() == 1) { val[0] = "0" + val[0]; }
			laufZeitSec = (laufZeitSec%3600);
		} else {
			val[0] = "00";
		}
		
		if(laufZeitSec >= 60) {
			val[1] = String.valueOf(Math.floor(laufZeitSec/60));
			val[1] = val[1].substring(0, val[1].length()-2);
			if(val[1].length() == 1) { val[1] = "0" + val[1]; }
			laufZeitSec = (laufZeitSec%60);
		} else {
			val[1] = "00";
		}

		val[2] = String.valueOf(Math.floor(laufZeitSec));
		val[2] = val[2].substring(0, val[2].length()-2);
		if(val[2].length() == 1) { val[2] = "0" + val[2]; }
		
		return val[0] + ":" + val[1] + ":" + val[2];
		
	}

}
