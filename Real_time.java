package bubble;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/user/apply")
public class Real_time {
	@GET
	@Path("/information/Real_time")
	//���ͪ���X�榡�A��json;�èϥΡAUTF-8���s�X
    @Produces("application/json; charset=UTF-8")
	public Response count
	(@QueryParam("place") String place ,@QueryParam("week") String week ,@QueryParam("time") String time) throws JSONException {
		NewResponse re =  new NewResponse();
		JSONObject output = new JSONObject();
		try {
			 SqlDB  s = new SqlDB();
	        ResultSet rs;
	        /*���椺�e*/
	        rs = s.stat.executeQuery
	        		("select count(*) as �ӽЦ��� "
	        				+ "from apply where placeid = '"
	        				+ 	place+"' and week = '"+week+"' and timecode = '"+time+"'");
	        while(rs.next()) {
	        	output = new JSONObject();
	        	output.put("�ӽЦ���",rs.getString(1));
	        	output.put("statuscode", "200");
	        }
	        /*�����귽*/
            rs.close();
            s.stat.close();
            re.setResponse(output.toString());
            return re.builder.build();
		}catch (SQLException e) {
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
