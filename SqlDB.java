package bubble;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqlDB {
   Statement stat;
   public SqlDB() throws Exception {
	  try{
		  /*資料庫連接所使用的字串*/
		    String conUrl = "jdbc:sqlserver://***********;"
		            + "databaseName=ball;"
		            + "user=******;"
		            + "password******;";
		     /*載入驅動程式*/
		    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		    /*產生連線*/
		    Connection con =DriverManager.getConnection(conUrl);
		    /*尋找資源: Statement下達指令 ResultSet 保存結果*/
		    
		    /*建立Statement物件*/
		    stat = con.createStatement();
	  }catch(Exception e){
		  System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	  }
   }
}
