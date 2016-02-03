package bubble;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/apply_static")
public class apply_static {
	@GET
	@Path("/json")
	//���ͪ���X�榡�A��json;�èϥΡAUTF-8���s�X
    @Produces("application/json; charset=UTF-8")
	public String much(@MatrixParam("week") @DefaultValue("�@") String week){
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
	        rs = stat.executeQuery
	        		("select timecode,count(*) as �ӽЦ���"
	        				+ " from apply "
	        				+ "where week = '"
	        				+ week+"' group by timecode ");
	        JSONArray result = new JSONArray();
            while(rs.next()) {
                JSONObject tmp = new JSONObject();
                tmp.put("�ɬq",rs.getString(1));
                tmp.put("�ӽЦ���",rs.getString(2));
                result.put(tmp);
            }
	        /*�����귽*/
            rs.close();
            stat.close();
            return result.toString();
		}catch(JSONException err) {
            return err.getMessage();
        } catch(Exception err) {
            return err.getMessage();
        }
	}
}
