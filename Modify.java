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
//���ͪ���X�J�榡�A��json;�èϥΡAUTF-8���s�X
 @Consumes("application/json; charset=UTF-8")
 @Produces("application/json; charset=UTF-8")
public Response login(String input) throws Exception{
	   JSONObject output = new JSONObject();
	   NewResponse re =  new NewResponse();
    try {
    	     SqlDB  s = new SqlDB();
	        //�ѪR json �r��Amessage ��json�r��
	        JSONObject sendin = new JSONObject(input);
	        
	        /*���������*/
	        String kind = sendin.getString("kind");
	        String deptid = sendin.getString("deptid");
	        String email = sendin.getString("email");
	        String passwd = sendin.getString("passwd");
	        /*��J��Ʈw��*/
	        s.stat.executeUpdate
	       ( "UPDATE account SET passwd = '"+passwd+"' WHERE kind = '"+kind+"' AND email = '"+email+"'AND deptid = '"+deptid+"'" );
	        /*�q��Ʈw������һݧ��K�X
	        /*�O�_�۲šA�è̷Ӭ۲� or not �^�Ǫ��A*/
		    output = new JSONObject();
			output.put("passed",passwd );
		    output.put("statuscode", "200");
			output.put("message", "�K�X�w�g����");

        /*�����귽*/
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