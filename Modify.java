package bubble;

import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;


@Path("/user")
public class Modify{
 @POST
 @Path("/passwd/verification")
//產生的輸出入格式，為json;並使用，UTF-8的編碼
 @Consumes("application/json; charset=UTF-8")
 @Produces("application/json; charset=UTF-8")
public Response login(String input) throws Exception{
	   JSONObject output = new JSONObject();
	   NewResponse re =  new NewResponse();
    try {
    	     SqlDB  s = new SqlDB();
	        //解析 json 字串，message 為json字串
	        JSONObject sendin = new JSONObject(input);
	        
	        /*抓對應的值*/
	        String kind = sendin.getString("kind");
	        String deptid = sendin.getString("deptid");
	        String email = sendin.getString("email");
	        String passwd = sendin.getString("passwd");
	        /*塞入資料庫中*/
	        s.stat.executeUpdate
	       ( "UPDATE account SET passwd = '"+passwd+"' WHERE kind = '"+kind+"' AND email = '"+email+"'AND deptid = '"+deptid+"'" );
	        /*從資料庫中抓取所需更改密碼
	        /*是否相符，並依照相符 or not 回傳狀態*/
		    output = new JSONObject();
			output.put("passed",passwd );
		    output.put("statuscode", "200");
			output.put("message", "密碼已經改變");

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