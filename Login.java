/*�ϥΪ̱b��&�K�X�ǤJ*/
//�ѽX
/*��X��Ʈw���O�_���۹��������*/
//���^��Yes
//�S���^��NO
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
 //���ͪ���X�J�榡�A��json;�èϥΡAUTF-8���s�X
   @Consumes("application/json; charset=UTF-8")
  public Response login(String input) throws Exception{
	  JSONObject output = new JSONObject();
	   NewResponse re =  new NewResponse();
      try {
    	  SqlDB  s = new SqlDB();
	        ResultSet rs;
	        /*���椺�e*/
	        //�ѪR json �r��Amessage ��json�r��
	        JSONObject sendin = new JSONObject(input);
	        
	        /*���������*/
	        String kind = sendin.getString("kind");
            String deptid = sendin.getString("deptid");
	        String passwd = sendin.getString("passwd");
	        String userid = deptid+kind;
	        /*�q��Ʈw������һݸӱb�����K�X*/
          rs = s.stat.executeQuery("select passwd from account where ballid = '"+userid+"'");
	        /*�O�_�۲šA�è̷Ӭ۲� or not �^�Ǫ��A*/
          int i = 0;
          while(rs.next()){
        	  output = new JSONObject();
        	  i++;
        	  if(rs.getString(1).equals(passwd)){
                  output.put("account",userid );
                  output.put("passwd", passwd);
                  output.put("statuscode", "200");
          		  output.put("message", "���\�n�J");
               }else{
            	   output.put("statuscode", "401");
            	   output.put("message", "�b���αK�X���~");
               }  
          }
          if(i== 0){
        	  output = new JSONObject();
        	  output.put("statuscode", "401");
       	      output.put("message", "�b���αK�X���~");
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
