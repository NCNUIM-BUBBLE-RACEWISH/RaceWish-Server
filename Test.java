/*�ϥΪ̱b��&�K�X�ǤJ*/
//�ѽX
/*��X��Ʈw���O�_���۹��������*/
//���^��Yes
//�S���^��NO
package bubble;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;



@Path("/user/passwd")
public class Test {
   @POST
 //���ͪ���X�J�榡�A��json;�èϥΡAUTF-8���s�X
   @Consumes("application/json; charset=UTF-8")
   @Produces("application/json; charset=UTF-8")
  public Response login(String input) throws ClassNotFoundException{
	   JSONObject output = new JSONObject();
	   NewResponse re =  new NewResponse();
      try {
	        /*��Ʈw�s���ҨϥΪ��r��*/
	        String conUrl = "jdbc:sqlserver://163.22.17.184:1433;"
	                + "databaseName=ball;"
	                + "user=Aisha;"
	                + "password=bang123!@#;";
	         /*���J�X�ʵ{��*/
	        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        /*���ͳs�u*/
	        Connection con =DriverManager.getConnection(conUrl);
	        /*�M��귽: Statement�U�F���O ResultSet �O�s���G*/
	        Statement stat;
	        ResultSet rs;
	        /*�إ�Statement����*/
	        stat = con.createStatement();
	        /*���椺�e*/
	        //�ѪR json �r��Amessage ��json�r��
	        JSONObject sendin = new JSONObject(input);
	        /*���������*/
	        String kind = sendin.getString("kind");
            String deptid = sendin.getString("deptid");
	        String passwd = sendin.getString("passwd");
	        String _id = deptid+kind;
	        /*�q��Ʈw������һݸӱb�����K�X*/
          rs = stat.executeQuery
          ("select passwd from accont where ballid = '"+_id +"'");
	        /*�O�_�۲šA�è̷Ӭ۲� or not �^�Ǫ��A*/
	        if(rs.getString(1).equals(passwd)){
              output = new JSONObject();
              output.put("account",_id );
              output.put("passwd", passwd);
              re.setResponse(output.toString());
           }else{
        	   output.put("message", "wrong password or Wrong Account");
           }
          /*�����귽*/
          rs.close();
          stat.close();
      /*���*/
		}catch(IllegalArgumentException e) {   
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return re.builder.build();
  }

}
