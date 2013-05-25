/*
 * BRMReadThread.java
 *
 * Created on 1. Juli 2003, 11:45
 */

import java.sql.Connection;
import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;
import de.feig.*;

/**
 *
 * @author Martin Bussmann
 */
public class BrmReadThread implements Runnable, FeIscListener {
    
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
                	
		        	fedm.addEventListener(this, FeIscListener.RECEIVE_STRING_EVENT);
		        	fedm.addEventListener(this, FeIscListener.SEND_STRING_EVENT);
	            	
	   		        fedm.setTableSize(FedmIscReaderConst.BRM_TABLE, 256);
	            	
	            	readBuffer(this.fedm, this.sets, this.db);
	                
	                if ((fedm.getLastError() >= 0) && clear) {
	                    clearBuffer(this.fedm);
	                }

		        	fedm.removeEventListener(this, FeIscListener.RECEIVE_STRING_EVENT);
		        	fedm.removeEventListener(this, FeIscListener.SEND_STRING_EVENT);
	                
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
        
		String derbyInsertString = "insert into APP.ZEIT (CTIME, TIME, SERIALNUMBER, STARTNUMMER) VALUES ";
		String mySqlInsertString = "insert into zeit (vID, lID, nummer, zeit, reader) values ";
		
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
                String[] time            = new String[brmItems.length];
                String[] type            = new String[brmItems.length];
                String[] antNr           = new String[brmItems.length];

                String cTime = getComputerTime();
				
				CsvWriter.openFile(fileName);
				
                for (int i = 0; i < brmItems.length; i++) {
                	if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_SNR)) {
                        serialNumberHex[i] = brmItems[i].getStringData(FedmIscReaderConst.DATA_SNR);

                        // zu kurze Seriennummern werden abgefangen
                        while (serialNumberHex[i].length() != 24) {
            				serialNumberHex[i] = "0" + serialNumberHex[i];
            			}

                        //serialNumber[i] = serialNumberHex[i].substring(serialNumberHex[i].length()-4, serialNumberHex[i].length());
                        serialNumber[i] = Integer.parseInt(serialNumberHex[i].substring(serialNumberHex[i].length()-4, serialNumberHex[i].length()),16);
                        uniqeNumber[i] = serialNumberHex[i].substring(serialNumberHex[i].length()-18, serialNumberHex[i].length() -4);
                        
                	}
                    
                    if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_RxDB)) { // data block
                        byte[] b = brmItems[i].getByteArrayData(FedmIscReaderConst.DATA_RxDB, brmItems[i].getBlockAddress(), brmItems[i].getBlockCount());
                        data[i] = FeHexConvert.byteArrayToHexString(b);
                        System.out.println("DATA_RxDB: " + FeHexConvert.byteArrayToHexString(b));
                    }
                    
                    if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_ANT_NR)) { // ant nr
                        antNr[i] = brmItems[i].getStringData(FedmIscReaderConst.DATA_ANT_NR);
                    }
                     
                    if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_TRTYPE)) { // tranponder type
                        type[i] = brmItems[i].getStringData(FedmIscReaderConst.DATA_TRTYPE);
                        //System.out.println("DATA_TRTYPE: "+ brmItems[i].getStringData(FedmIscReaderConst.DATA_TRTYPE));
                    }
                                        
					if (brmItems[i].isDataValid(FedmIscReaderConst.DATA_TIMER)) { // Timer
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
					
					String antNrDual = getDualValue(antNr[i]);

					String[] csvFileContent = new String[7];
					csvFileContent[0] = Integer.toString(vID);
					csvFileContent[1] = Integer.toString(lID);
					csvFileContent[2] = Integer.toString(serialNumber[i]);
					csvFileContent[3] = time[i];
					csvFileContent[4] = antNrDual;
					csvFileContent[5] = uniqeNumber[i];
					csvFileContent[6] = cTime;

                    CsvWriter.write(csvFileContent);

                    LogWriter.write(serialNumberHex[i] + " - " + antNrDual + " - " + serialNumber[i] + "\n");

					derbyInsertString += "('" + cTime + "', '" + time[i].substring(0, 8) + "', '" + serialNumberHex[i] + "', '" + serialNumber[i] + "')";
					mySqlInsertString += "(" + vID +"," + lID + ", '" + serialNumber[i] +"', '" + time[i] +"', '" + host + "')";
 
					if(i < brmItems.length - 1) {
						derbyInsertString += ",\n";
						mySqlInsertString += ",\n";
					}
                }


                CsvWriter.closeFile();
	            
				Connection derbyCn = Derby.derbyConnect();
                statusDerby = Derby.derbyUpdate(derbyInsertString, derbyCn);		            
                Derby.derbyDisconnect(derbyCn);
				
				// wenn neue Datensätze in der DerbyDB.
				if(statusDerby) {
					feigGuiListener.onGetReaderSets(true);
				}
				
				if( db == true ) {
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
    
	public String getComputerTime() {
		Date now = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
		return sdf.format(now);
	}
    
	public void openCsv() {
	  	try {
	  		csvFile = new FileWriter(fileName, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
	}
	
	public void closeCsv() {
        try {
        	csvFile.flush();
			csvFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
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
    
	public void onReceiveProtocol(FedmIscReader reader, byte[] receiveProtocol) {   
	}
	
	public void onSendProtocol(FedmIscReader reader, byte[] sendProtocol) {
	}

	public void onReceiveProtocol(FedmIscReader arg0, String arg1) {
//		protocollListener.setProtocoll(arg1);
		LogWriter.write(arg1);
	}

	public void onSendProtocol(FedmIscReader arg0, String arg1) {
//		protocollListener.setProtocoll(arg1);	
		LogWriter.write(arg1);
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setId(int[] id) {
		this.vID = id[0];
		this.lID = id[1];
	}
	
	private int lID;
	private int vID;
	private String fileName;
	private int sleepTime;
	private String host;
    private boolean clear;
    private FedmIscReader fedm;
    private int sets = 255;
    private boolean running;
	private boolean db;
	private boolean statusMySql;
	private boolean statusDerby;
	public FileWriter csvFile;
    private FeigGuiListener feigGuiListener;

}
