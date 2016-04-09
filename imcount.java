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

@Path("/wish/imcount")
public class imcount {
    @GET
    @Path("/json")
    //���ͪ���X�榡�A��json;�èϥΡAUTF-8���s�X
    @Produces("application/json; charset=UTF-8")
    /*���|http://localhost:2339/bubble/wish/imcount/json;week=�T*/
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
                    ("select week,placeid,timecode,count(*)"
                            + " from apply "
                            + "where week = '"
                            + week+"' group by placeid , week , timecode ");
            JSONArray result = new JSONArray();
            while(rs.next()) {
                JSONObject tmp = new JSONObject();
                tmp.put("�P��",rs.getString(1));
                tmp.put("���a�N�X",rs.getString(2));
                tmp.put("�ɬq",rs.getString(3));
                tmp.put("�ӽж����",rs.getString(4));
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