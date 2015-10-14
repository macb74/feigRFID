import de.feig.FedmIscReader;


public class FeigRfidHeadless implements FeigGuiListener {

	public FeigRfidHeadless(FedmIscReader fedm) {
    	int[] id = new int[2];
    	id[0] = ReadConfig.getConfig().getInt("vID");
    	id[1] = ReadConfig.getConfig().getInt("lID");
    	
        // start thread
		BrmReadThread brmReadThread = new BrmReadThread();
        brmReadThread.setFedmIscReader(fedm);
        brmReadThread.setHost(ReadConfig.getConfig().getString("HOST"));
        brmReadThread.setSleepTime(ReadConfig.getConfig().getInt("SLEEPTIME"));
        brmReadThread.setSets(ReadConfig.getConfig().getInt("SETS"));
		brmReadThread.setDB(true);
		brmReadThread.setId(id);
        brmReadThread.setFeigGuiListener(this);
        brmReadThread.setUseDerby(false);
        Thread runner = new Thread(brmReadThread);
        brmReadThread.setRunning(true);
        runner.start(); 
	}

	@Override
	public void onGetReadSets(String[][] tableData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetReaderSets(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProtocoll(String connectionText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMessage(String message, int sleeptime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetWriteSets(String newNr, String info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReaderConnect(boolean c) {
		// TODO Auto-generated method stub
		
	}

}
