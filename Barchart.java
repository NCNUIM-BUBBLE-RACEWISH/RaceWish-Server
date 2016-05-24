package bubble;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/user/apply")
public class Barchart {
    @GET
    @Path("/bar_chart")
    //���ͪ���X�榡�A��json;�èϥΡAUTF-8���s�X
    @Produces("application/json; charset=UTF-8")
    /*���|http://localhost:2339/bubble/wish/imcount/json;week=�T*/
    public Response much(@QueryParam("week") @DefaultValue("�@") String week) throws JSONException {
        JSONObject output = new JSONObject();
 	    NewResponse re =  new NewResponse();
 	    JSONArray result = new JSONArray();
    	try {
    		 SqlDB  s = new SqlDB();
            ResultSet rs;
            /*���椺�e*/
            rs = s.stat.executeQuery
                    ("select week,placeid,timecode,count(*)"
                            + " from apply "
                            + "where week = '"
                            + week+"' group by placeid , week , timecode ");
            while(rs.next()) {
               output = new JSONObject();
               output.put("�P��",rs.getString(1));
               output.put("���a�N�X",rs.getString(2));
               output.put("�ɬq",rs.getString(3));
               output.put("�ӽж����",rs.getString(4));
               output.put("statuscode", "200");
               result.put(output);
            }
            re.setResponse(result.toString());
            /*�����귽*/
            rs.close();
            s.stat.close();
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
