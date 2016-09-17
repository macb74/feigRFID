/*
 * BRMReadThread.java
 *
 * Created on 1. Juli 2003, 11:45
 */

//import java.io.FileWriter;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;

import de.feig.FeHexConvert;
import de.feig.FePortDriverException;
import de.feig.FeReaderDriverException;
import de.feig.FedmBrmTableItem;
import de.feig.FedmException;
import de.feig.FedmIscReader;
import de.feig.FedmIscReaderConst;
import de.feig.FedmIscReaderID;
import de.feig.FedmIscReaderInfo;
import de.feig.FedmIscRssiItem;

/**
 *
 * @author Martin Bussmann
 */
public class BrmReadThread implements Runnable {
    
	public synchronized void run() {
        try {  
		
            while (isRunning()) {

            	// read buffer
            	FedmConnect con = new FedmConnect();
            	con.setFedmIscReader(fedm);
            	con.setHost(host);
            	con.fedmOpenConnection();
            	
                if(con.isConnected()) {
                	feigGuiListener.onReaderConnect(true);
                		            	
	   		        fedm.setTableSize(FedmIscReaderConst.BRM_TABLE, 256);
	            	
	            	readBuffer(this.fedm, this.sets, this.db);
	                
	                if ((fedm.getLastError() >= 0) && clear) {
	                    clearBuffer(this.fedm);
	                }
	                
	                con.fedmCloseConnection();

	                // Sleep, damit die connection Anzeige sichtbar wird.
	                Thread.sleep(100);
                	feigGuiListener.onReaderConnect(false);
                } else {
	            	feigGuiListener.setMessage("Can not connect", 2000);
                }
                
                Thread.sleep(sleepTime);
            }
        }
        catch (InterruptedException e) {} catch (FedmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

	private void readBuffer(FedmIscReader fedm, int sets, boolean db) {
        
		String derbyInsertString = "insert into APP.ZEIT (CTIME, TIME, ZEHNTEL, SERIALNUMBER, STARTNUMMER, ANT, RSSI) VALUES ";
		String mySqlInsertString = "insert into zeit (vID, lID, nummer, zeit, millisecond, reader, ant, rssi) values ";
		
		if (fedm == null) {
            return;
        }
        
        FedmIscReaderInfo readerInfo = fedm.getReaderInfo();
        // read data from reader
        // read max. possible no. of data sets: request 255 data sets
        try {
            switch(readerInfo.readerType)
            {
                case de.feig.FedmIscReaderConst.TYPE_ISCLR200:
                    fedm.setData(FedmIscReaderID.FEDM_ISCLR_TMP_BRM_SETS, sets);
                    fedm.sendProtocol((byte)0x21);
                    break;
                default:
                	fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_ADV_BRM_SETS, sets);
                    fedm.sendProtocol((byte)0x22);
                    break;               
            }

            
            FedmBrmTableItem[] brmItems = null;
            LogWriter.write("* " + fedm.getTableLength(FedmIscReaderConst.BRM_TABLE) + " *********************\n");
            if (fedm.getTableLength(FedmIscReaderConst.BRM_TABLE) > 0)
                brmItems = (FedmBrmTableItem[])fedm.getTable(FedmIscReaderConst.BRM_TABLE);
            
            if (brmItems != null) {
                            	
            	String[] serialNumberHex = new String[brmItems.length];
            	//String[] serialNumber    = new String[brmItems.length];
            	int[] serialNumber    	 = new int[brmItems.length];
            	String[] uniqeNumber     = new String[brmItems.length];
                String[] data            = new String[brmItems.length];
                String[] date            = new String[brmItems.length];
                String[] time            = new String[brmItems.length];
                String[] type            = new String[brmItems.length];
                String[] antNr           = new String[brmItems.length];
                String[] rssi            = new String[brmItems.length];

                String cTime = getComputerTime();
				
				CsvWriter csv = new CsvWriter();
				csv.openFile("brm-result.csv");
				
                for (int i = 0; i < brmItems.length; i++) {
                	
                	if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_SNR)) {
                        serialNumberHex[i] = brmItems[i].getStringData(FedmIscReaderConst.DATA_SNR);

                        // zu kurze Seriennummern werden abgefangen
                        while (serialNumberHex[i].length() < 8) {
            				serialNumberHex[i] = "0" + serialNumberHex[i];
            			}
                        
                        if (serialNumberHex[i].length() > 8) {
                        	serialNumberHex[i] = serialNumberHex[i].substring(0, 8);
                        }

                        //serialNumber[i] = serialNumberHex[i].substring(serialNumberHex[i].length()-4, serialNumberHex[i].length());
                        serialNumber[i] = Integer.parseInt(serialNumberHex[i].substring(serialNumberHex[i].length()-4, serialNumberHex[i].length()),16);
                        uniqeNumber[i] = serialNumberHex[i].substring(0, serialNumberHex[i].length() -4);
                        
                	}
                    
                    if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_RxDB)) { // data block
                        byte[] b = brmItems[i].getByteArrayData(FedmIscReaderConst.DATA_RxDB, brmItems[i].getBlockAddress(), brmItems[i].getBlockCount());
                        data[i] = FeHexConvert.byteArrayToHexString(b);
                        System.out.println("DATA_RxDB: " + FeHexConvert.byteArrayToHexString(b));
                    }
                                         
                    if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_TRTYPE)) { // tranponder type
                        type[i] = brmItems[i].getStringData(FedmIscReaderConst.DATA_TRTYPE);
                        //System.out.println("DATA_TRTYPE: "+ brmItems[i].getStringData(FedmIscReaderConst.DATA_TRTYPE));
                    }
                    
					rssi[i] = getAntData(brmItems[i], "RSSI");
                    antNr[i] = getAntData(brmItems[i], "NR");
                                        
					if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_TIMER)) { // Timer
                        
						switch(readerInfo.readerType)
			            {
			                case de.feig.FedmIscReaderConst.TYPE_ISCLRU1002:
			                	date[i]   = SetTime.getComputerDate();
			                	break;
			                default:
								String year  = Integer.toString(brmItems[i].getReaderTime().getYear());
		                        String month = Integer.toString(brmItems[i].getReaderTime().getMonth());
		                        String day   = Integer.toString(brmItems[i].getReaderTime().getDay());
		                        date[i] = year + "-" + month + "-" + day;
			            		break;
			            }
						

						String hour = Integer.toString(brmItems[i].getReaderTime().getHour());
                        if (hour.length() == 1) {
                            hour = "0" + hour;
                        }
                        String minute = Integer.toString(brmItems[i].getReaderTime().getMinute());
                        if (minute.length() == 1) {
                            minute = "0" + minute;
                        }
                        String second = Integer.toString(brmItems[i].getReaderTime().getMilliSecond() / 1000);
                        if (second.length() == 1) {
                            second = "0" + second;
                        }
                        String millisecond = Integer.toString(brmItems[i].getReaderTime().getMilliSecond() % 1000);
                        if (millisecond.length() == 1) {
                            millisecond = "0" + millisecond;
                        }
                        if (millisecond.length() == 2) {
                            millisecond = "0" + millisecond;
                        }
                        

                        
                        time[i] = hour
                                 + ":"
                                 + minute
                                 + ":"
                                 + second
                                 + "."
                                 + millisecond;
                    }

					String[] csvFileContent = new String[8];
					csvFileContent[0] = Integer.toString(vID);
					csvFileContent[1] = Integer.toString(lID);
					csvFileContent[2] = Integer.toString(serialNumber[i]);
					csvFileContent[3] = time[i];
					csvFileContent[4] = antNr[i];
					csvFileContent[5] = rssi[i];					
					csvFileContent[6] = uniqeNumber[i];
					csvFileContent[7] = cTime;

                    csv.write(csvFileContent);
                    LogWriter.write(serialNumberHex[i] + " - " + antNr[i] + " - " + rssi[i] + " - " + serialNumber[i] + "\n");
                    
					derbyInsertString += "('" + cTime + "', "
							+ "'" + date[i] + " " + time[i].substring(0, 10) + "', "
							+ "" + time[i].substring(9, 10) + ", "
							+ "'" + serialNumberHex[i] + "', "
							+ "'" + serialNumber[i] + "', "
							+ "'" + antNr[i] + "', "
							+ rssi[i] + ")";
					
					mySqlInsertString += "(" + vID +","
							+ "" + lID + ", "
							+ "'" + serialNumber[i] +"', "
							+ "'" + date[i] + " " + time[i].substring(0, 8) +"', "
							+ "" + time[i].substring(9, 12) + ", "
							+ "'" + host + "', "
							+ "'" + antNr[i] + "', "
							+ rssi[i] + ")";
 
					if(i < brmItems.length - 1) {
						derbyInsertString += ",\n";
						mySqlInsertString += ",\n";
					}
                }


                csv.closeFile();
	            
                //Senden der Daten an die serielle Schnittstelle
    	    	if(ReadConfig.getConfig().getString("SERIAL_OUTPUT").equalsIgnoreCase("YES")) {
    	    		LogWriter.write("Write to serial Port\n");
	        		SerialSendThread sSendThread = new SerialSendThread();
	                Thread runner = new Thread(sSendThread);
	                sSendThread.setMessage(time, serialNumber);
	                runner.start();
    	    	}
    	    	
    	    	/*
    	    	 * im Headless mode wird keine derby DB benoetigt
    	    	 */
    	    	if( derby ) {
					Connection derbyCn = Derby.derbyConnect();
	                statusDerby = Derby.derbyUpdate(derbyInsertString, derbyCn);		            
	                Derby.derbyDisconnect(derbyCn);

	                // wenn neue Datensätze in der DerbyDB.
					if(statusDerby) {
						feigGuiListener.onGetReaderSets(true);
					}
    	    	} else {
    	    		statusDerby = true;
    	    	}
                
				
				if( db ) {
					Connection cn = MySQL.mySqlConnect();
					statusMySql = MySQL.mySqlInsert(mySqlInsertString, cn);
		            MySQL.mySqlDisconnect(cn);
				} else {
					statusMySql = true;
				}
								
				// wenn in beide Datenbanken erfolgreich geschrieben wurde, dann kann der
				// buffer gelöscht werden
				if(statusMySql && statusDerby) {
					clear = true;
				}
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	private String getAntData(FedmBrmTableItem fedmBrmTableItem, String key) {
		
		String res = "0";
		byte b = 0;
		try {
			if(fedmBrmTableItem.getIntegerData(FedmIscReaderConst.DATA_ANT_NR) == 0) {
				HashMap<Integer, FedmIscRssiItem> item;
	
				item = fedmBrmTableItem.getRSSI();
		        for(int i=1; i < 5; i++) {
		            if(item.get(i) != null) {
		                FedmIscRssiItem fedmIscRssiItem = (item.get(i));
						if(key.equals("RSSI")) { b = fedmIscRssiItem.RSSI; }
						if(key.equals("NR")) { b = fedmIscRssiItem.antennaNumber; }
						res = b + "";
		            }
		    	}
	
			} else {
				if(key.equals("NR")) {
					if (fedmBrmTableItem.isDataValid(FedmIscReaderConst.DATA_ANT_NR)) { // ant nr
						res = fedmBrmTableItem.getStringData(FedmIscReaderConst.DATA_ANT_NR);
						res = getDualValue(res);
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res + "";
	}

	public String getComputerTime() {
		Date now = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
		return sdf.format(now);
	}


	private String getDualValue(String antNr) {

		int r; // Rest r
		
		int dez = Integer.parseInt(antNr, 16);
		//int dez = Integer.parseInt(antNr);
		String dual = ""; // die Ausgabe wird in einer Zeichenkette (string) gesammelt

		do{
		  r = dez % 2; // Rest berechnen 
		  dez = dez / 2; // neues n berechnen 
		  if (r==0)
		    dual = '0' + dual;
		  else
		    dual = '1' + dual; // Ausgabe konstruieren
		} while (dez>0);
		
		while (dual.length() < 4) {
			dual = "0" + dual;
		}
		
		return dual;
	}

	
    private void clearBuffer(FedmIscReader fedm) {
        if (fedm == null) {
            return;
        }
    
        // clear all read data in reader
        try {
            fedm.sendProtocol((byte)0x32);
        }
        catch (FedmException e) {
        }
        catch (FeReaderDriverException e) {          
        }
        catch (FePortDriverException e) {          
        }

        clear = false;
        statusMySql = false;
        statusDerby = false;
        
        // show protocol
        //ShowLastProt iState, sTime
    }
        
    
    public void setFedmIscReader(FedmIscReader fedm) {
        this.fedm = fedm;
    }
    
    /** Getter for property sets.
     * @return Value of property sets.
     *
     */
    public int getSets() {
        return sets;
    }
    
    /** Setter for property sets.
     * @param sets New value of property sets.
     *
     */
    public void setSets(int sets) {
        this.sets = sets;
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
    
 
    public void setFeigGuiListener(FeigGuiListener feigGuiListener) {
        this.feigGuiListener = feigGuiListener;
    }
    
    /** Setter for property sets.
     * @param sets New value of property sets.
     *
     */
    public void setDB(boolean db) {
        this.db = db;
    }
    	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	public void setId(int[] id) {
		this.vID = id[0];
		this.lID = id[1];
	}
	
	public void setUseDerby(boolean b) {
		this.derby = b;
	}
	
	private int lID;
	private int vID;
	private int sleepTime;
	private String host;
    private boolean clear;
    private FedmIscReader fedm;
    private int sets = 255;
    private boolean running;
	private boolean db;
	private boolean statusMySql;
	private boolean statusDerby;
    private FeigGuiListener feigGuiListener;
	private boolean derby = true;

}
