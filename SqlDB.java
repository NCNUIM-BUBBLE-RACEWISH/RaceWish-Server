package bubble;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqlDB {
   Statement stat;
   public SqlDB() throws Exception {
	  try{
		  /*��Ʈw�s���ҨϥΪ��r��*/
		    String conUrl = "jdbc:sqlserver://163.22.17.184:1433;"
		            + "databaseName=ball;"
		            + "user=Aisha;"
		            + "password=bang123!@#;";
		     /*���J�X�ʵ{��*/
		    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		    /*���ͳs�u*/
		    Connection con =DriverManager.getConnection(conUrl);
		    /*�M��귽: Statement�U�F���O ResultSet �O�s���G*/
		    
		    /*�إ�Statement����*/
		    stat = con.createStatement();
	  }catch(Exception e){
		  System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	  }
   }
}
