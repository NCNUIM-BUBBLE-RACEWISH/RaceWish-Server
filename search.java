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

@Path("Search_place")
public class Search_place {
    @GET
    @Path("/json")
    //產生的輸出格式，為json;並使用，UTF-8的編碼
    @Produces("application/json; charset=UTF-8")
    public String stepf(@MatrixParam("target")  @DefaultValue("All")  String target) throws Exception{
        /*try & error*/
        try {
            /*資料庫連接*/
            String conUrl = "jdbc:sqlserver://163.22.17.184:1433;"
                    + "databaseName=ball;"
                    + "user=Aisha;"
                    + "password=bang123!@#;";
            Connection con = null;//釋放物件
            //載入驅動程式
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(conUrl);//con 為 DB
            /*尋找資源*/
            //statement 發送資料庫命令，並且產生statement object
            Statement stat = con.createStatement();
            ResultSet rs;
            if(target.equals("All")){
                rs = stat.executeQuery("select useplace.timecode 時間代碼,substring(useplace.placeid,2,1) 場地代碼, department.cname 使用者 "+
                "from department,useplace where department.deptid = substring(useplace.theuser,1,2)");
            }else{
           	 rs = stat.executeQuery("select useplace.timecode 時間代碼 , substring(useplace.placeid,2,1) 場地代碼, department.cname 使用者 from department,useplace where department.deptid = substring(useplace.theuser,1,2) and substring(department.cname,1,2) =  "+target);
         }
            JSONArray result = new JSONArray();
            while(rs.next()) {
                JSONObject tmp = new JSONObject();
                tmp.put("時間代碼",rs.getString(1));
                tmp.put("場地代碼",rs.getString(2));
                tmp.put("使用者",rs.getString(3));
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