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

@Path("Search_place")
public class Search_place {
    @GET
    @Path("/json")
    //���ͪ���X�榡�A��json;�èϥΡAUTF-8���s�X
    @Produces("application/json; charset=UTF-8")
    public String stepf(@MatrixParam("target")  @DefaultValue("All")  String target) throws Exception{
        /*try & error*/
        try {
            /*��Ʈw�s��*/
            String conUrl = "jdbc:sqlserver://163.22.17.184:1433;"
                    + "databaseName=ball;"
                    + "user=Aisha;"
                    + "password=bang123!@#;";
            Connection con = null;//���񪫥�
            //���J�X�ʵ{��
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(conUrl);//con �� DB
            /*�M��귽*/
            //statement �o�e��Ʈw�R�O�A�åB����statement object
            Statement stat = con.createStatement();
            ResultSet rs;
            if(target.equals("All")){
                rs = stat.executeQuery("select useplace.timecode �ɶ��N�X,substring(useplace.placeid,2,1) ���a�N�X, department.cname �ϥΪ� "+
                "from department,useplace where department.deptid = substring(useplace.theuser,1,2)");
            }else{
           	 rs = stat.executeQuery("select useplace.timecode �ɶ��N�X , substring(useplace.placeid,2,1) ���a�N�X, department.cname �ϥΪ� from department,useplace where department.deptid = substring(useplace.theuser,1,2) and substring(department.cname,1,2) =  "+target);
         }
            JSONArray result = new JSONArray();
            while(rs.next()) {
                JSONObject tmp = new JSONObject();
                tmp.put("�ɶ��N�X",rs.getString(1));
                tmp.put("���a�N�X",rs.getString(2));
                tmp.put("�ϥΪ�",rs.getString(3));
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