
import de.feig.Fedm;
import de.feig.FedmIscReader;
import de.feig.FedmIscReaderInfo;


public class FedmConnect {

	public void fedmOpenConnection() {
        try {
        	//closeConnection();
	    	fedm.connectTCP(host, ReadConfig.getConfig().getInt("PORT"));
	        fedm.setPortPara("Timeout", ReadConfig.getConfig().getString("TIMEOUT"));
            //System.out.println("connection opened");
           	
            FedmIscReaderInfo readerInfo = fedm.readReaderInfo();
            fedm.readCompleteConfiguration(true);
            
            switch(readerInfo.readerType)
            {
                case de.feig.FedmIscReaderConst.TYPE_ISCMR200:
                case de.feig.FedmIscReaderConst.TYPE_ISCLR2000:
                case de.feig.FedmIscReaderConst.TYPE_ISCMRU200:
                case de.feig.FedmIscReaderConst.TYPE_ISCLRU1000:
                case de.feig.FedmIscReaderConst.TYPE_ISCLRU2000:
                case de.feig.FedmIscReaderConst.TYPE_ISCLRU3000:
                    fedm.setProtocolFrameSupport(Fedm.PRT_FRAME_ADVANCED);
                    break;                    
                default:
                    fedm.setProtocolFrameSupport(Fedm.PRT_FRAME_STANDARD);
                    break;
            }
           	
        }
        catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Can not connect");
        	//System.exit(1);
        }
    }

    public void fedmCloseConnection() {
        try {
            if (fedm.isConnected()) {
                fedm.disConnect();
            }
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
        
    public void setFedmIscReader(FedmIscReader fedm) {
        this.fedm = fedm;
    }
    
    public boolean isConnected() {
        return fedm.isConnected();
    }

	public void setHost(String host) {
		this.host = host;
		
	}
    
    private FedmIscReader fedm;
	private String host;


}
