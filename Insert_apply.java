package bubble;

import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
@Path("/user/apply")
public class Insert_apply {
	@POST
    @Path("/insert")
    //���ͪ���X�榡�A��json;�èϥΡAUTF-8���s�X
    @Consumes("application/json; charset=UTF-8")
    public Response writein(String message) throws JSONException {
		JSONObject output = new JSONObject();
		NewResponse re =  new NewResponse();
        try {
        	 SqlDB  s = new SqlDB();
            /*���椺�e*/
            //�ѪR json �r��Amessage ��json�r��
            JSONArray jarray = new JSONArray(message);
            String ballid = null;
            for(int i = 0; i < jarray.length(); i++) {
                /*���������*/
                JSONObject sendin = jarray.getJSONObject(i);
                ballid = sendin.getString("ballid");
                String wish = sendin.getString("wish");
                String week = sendin.getString("week");
                String placeid = sendin.getString("placeid");
                String timecode = sendin.getString("timecode");
                String term = sendin.getString("term");
                /*��J��Ʈw��*/
                s.stat.executeUpdate
                ("insert into apply (ballid,wish,placeid,week,timecode,term)"
                        +" values('"+ballid+"','"+wish+"','"+placeid+"','"+week
                        +"','"+timecode+"','"+term+"'); ");
            }
            output.put("statuscode", "200");
			output.put("message","�ӽЦ��\");
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
