import gnu.io.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class SerialSendThread implements Runnable {

	CommPortIdentifier serialPortId;
	Enumeration enumComm;
	SerialPort serialPort;
	OutputStream outputStream;
	Boolean serialPortGeoeffnet = false;
	int baudrate;
	int dataBits;
	int stopBits;
	int parity;
	private String[] time;
	private int[] serialNumber;
	String portName = ReadConfig.getConfig().getString("SERIAL_OUTPUT_PORT");

    public void run()
    {		
		if(ReadConfig.getConfig().getString("SERIAL_OUTPUT_FORMAT").equals("UB_Datentechnik")) {
			baudrate = 2400;
			dataBits = SerialPort.DATABITS_8;
			stopBits = SerialPort.STOPBITS_1;
			parity = SerialPort.PARITY_NONE;
			
			if (oeffneSerialPort(portName) == true) {
			
				for (int i = 0; i < time.length; i++) {
		        	String sn = Integer.toString(serialNumber[i]);
					while (sn.length() < 4) {
		        		sn = " " + sn;
		        	}
					
					String message = "#Z" + sn + time[i].substring(0, 10) + "\r";
					sendeSerialPort(message);
				}
				schliesseSerialPort();
			}
		}
    }
	    
	public boolean oeffneSerialPort(String portName)
	{
		Boolean foundPort = false;
		if (serialPortGeoeffnet != false) {
			LogWriter.write("Serialport bereits geöffnet");
			return false;
		}
		//System.out.println("Öffne Serialport");
		enumComm = CommPortIdentifier.getPortIdentifiers();
		while(enumComm.hasMoreElements()) {
			serialPortId = (CommPortIdentifier) enumComm.nextElement();
			if (portName.contentEquals(serialPortId.getName())) {
				foundPort = true;
				break;
			}
		}
		if (foundPort != true) {
			LogWriter.write("Serialport nicht gefunden: " + portName + "\n");
			return false;
		}
		try {
			serialPort = (SerialPort) serialPortId.open("Öffnen und Senden", 500);
		} catch (PortInUseException e) {
			LogWriter.write("Port belegt\n");
		}
		try {
			outputStream = serialPort.getOutputStream();
		} catch (IOException e) {
			LogWriter.write("Keinen Zugriff auf OutputStream\n");
		}
/*
		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
			System.out.println("Keinen Zugriff auf InputStream");
		}
		try {
			serialPort.addEventListener(new serialPortEventListener());
		} catch (TooManyListenersException e) {
			System.out.println("TooManyListenersException für Serialport");
		}
		serialPort.notifyOnDataAvailable(true);
*/
		try {
			serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
		} catch(UnsupportedCommOperationException e) {
			LogWriter.write("Konnte Schnittstellen-Paramter nicht setzen\n");
		}
		
		serialPortGeoeffnet = true;
		return true;
	}

	public void schliesseSerialPort()
	{
		if ( serialPortGeoeffnet == true) {
			//System.out.println("Schließe Serialport");
			serialPort.close();
			serialPortGeoeffnet = false;
		} else {
			LogWriter.write("Serialport bereits geschlossen\n");
		}
	}
	
	void sendeSerialPort(String nachricht)
	{
		//System.out.println("Sende: " + nachricht);
		if (serialPortGeoeffnet != true)
			return;
		try {
			outputStream.write(nachricht.getBytes());
		} catch (IOException e) {
			LogWriter.write("Fehler beim Senden\n");
		}
	}

	public void setMessage(String[] time, int[] serialNumber) {
		this.time = time;
		this.serialNumber = serialNumber;
	}

}