package bubble;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/wish/imcount")
public class imcount {
    @GET
    @Path("/json")
    //產生的輸出格式，為json;並使用，UTF-8的編碼
    @Produces("application/json; charset=UTF-8")
    /*路徑http://localhost:2339/bubble/wish/imcount/json;week=三*/
    public String much(@MatrixParam("week") @DefaultValue("一") String week){
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
                    ("select week,placeid,timecode,count(*)"
                            + " from apply "
                            + "where week = '"
                            + week+"' group by placeid , week , timecode ");
            JSONArray result = new JSONArray();
            while(rs.next()) {
                JSONObject tmp = new JSONObject();
                tmp.put("星期",rs.getString(1));
                tmp.put("場地代碼",rs.getString(2));
                tmp.put("時段",rs.getString(3));
                tmp.put("申請隊伍數",rs.getString(4));
                result.put(tmp);
            }
            /*關閉資源*/
            rs.close();
            stat.close();
            return result.toString();
        }catch(JSONException err) {
            return err.getMessage();
        } catch(Exception err) {
            return err.getMessage();
        }
    }
}