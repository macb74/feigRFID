import java.util.Date;

import de.feig.FeIscListener;
import de.feig.FePortDriverException;
import de.feig.FeReaderDriverException;
import de.feig.FedmException;
import de.feig.FedmIscReader;
import de.feig.FedmIscReaderConst;
import de.feig.FedmIscReaderID;

public class WriteTag implements Runnable, FeIscListener {

	public synchronized void run() {
        try {                  
            while (isRunning()) {

            	boolean success = false;
				boolean write = false;
				
				stNr    = stnrTo4DigitString(intStNr);
				uniqeNr = "";
				
				if (uniqeID == true) {
					uniqeNr = getUniqeNumber();
				} else {
					uniqeNr = "";
				}
		
				String newSnr = uniqeNr + stNr;
				while (newSnr.length() != 8) {
					newSnr = "0" + newSnr;
				}
								
				FedmConnect con = new FedmConnect();
		    	con.setFedmIscReader(fedm);
		    	con.setHost(host);
				con.fedmOpenConnection();
				
				if(con.isConnected()) {
                	feigGuiListener.onReaderConnect(true);

			        try {
			            fedm.setTableSize(FedmIscReaderConst.ISO_TABLE, 128);
			        	fedm.addEventListener(this, FeIscListener.RECEIVE_STRING_EVENT);
			        	fedm.addEventListener(this, FeIscListener.SEND_STRING_EVENT);
			
			        } catch (FedmException e) {
						e.printStackTrace();
					}
					
			        int tagsInZoneBefore = isoReadTag();
			        String sNrBefore = sNr;
			        feigGuiListener.setProtocoll("alte sNr:   " + sNrBefore + "\n");
					sleep(200);
			        
			        if (tagsInZoneBefore == 1) {
			        	write = isoTagWrite(sNrBefore, newSnr);
			    		sleep(250);
			        } else {
			        	feigGuiListener.setProtocoll("es sind " + tagsInZoneBefore + " Tags im Lesebereich\n");
			        	success = false;
			        }
		
			        
					if (write) {
				        isoReadTag();
				        String sNrAfter = sNr;
				        feigGuiListener.setProtocoll("neue sNr: " + sNrAfter + "\n");
				        
				        if (sNrAfter.equalsIgnoreCase(newSnr)) {
				        	success = true;
				        } else {
				        	feigGuiListener.setProtocoll("ACHTUNG: Neue Nummer ist FALSCH!!!\n");
				        	success = false;
				        }
				        
				        if (sNrBefore.equals(sNrAfter)) {
				        	feigGuiListener.setProtocoll("ACHTUNG: Neue und alte Nummer sind gleich!!!\n");
				        }
				        	        
					}
			        
			        try {
			        	fedm.removeEventListener(this, FeIscListener.RECEIVE_STRING_EVENT);
			        	fedm.removeEventListener(this, FeIscListener.SEND_STRING_EVENT);
			
			        } catch (FedmException e) {
						e.printStackTrace();
			        }
			        
                	feigGuiListener.onReaderConnect(false);
					con.fedmCloseConnection();
					if(success == true) {
						intStNr++;
					}
					feigGuiListener.onGetWriteSets(Integer.toString(intStNr), "sleeping");
			        sleep(sleepTime);
			        feigGuiListener.onGetWriteSets(Integer.toString(intStNr), "");
				} else {
					feigGuiListener.setMessage("Can not connect", 2000);
				}
            }
        }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
		  
	}

	private boolean isoTagWrite(String snr, String newSnr) {
		
		try {

			String hostCommand 		= "24";
			String mode 			= "31";
			//String pre = "2431";
			String oldEpcLng 		= getSnrLenHex(snr);
			String oldEPC 			= snr;
			String epcMemoryBank 	= "01";
			String setPassword		= "00";
			String dbAdr			= "01";
			String dbNoOfBlocks		= "03";
			String dbBlockSize		= "02";
			String dataBlock		= "1000";
			
//			String middle = "01000107023000";
			String requestData = hostCommand + 
					mode + 
					oldEpcLng + 
					oldEPC + 
					epcMemoryBank + 
					setPassword + 
					dbAdr + 
					dbNoOfBlocks + 
					dbBlockSize + 
					dataBlock + 
					newSnr;
			
//			String requestData = pre + oldSnrLen + oldSnr + middle + newSnr;
			//System.out.println("writ sNr: " + newSnr);
			fedm.sendProtocol((byte) 0xB0, requestData);
			
		} catch (FePortDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (FeReaderDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (FedmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} // Kommunikation mit Leser/Transponder

		return true;

	}

	private String getSnrLenHex(String snr) {
		int snrLenInt = snr.length()/2;
		String snrLenHex = Integer.toHexString(snrLenInt);
		snrLenHex = 0 + snrLenHex;
		return snrLenHex;
	}

	private int isoReadTag() {

		int i = 0;
		sNr = "";

		try {
            fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_B0_CMD, 0x01);
            fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_B0_MODE, 0x00);
            fedm.deleteTable(FedmIscReaderConst.ISO_TABLE);
			
			fedm.sendProtocol((byte)0xB0);

			String[] serialNumber = new String[fedm.getTableLength(FedmIscReaderConst.ISO_TABLE)];
			String[] tagType = new String[fedm.getTableLength(FedmIscReaderConst.ISO_TABLE)];
			
			//System.out.println(fedm.getTableLength(FedmIscReaderConst.ISO_TABLE) + " Tag in der Zone");
			
            for(i=0; i< fedm.getTableLength(FedmIscReaderConst.ISO_TABLE); ++i)
			{
                serialNumber[i] = fedm.getStringTableData(i, FedmIscReaderConst.ISO_TABLE, FedmIscReaderConst.DATA_SNR);
                tagType[i] = fedm.getStringTableData(i, FedmIscReaderConst.ISO_TABLE, FedmIscReaderConst.DATA_TRTYPE);
                                
                if (tagType[i].equals("00")) tagType[i] = "Philips I-Code1";
                if (tagType[i].equals("01")) tagType[i] = "Texas Instruments Tag-it HF";
                if (tagType[i].equals("03")) tagType[i] = "ISO15693 Transponder";
                if (tagType[i].equals("04")) tagType[i] = "ISO14443-A";
                if (tagType[i].equals("05")) tagType[i] = "ISO14443-B";
                if (tagType[i].equals("06")) tagType[i] = "I-CODE EPC";
                if (tagType[i].equals("07")) tagType[i] = "I-CODE UID";
                if (tagType[i].equals("09")) tagType[i] = "EPC Class1 Gen2 HF";
                if (tagType[i].equals("81")) tagType[i] = "ISO18000-6-B";
                if (tagType[i].equals("84")) tagType[i] = "EPC Class1 Gen2 UHF";
                
                sNr = serialNumber[i];
			}	
		} catch (FePortDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeReaderDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FedmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return i;
		
	}

	private static String getUniqeNumber() {
		// Date now = new java.util.Date();
		// java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		// return df.format(now);
		
		// generiert eine Zufallszahl zwischen 4096 (Hex: 1000) und 65535 (Hex: FFFF)
		int randomInt = (int)Math.floor(Math.random()*(65535-4096))+4096;
		String randomHex = Integer.toHexString(randomInt);
		return randomHex;
	}

	private static String stnrTo4DigitString(int intStNr) {
		
		String stNr = Integer.toHexString(intStNr);
		while (stNr.length() != 4) {
			stNr = "0" + stNr;
		}
		
		return stNr;
		
	}

	
	public void onReceiveProtocol(FedmIscReader reader, String receiveProtocol) {
	    //System.out.println(receiveProtocol);
		//protocollListener.setProtocoll(receiveProtocol);
		LogWriter.write(receiveProtocol);
	}
	
	public void onSendProtocol(FedmIscReader reader, String sendProtocol) {
	    //System.out.println(sendProtocol);
		//protocollListener.setProtocoll(sendProtocol);
		LogWriter.write(sendProtocol);
	}
	
	public void onReceiveProtocol(FedmIscReader reader, byte[] receiveProtocol) {   
	}
	
	public void onSendProtocol(FedmIscReader reader, byte[] sendProtocol) {
	}

    public void setFeigGuiListener(FeigGuiListener feigGuiListener) {
		this.feigGuiListener = feigGuiListener;
    }
	
    
	private void sleep(int s) {
		try {
			Thread.sleep(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setFedmIscReader(FedmIscReader fedm) {
        this.fedm = fedm;
    }


	public void setNewSnr(int snr) {
		this.intStNr = snr;
	}
	
	public void setUniqeID(boolean uniqeID) {
		this.uniqeID = uniqeID;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	
    /** Getter for property running.
     * @return Value of property running.
     *
     */
    public boolean isRunning() {
        return running;
    }
    
    /** Setter for property running.
     * @param running New value of property running.
     *
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    private boolean running;
    private FedmIscReader fedm;
    private String host;
    private int sleepTime;
    boolean uniqeID;
    private String uniqeNr;
	private String stNr;
	private int intStNr;
	private String sNr = "";
	private FeigGuiListener feigGuiListener;

}
