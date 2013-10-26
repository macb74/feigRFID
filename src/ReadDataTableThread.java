import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ReadDataTableThread implements Runnable {

	private FeigGuiListener feigGuiListener;
	private ResultSet resultSet = null;
	private int sort;
	private String sTime;
	private String query = null;
	private int i = 0;
	private int rowCount = 0;
	
	@Override
	public void run() {
        
		switch (sort) {
			case 0: query = "SELECT distinct '0' as LAP, STARTNUMMER, TIME, ZEHNTEL, SERIALNUMBER " +
					"from ZEIT WHERE ID in (SELECT ID from ZEIT where time > '" + sTime + "') order by TIME desc, ZEHNTEL desc";
					break;
			case 1: query = "SELECT distinct count(STARTNUMMER) as LAP, STARTNUMMER, max(TIME) as TIME, 0 as ZEHNTEL, SERIALNUMBER " +
					"from ZEIT WHERE ID in (SELECT ID from ZEIT where time > '" + sTime + "') group by STARTNUMMER, SERIALNUMBER order by TIME desc";
					break;
			case 2: query = "SELECT distinct count(STARTNUMMER) as LAP, STARTNUMMER, max(TIME) as TIME, 0 as ZEHNTEL, SERIALNUMBER " +
					"from ZEIT WHERE ID in (SELECT ID from ZEIT where time > '" + sTime + "') group by STARTNUMMER, SERIALNUMBER order by LAP desc, TIME asc";
					break;
		}
		
		Connection derbyCn = Derby.derbyConnect();

		try {
			resultSet = Derby.executeQuery(query, derbyCn);
			rowCount  = Derby.rowCount(resultSet);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		String[][] tableData = new String[rowCount][5];
		
		try {
			while (resultSet.next()) {
				tableData[i][0] = resultSet.getString("STARTNUMMER");
				tableData[i][1] = resultSet.getString("LAP");
				tableData[i][2] = resultSet.getString("TIME");
				tableData[i][3] = resultSet.getString("SERIALNUMBER");
				tableData[i][4] = resultSet.getString("ZEHNTEL");
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Derby.derbyDisconnect(derbyCn);

		feigGuiListener.onGetReadSets(tableData);

	}

    public void setFeigGuiListener(FeigGuiListener feigGuiListener) {
        this.feigGuiListener = feigGuiListener;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setStartTime(String sTime) {
        this.sTime = sTime;
    }
}
