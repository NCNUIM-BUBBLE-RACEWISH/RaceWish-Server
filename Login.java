/*使用者帳號&密碼傳入*/
//解碼
/*找出資料庫中是否有相對應的資料*/
//有回傳Yes
//沒有回傳NO
package bubble;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/user")
public class Login {
   @POST
   @Path("/passwd")
 //產生的輸出入格式，為json;並使用，UTF-8的編碼
   @Consumes("application/json; charset=UTF-8")
  public Response login(String input) throws Exception{
	  JSONObject output = new JSONObject();
	   NewResponse re =  new NewResponse();
      try {
    	  SqlDB  s = new SqlDB();
	        ResultSet rs;
	        /*執行內容*/
	        //解析 json 字串，message 為json字串
	        JSONObject sendin = new JSONObject(input);
	        
	        /*抓對應的值*/
	        String kind = sendin.getString("kind");
            String deptid = sendin.getString("deptid");
	        String passwd = sendin.getString("passwd");
	        String userid = deptid+kind;
	        /*從資料庫中抓取所需該帳號的密碼*/
          rs = s.stat.executeQuery("select passwd from account where ballid = '"+userid+"'");
	        /*是否相符，並依照相符 or not 回傳狀態*/
          int i = 0;
          while(rs.next()){
        	  output = new JSONObject();
        	  i++;
        	  if(rs.getString(1).equals(passwd)){
                  output.put("account",userid );
                  output.put("passwd", passwd);
                  output.put("statuscode", "200");
          		  output.put("message", "成功登入");
               }else{
            	   output.put("statuscode", "401");
            	   output.put("message", "帳號或密碼錯誤");
               }  
          }
          if(i== 0){
        	  output = new JSONObject();
        	  output.put("statuscode", "401");
       	      output.put("message", "帳號或密碼錯誤");
          }
          /*關閉資源*/
          rs.close();
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
