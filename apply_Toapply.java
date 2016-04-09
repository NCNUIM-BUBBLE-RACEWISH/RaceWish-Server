package bubble;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/apply")
public class apply {
	@POST
    @Path("/json")
    //產生的輸出格式，為json;並使用，UTF-8的編碼
    @Consumes("application/json; charset=UTF-8")
    public String writein(String message) {
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
	        JSONObject sendin = new JSONObject(message);
	        /*抓對應的值*/
	        String code = sendin.getString("code");
	        String ballid = sendin.getString("ballid");
	        String wish = sendin.getString("wish");
	        String week = sendin.getString("week");
	        String placeid = sendin.getString("placeid");
	        String timecode = sendin.getString("timecode");
	        String term = sendin.getString("term");
	        /*塞入資料庫中*/
	        stat.executeUpdate
	        ("insert into apply (code,ballid,wish,placeid,week,timecode,term)"
	        		+" values('"+code+"','"+ballid+"','"+wish+"','"+placeid+"','"+week
	        		+"','"+timecode+"','"+term+"'); ");
	        /*回傳所選的時間*/
	        rs = stat.executeQuery("select * from apply where ballid = '"+ballid+"' ");
	        JSONArray result = new JSONArray();
	        while(rs.next()) {
	            JSONObject tmp = new JSONObject();
	            tmp.put("代碼",rs.getString(1));
	            tmp.put("帳號",rs.getString(2));
	            tmp.put("志願",rs.getString(3));
	            tmp.put("場地",rs.getString(4));
	            tmp.put("星期",rs.getString(5));
	            tmp.put("時間代碼",rs.getString(6));
	            tmp.put("學期",rs.getString(7));
	            result.put(tmp);
	        }
        /*關閉資源*/
        rs.close();
        stat.close();
        return result.toString();
        /*抓錯*/
		} catch(JSONException err) {
            	return err.getMessage();
		} catch(Exception err) {
            	return err.getMessage();
		}
	}
}
