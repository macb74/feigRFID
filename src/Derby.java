import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Derby
  {
	
  public static boolean derbyUpdate( String string, Connection cn ) throws SQLException
    {
     try {
         Statement  st = cn.createStatement();
         //System.out.println( string );
         st.executeUpdate( string );
         
/*         
         ResultSetMetaData rsmd = rs.getMetaData();
         int n, nmax = rsmd.getColumnCount();
         System.out.println("----------- Antwort -------------------") ;
         while( rs.next() )
            {
            for( n=1 ; n<=nmax ; n++ )
               System.out.print( rs.getString( n ) + "--" ) ;
            System.out.println() ;
            }
         System.out.println("----------- Antwort -------------------") ;
             rs.close();
             st.close();
*/
     } catch( SQLException ex ) {
    	 System.out.println( ex );
         return false;
     }
     return true;
   }

  public static ResultSet executeQuery( String string, Connection cn ) throws SQLException
  {
	  ResultSet result = null;
	  try {
	      Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	      //System.out.println( string );
	      result = st.executeQuery( string );

	/*        
	      ResultSetMetaData rsmd = rs.getMetaData();
	      int n, nmax = rsmd.getColumnCount();
	      System.out.println("----------- Antwort -------------------") ;
	      while( rs.next() )
	         {
	         for( n=1 ; n<=nmax ; n++ )
	            System.out.print( rs.getString( n ) + "--" ) ;
	         System.out.println() ;
	         }
	      System.out.println("----------- Antwort -------------------") ;
	          rs.close();
	          st.close();
	*/
	  } catch( SQLException ex ) {
		  System.out.println( ex );
		  return result;
	  }
	  return result;
 }
  
  public static Connection derbyConnect() {

	String memdb = "";
	if(ReadConfig.getConfig().getInt("USE_MEMORY_DB") == 1) {
		memdb = "memory:";
	}
	  
	try {
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
		Connection cn = DriverManager.getConnection("jdbc:derby:" + memdb + "Result-DB;create=true");
		//Connection cn = DriverManager.getConnection("jdbc:derby:Result-DB;create=true");
		
		DatabaseMetaData dmd = cn.getMetaData();
		ResultSet rs = dmd.getTables(null,"APP", "ZEIT",null);
		
		if (!rs.next()) {
			Statement stmt = cn.createStatement();
		    stmt.executeUpdate("CREATE TABLE \"APP\".\"ZEIT\"(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ctime TIME, time TIMESTAMP, zehntel INT, serialnumber VARCHAR(255), startnummer VARCHAR(255) NOT NULL)");
		}
		
		return cn;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}	
  }

  public static void derbyDisconnect(Connection cn) {
	try {
		if (cn != null) {
			cn.close();
		}
	} catch (Exception e) {
	
	}  
  }

public static int rowCount(ResultSet resultSet) throws SQLException {
	resultSet.last();
	int rowCount = resultSet.getRow();
	resultSet.beforeFirst();
	return(rowCount);
}
}