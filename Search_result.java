package bubble;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/Search")
public class Search_result {
    @GET
    @Path("/result")
    //���ͪ���X�榡�A��json;�èϥΡAUTF-8���s�X
    @Produces("application/json; charset=UTF-8")
    /* Search_place/json;target='����'*/
    public Response stepf() throws Exception{
        JSONObject output = new JSONObject();
        NewResponse re =  new NewResponse();
        JSONArray result = new JSONArray();
        /*try & error*/
        try {
        	 SqlDB  s = new SqlDB();
            ResultSet rs;
            rs =s. stat.executeQuery("select useplace.timecode �ɶ��N�X,week �P��,substring(useplace.placeid,2,1) ���a�N�X, department.cname �ϥΪ�,ballkind.cname �y��,useplace.theuser �b�� ,useplace.term "+
            "from department,useplace,account,ballkind where department.deptid = substring(useplace.theuser,1,2) and useplace.theuser = account.ballid and account.kind = ballkind.kind");
            while(rs.next()) {
                output = new JSONObject();
                output.put("�ɶ��N�X",rs.getString(1));
                output.put("�P��",rs.getString(2));
                output.put("���a�N�X",rs.getString(3));
                output.put("�ϥΪ�",rs.getString(4));
                output.put("�y��",rs.getString(5));
                output.put("�b��",rs.getString(6));
                output.put("�Ǵ�",rs.getString(7));
                output.put("statuscode", "200");
                result.put(output);
            }
            /*�����귽*/
            rs.close();
            s.stat.close();
            re.setResponse(result.toString());
            return re.builder.build();
        /*���*/
        } catch(SQLException err) {
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