/*使用者帳號&密碼傳入*/
//解碼
/*找出資料庫中是否有相對應的資料*/
//有回傳Yes
//沒有回傳NO
package bubble;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;



@Path("/user/passwd")
public class Test {
   @POST
 //產生的輸出入格式，為json;並使用，UTF-8的編碼
   @Consumes("application/json; charset=UTF-8")
   @Produces("application/json; charset=UTF-8")
  public Response login(String input) throws ClassNotFoundException{
	   JSONObject output = new JSONObject();
	   NewResponse re =  new NewResponse();
      try {
	        /*資料庫連接所使用的字串*/
	        String conUrl = "jdbc:sqlserver://163.22.17.184:1433;"
	                + "databaseName=ball;"
	                + "user=Aisha;"
	                + "password=bang123!@#;";
	         /*載入驅動程式*/
	        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        /*產生連線*/
	        Connection con =DriverManager.getConnection(conUrl);
	        /*尋找資源: Statement下達指令 ResultSet 保存結果*/
	        Statement stat;
	        ResultSet rs;
	        /*建立Statement物件*/
	        stat = con.createStatement();
	        /*執行內容*/
	        //解析 json 字串，message 為json字串
	        JSONObject sendin = new JSONObject(input);
	        /*抓對應的值*/
	        String kind = sendin.getString("kind");
            String deptid = sendin.getString("deptid");
	        String passwd = sendin.getString("passwd");
	        String _id = deptid+kind;
	        /*從資料庫中抓取所需該帳號的密碼*/
          rs = stat.executeQuery
          ("select passwd from accont where ballid = '"+_id +"'");
	        /*是否相符，並依照相符 or not 回傳狀態*/
	        if(rs.getString(1).equals(passwd)){
              output = new JSONObject();
              output.put("account",_id );
              output.put("passwd", passwd);
              re.setResponse(output.toString());
           }else{
        	   output.put("message", "wrong password or Wrong Account");
           }
          /*關閉資源*/
          rs.close();
          stat.close();
      /*抓錯*/
		}catch(IllegalArgumentException e) {   
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return re.builder.build();
  }

}
