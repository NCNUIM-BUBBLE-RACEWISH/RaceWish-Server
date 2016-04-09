package bubble;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/apply")
public class apply {
	@POST
    @Path("/json")
    //���ͪ���X�榡�A��json;�èϥΡAUTF-8���s�X
    @Consumes("application/json; charset=UTF-8")
    public String writein(String message) {
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
	        JSONObject sendin = new JSONObject(message);
	        /*���������*/
	        String code = sendin.getString("code");
	        String ballid = sendin.getString("ballid");
	        String wish = sendin.getString("wish");
	        String week = sendin.getString("week");
	        String placeid = sendin.getString("placeid");
	        String timecode = sendin.getString("timecode");
	        String term = sendin.getString("term");
	        /*��J��Ʈw��*/
	        stat.executeUpdate
	        ("insert into apply (code,ballid,wish,placeid,week,timecode,term)"
	        		+" values('"+code+"','"+ballid+"','"+wish+"','"+placeid+"','"+week
	        		+"','"+timecode+"','"+term+"'); ");
	        /*�^�ǩҿ諸�ɶ�*/
	        rs = stat.executeQuery("select * from apply where ballid = '"+ballid+"' ");
	        JSONArray result = new JSONArray();
	        while(rs.next()) {
	            JSONObject tmp = new JSONObject();
	            tmp.put("�N�X",rs.getString(1));
	            tmp.put("�b��",rs.getString(2));
	            tmp.put("���@",rs.getString(3));
	            tmp.put("���a",rs.getString(4));
	            tmp.put("�P��",rs.getString(5));
	            tmp.put("�ɶ��N�X",rs.getString(6));
	            tmp.put("�Ǵ�",rs.getString(7));
	            result.put(tmp);
	        }
        /*�����귽*/
        rs.close();
        stat.close();
        return result.toString();
        /*���*/
		} catch(JSONException err) {
            	return err.getMessage();
		} catch(Exception err) {
            	return err.getMessage();
		}
	}
}
