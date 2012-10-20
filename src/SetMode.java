import de.feig.*;


public class SetMode {
	
	private FedmIscReader fedm;

	public synchronized void run() {
		
		byte modeAdr = 1;
		int intMode;
		
		if(this.mode.equals("BRM")) { intMode = 128; } else { intMode = 0; }
		
		try {
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_READ_CFG, (byte)0);
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_READ_CFG_LOC, true); // aus dem EPROM lesen
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_READ_CFG_ADR, modeAdr);
			fedm.sendProtocol((byte)0x80);
			
			// schreiben
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_WRITE_CFG, (byte)0);
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_WRITE_CFG_LOC, true); // aus dem EPROM lesen
			fedm.setData(FedmIscReaderID.FEDM_ISC_TMP_WRITE_CFG_ADR, modeAdr);
			fedm.setConfigPara(de.feig.ReaderConfig.OperatingMode.Mode, intMode, true);			
			
			fedm.sendProtocol((byte)0x81);
			fedm.sendProtocol((byte)0x63);

			LogWriter.write("Reset...");

			
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

	}
	
    public void setFedmIscReader(FedmIscReader fedm) {
        this.fedm = fedm;
    }
	
	private String mode;
	public void setNewMode(String mode) {
		this.mode = mode;
	}
}
