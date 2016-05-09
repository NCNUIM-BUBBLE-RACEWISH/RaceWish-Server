package bubble;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
@Path("/user/apply")
public class Insert_apply {
    @POST
    @Path("/insert")
    //產生的輸出格式，為json;並使用，UTF-8的編碼
    @Consumes("application/json; charset=UTF-8")
    public Response writein(String message) throws JSONException {
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
            JSONArray jarray = new JSONArray(message);
            String ballid = null;
            for(int i = 0; i < jarray.length(); i++) {
                /*抓對應的值*/
                JSONObject sendin = jarray.getJSONObject(i);
                ballid = sendin.getString("ballid");
                String wish = sendin.getString("wish");
                String week = sendin.getString("week");
                String placeid = sendin.getString("placeid");
                String timecode = sendin.getString("timecode");
                String term = sendin.getString("term");
                /*塞入資料庫中*/
                stat.executeUpdate
                ("insert into apply (ballid,wish,placeid,week,timecode,term)"
                        +" values('"+ballid+"','"+wish+"','"+placeid+"','"+week
                        +"','"+timecode+"','"+term+"'); ");
            }
            /*回傳所選的時間*/
            rs = stat.executeQuery("select * from apply where ballid = '"+ballid+"' ");
            JSONArray result = new JSONArray();
            while(rs.next()) {
                JSONObject tmp = new JSONObject();
                tmp.put("帳號",rs.getString(1));
                tmp.put("志願",rs.getString(2));
                tmp.put("場地",rs.getString(3));
                tmp.put("星期",rs.getString(4));
                tmp.put("時間代碼",rs.getString(5));
                tmp.put("學期",rs.getString(6));
                result.put(tmp);
            }
        /*關閉資源*/
        rs.close();
        stat.close();
        re.setResponse(result.toString());
        return re.builder.build();
        /*抓錯*/
        } catch (SQLException e) {
            output.put("statuscode", "500");
            output.put("message","伺服器錯誤");
            re.setResponse(output.toString());
            return re.builder.build();
        } catch (JSONException e) {
            output.put("statuscode", "400");
            output.put("message","錯誤要求");
            re.setResponse(output.toString());
            return re.builder.build();
        }catch(Exception err){
            output.put("statuscode", "500");
            output.put("message","伺服器錯誤");
            re.setResponse(output.toString());
            return re.builder.build();
        }
    }
}
