

public interface FeigGuiListener {
	public void onGetReadSets(String[][] tableData);
	
	public void onGetReaderSets(boolean b);
	
	public void setProtocoll(String connectionText);
	
    public void setMessage(String message, int sleeptime);
    
	public void onGetWriteSets(String newNr, String info);

	public void onReaderConnect(boolean c);

}
