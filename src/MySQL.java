import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL
  {

  public static boolean mySqlInsert( String string, Connection cn ) throws SQLException
    {
     try {
         //System.out.println( string );
    	 Statement  st = cn.createStatement();
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

  public static Connection mySqlConnect() {
	    String treiber=null, DbUrl=null ;

	    treiber = "org.gjt.mm.mysql.Driver" ;
	    DbUrl = ReadConfig.getConfig().getString("DBURL");
		//System.out.println("mySQL connect");

	     try {
	         Class.forName( treiber ).newInstance();
	         Connection cn = DriverManager.getConnection( DbUrl, ReadConfig.getConfig().getString("DBUSER"), ReadConfig.getConfig().getString("DBPASS") );
	         return cn;
	     } catch( Exception ex ) {
	    	 System.out.println( ex );
	    	 return null;
	     }

  }

  public static void mySqlDisconnect(Connection cn) {
	  try {
		cn.close();
		//System.out.println("mySQL disconnect");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	  
  }

}