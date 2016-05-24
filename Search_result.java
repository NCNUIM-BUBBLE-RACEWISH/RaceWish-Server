package bubble;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/Search")
public class Search_result {
    @GET
    @Path("/result")
    //產生的輸出格式，為json;並使用，UTF-8的編碼
    @Produces("application/json; charset=UTF-8")
    /* Search_place/json;target='公行'*/
    public Response stepf() throws Exception{
        JSONObject output = new JSONObject();
        NewResponse re =  new NewResponse();
        JSONArray result = new JSONArray();
        /*try & error*/
        try {
        	 SqlDB  s = new SqlDB();
            ResultSet rs;
            rs =s. stat.executeQuery("select useplace.timecode 時間代碼,week 星期,substring(useplace.placeid,2,1) 場地代碼, department.cname 使用者,ballkind.cname 球種,useplace.theuser 帳號 ,useplace.term "+
            "from department,useplace,account,ballkind where department.deptid = substring(useplace.theuser,1,2) and useplace.theuser = account.ballid and account.kind = ballkind.kind");
            while(rs.next()) {
                output = new JSONObject();
                output.put("時間代碼",rs.getString(1));
                output.put("星期",rs.getString(2));
                output.put("場地代碼",rs.getString(3));
                output.put("使用者",rs.getString(4));
                output.put("球種",rs.getString(5));
                output.put("帳號",rs.getString(6));
                output.put("學期",rs.getString(7));
                output.put("statuscode", "200");
                result.put(output);
            }
            /*關閉資源*/
            rs.close();
            s.stat.close();
            re.setResponse(result.toString());
            return re.builder.build();
        /*抓錯*/
        } catch(SQLException err) {
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