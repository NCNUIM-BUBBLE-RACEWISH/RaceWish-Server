package bubble;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/user/apply")
public class Real_time {
	@GET
	@Path("/information/Real_time")
	//產生的輸出格式，為json;並使用，UTF-8的編碼
    @Produces("application/json; charset=UTF-8")
	public Response count
	(@QueryParam("place") String place ,@QueryParam("week") String week ,@QueryParam("time") String time) throws JSONException {
		NewResponse re =  new NewResponse();
		JSONObject output = new JSONObject();
		try {
			 SqlDB  s = new SqlDB();
	        ResultSet rs;
	        /*執行內容*/
	        rs = s.stat.executeQuery
	        		("select count(*) as 申請次數 "
	        				+ "from apply where placeid = '"
	        				+ 	place+"' and week = '"+week+"' and timecode = '"+time+"'");
	        while(rs.next()) {
	        	output = new JSONObject();
	        	output.put("申請次數",rs.getString(1));
	        	output.put("statuscode", "200");
	        }
	        /*關閉資源*/
            rs.close();
            s.stat.close();
            re.setResponse(output.toString());
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
