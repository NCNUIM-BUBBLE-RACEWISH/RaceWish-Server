package bubble;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/user/apply")
public class Barchart {
    @GET
    @Path("/bar_chart")
    //產生的輸出格式，為json;並使用，UTF-8的編碼
    @Produces("application/json; charset=UTF-8")
    /*路徑http://localhost:2339/bubble/wish/imcount/json;week=三*/
    public Response much(@QueryParam("week") @DefaultValue("一") String week) throws JSONException {
        JSONObject output = new JSONObject();
 	    NewResponse re =  new NewResponse();
 	    JSONArray result = new JSONArray();
    	try {
    		 SqlDB  s = new SqlDB();
            ResultSet rs;
            /*執行內容*/
            rs = s.stat.executeQuery
                    ("select week,placeid,timecode,count(*)"
                            + " from apply "
                            + "where week = '"
                            + week+"' group by placeid , week , timecode ");
            while(rs.next()) {
               output = new JSONObject();
               output.put("星期",rs.getString(1));
               output.put("場地代碼",rs.getString(2));
               output.put("時段",rs.getString(3));
               output.put("申請隊伍數",rs.getString(4));
               output.put("statuscode", "200");
               result.put(output);
            }
            re.setResponse(result.toString());
            /*關閉資源*/
            rs.close();
            s.stat.close();
            return re.builder.build();
        }catch (SQLException e) {
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
