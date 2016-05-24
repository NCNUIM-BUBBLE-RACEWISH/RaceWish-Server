package bubble;

import java.sql.SQLException;
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
        	 SqlDB  s = new SqlDB();
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
                s.stat.executeUpdate
                ("insert into apply (ballid,wish,placeid,week,timecode,term)"
                        +" values('"+ballid+"','"+wish+"','"+placeid+"','"+week
                        +"','"+timecode+"','"+term+"'); ");
            }
            output.put("statuscode", "200");
			output.put("message","申請成功");
        /*關閉資源*/
        s.stat.close();
        re.setResponse(output.toString());
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
