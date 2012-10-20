
public class CalculateTime {

	public static String calcTime(String startZeit, String zielZeit) {
		
		int laufZeitSec;
		String laufZeit;
		
		laufZeitSec = getSeconds(zielZeit) - getSeconds(startZeit);
		laufZeit = sec2Time(laufZeitSec);
		
		return laufZeit;
	}
	
	private static int getSeconds(String s) {
		String delimiter = ":";
		String[] sArray = new String[3];
		
		if(s.contains(".")) { s = s.substring(0, s.length()-4); }
		sArray = s.split(delimiter);
		int sec = Integer.parseInt(sArray[0]) * 3600 + Integer.parseInt(sArray[1]) * 60 + Integer.parseInt(sArray[2]);
		return sec;
	}

	private static String sec2Time(int sec) {
		String[] val = new String[3];

		if(sec >= 3600) {
			val[0] = String.valueOf(Math.floor(sec/3600));
			val[0] = val[0].substring(0, val[0].length()-2);
			if(val[0].length() == 1) { val[0] = "0" + val[0]; }
			sec = (sec%3600);
		} else {
			val[0] = "00";
		}
		
		if(sec >= 60) {
			val[1] = String.valueOf(Math.floor(sec/60));
			val[1] = val[1].substring(0, val[1].length()-2);
			if(val[1].length() == 1) { val[1] = "0" + val[1]; }
			sec = (sec%60);
		} else {
			val[1] = "00";
		}

		val[2] = String.valueOf(Math.floor(sec));
		val[2] = val[2].substring(0, val[2].length()-2);
		if(val[2].length() == 1) { val[2] = "0" + val[2]; }
		
		return val[0] + ":" + val[1] + ":" + val[2];
		
	}

}
