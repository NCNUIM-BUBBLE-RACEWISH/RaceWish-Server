package bubble;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/apply_count")
public class apply_count {
    @GET
    @Path("/json")
    //產生的輸出格式，為json;並使用，UTF-8的編碼
    @Produces("application/json; charset=UTF-8")
    public String count
    (@MatrixParam("place") String place ,@MatrixParam("week") String week ,@MatrixParam("time") String time) throws JSONException {
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
            rs = stat.executeQuery
                    ("select count(*) as 申請次數 "
                            + "from apply where placeid = '"
                            + 	place+"' and week = '"+week+"' and timecode = '"+time+"'");
            JSONArray result = new JSONArray();
            while(rs.next()) {
                JSONObject tmp = new JSONObject();
                tmp.put("申請次數",rs.getString(1));
                result.put(tmp);
            }
            /*關閉資源*/
            rs.close();
            stat.close();
            return result.toString();
        }catch(Exception err) {
                return err.getMessage();
        }
    }
}
