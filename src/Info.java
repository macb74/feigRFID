import de.feig.*;


public class Info {
	
	private FedmIscReader fedm;

	public synchronized String[] run() {
		
		byte transponderValidTimeAdr = 12;
		byte modeAdr = 1;
		byte antennaAdr = 15;
		byte rfAdr = 3;
//		byte RelaisAdr = 9;
		String stMode = null;
		String[] returnString = new String[6];
		
		try {
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_READ_CFG, (byte)0);
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_READ_CFG_LOC, true); // aus dem RAM lesen

			// Mode
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_READ_CFG_ADR, modeAdr);
			fedm.sendProtocol((byte)0x80);
			int intMode = fedm.getConfigParaAsInteger(de.feig.ReaderConfig.OperatingMode.Mode, true);
			if(intMode == 0 ) { stMode = "ISO"; } else { stMode = "BRM"; }
			
			// Transponder Valid Time
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_READ_CFG_ADR, transponderValidTimeAdr);
			fedm.sendProtocol((byte)0x80);
			int intVTime = fedm.getConfigParaAsInteger(de.feig.ReaderConfig.OperatingMode.BufferedReadMode.Filter.TransponderValidTime, true);

			// Antennas
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_READ_CFG_ADR, antennaAdr);
			fedm.sendProtocol((byte)0x80);
			int intAntenna = fedm.getConfigParaAsInteger(de.feig.ReaderConfig.AirInterface.Multiplexer.UHF.Internal.NoOfAntennas, true);

			// RF Power
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_READ_CFG_ADR, rfAdr);
			fedm.sendProtocol((byte)0x80);
			int intRf = fedm.getConfigParaAsInteger(de.feig.ReaderConfig.AirInterface.Antenna.UHF.No1.OutputPower, true);

//			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_WRITE_CFG_ADR, RelaisAdr);
//			fedm.sendProtocol((byte)0x80);
//			int intRelais = fedm.getConfigParaAsInteger(de.feig.ReaderConfig.DigitalIO.Relay.No1.ReadEventActivation.AntennaNo, true);
//
//			System.out.println(intRelais);
			
			
			SetTime setTime = new SetTime();
			setTime.setFedmIscReader(fedm);
			String readerTime = setTime.getReaderTime();
			intVTime = intVTime/10;
			int rf = (intRf - 15) * 100;
			
//			System.out.println("Mode...................." + stMode );
//			System.out.println("TransponderValidTime...." + intVTime + " sec");
//			System.out.println("Reader Time............." + readerTime);
//			System.out.println("Antennas................" + intAntenna);
//			System.out.println("Output Power............" + rf + " Watt");

			returnString[0] = stMode;
			returnString[1] = Integer.toString(intVTime);
			returnString[2] = readerTime;
			returnString[3] = Integer.toString(intAntenna);
			returnString[4] = Integer.toString(rf);
			//returnString[5] = Integer.toString(rf);

			return returnString;
			
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

		return returnString;

		//String transponderValidTime = fedm.getConfigParaAsString(, false);
		
	}
	
    public void setFedmIscReader(FedmIscReader fedm) {
        this.fedm = fedm;
    }
	
}