package bubble;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;


@Path("/user")
public class forget{
 @POST
 @Path("/passwd/forget")
//���ͪ���X�J�榡�A��json;�èϥΡAUTF-8���s�X
 @Consumes("application/json; charset=UTF-8")
 @Produces("application/json; charset=UTF-8")
public Response login(String input) throws Exception{
	   JSONObject output = new JSONObject();
	   NewResponse re =  new NewResponse();
    try {
    	 SqlDB  s = new SqlDB();
	        ResultSet rs;
	        /*�إ�Statement����*/
	        /*���椺�e*/
	        //�ѪR json �r��Amessage ��json�r��
	        JSONObject sendin = new JSONObject(input);
	        
	        /*���������*/
	        String kind = sendin.getString("kind");
	        String deptid = sendin.getString("deptid");
	        String userid = deptid+kind;
	        String email = sendin.getString("email");
	        /*�q��Ʈw������һݸӱb�����K�X*/
        rs = s.stat.executeQuery("select email from account where ballid = '"+userid+"'");
	        /*�O�_�۲šA�è̷Ӭ۲� or not �^�Ǫ��A*/
        while(rs.next()){
      	  if(rs.getString(1).equals(email)){
                output = new JSONObject();
				output.put("email",email );
                output.put("statuscode", "200");
        		output.put("message", "���\�H�H");
             }else{
          	   output.put("statuscode", "401");
          	   output.put("message", "�b���αK�X���~");
             }  
        }
        /*�����귽*/
        rs.close();
        s.stat.close();
        re.setResponse(output.toString());
        return re.builder.build();
    /*���*/
      } catch (SQLException e) {
      	output.put("statuscode", "500");
			output.put("message","���A�����~");
			re.setResponse(output.toString());
	        return re.builder.build();
		} catch (JSONException e) {
			output.put("statuscode", "400");
			output.put("message","���~�n�D");
			re.setResponse(output.toString());
	        return re.builder.build();
		}catch(Exception err){
			output.put("statuscode", "500");
			output.put("message","���A�����~");
			re.setResponse(output.toString());
	        return re.builder.build();
		}
      }
}
