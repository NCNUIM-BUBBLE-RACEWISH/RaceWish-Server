package bubble;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
class Node{
    int wish;
    String account;
    String place;//timecode+placeid+week
}
class Team{
    String account;
}
class Location{
    String loca;
    int check;
}
class Record{
    String account;
    String loca;
}

@Path("/user/apply")
public class Apply_choose{
    String term;//�]�w�Ǵ�
    String kind;//�]�w�y��
    //�t���ơA�T�w��վ�TEAM���}�C�j�p
    int all_deptid ;
            
    //�b�����a�Ƭ� ���a*�ɬq��*�P��(�@�줭)
    int all_place;
    
    //�b���`���@��
    int all_wish;
    
    Node[] allwish;
    Team[] allteam ;
    Location[] allplace;
    Record[] final_wish ;
    
    int tail = 0;//�����\�񪺧���
    //account_check �洫����
    String[] account_check;
    @POST
    @Path("/ballot")
    @Consumes("application/json; charset=UTF-8")
    //���ͪ���X�榡�A��json;�èϥΡAUTF-8���s�X
    @Produces("application/json; charset=UTF-8")
    public Response apply_choose(String input)throws Exception{
    	JSONObject output = new JSONObject();
	    NewResponse re =  new NewResponse();
        try {
    	    SqlDB  s = new SqlDB();
	        ResultSet rs = null;
	        /*���椺�e*/
	        //�ѪR json �r��Amessage ��json�r��
	        JSONObject sendin = new JSONObject(input);
	        JSONArray result = new JSONArray();
	        /*���������*/
	        term = sendin.getString("term");
            kind = sendin.getString("kind");
            
            rs = s.stat.executeQuery("select count(*) as �ƥ�  from ( select tmpapply.ballid from tmpapply , account  where tmpapply.ballid = account.ballid and term = "
            		+ term +" and account.kind = '"+kind+"'  group by tmpapply.ballid) as A");
            while(rs.next()) {
            	all_deptid = rs.getInt(1);
            }
            
            //�b�����a�Ƭ� ���a*�ɬq��*�P��(�@�줭)
            all_place = 17;
            
            //�b���`���@��
            rs = s.stat.executeQuery("select count(*) as ���@�Ǽ� from tmpapply , account "+
            "where tmpapply.ballid = account.ballid and tmpapply.side='h' and tmpapply.term ="+term+" and account.kind ='"+kind+"'"
            );
            
            while(rs.next()) {
            	all_wish = rs.getInt(1);
            }
            
            allwish = new Node[all_wish];//�Ҧ����@�Ǫ�������T����
            allteam = new Team[all_deptid];//�t���N�X(���X�Өt��)
            allplace = new Location[all_place];//���h�ֳ��a�A�H�γQ��L�X��
            final_wish = new Record[all_deptid];//�̲׿�X���G

            //��l�ƩҦ���¦���A NODE TEAM LOCATION RECORD
            for(int i=0;i<all_wish;i++){
            	allwish[i] = new Node();
            }
            for(int i=0;i<all_deptid;i++){
            	allteam[i] = new Team();
            }
            for(int i=0;i<all_place;i++){
            	allplace[i] = new Location();
            }
            for(int i=0;i<all_deptid;i++){
            	final_wish[i] = new Record();
            }
            
            
            //final_wish[i].local = "ok";
           // System.out.println(final_wish[i].local);
            /*�������@�ǩ�J��J���O SQL*/
            //��l�� allwish(�Ҧ����@�ǩ�m)
            rs = s.stat.executeQuery("select tmpapply.ballid,tmpapply.wish,tmpapply.placeid,tmpapply.week,tmpapply.timecode "+
                "from tmpapply,account "+"where tmpapply.ballid = account.ballid and tmpapply.side='h' and term = "+term+" and account.kind = '"+kind+"' "+
                "order by wish,placeid,week,timecode");


            int r=0;
            while(rs.next()){//�٦�
            	if(r>=all_wish){
            		break;
            	}
                allwish[r].account = rs.getString(1);
                allwish[r].wish = rs.getInt(2);
                allwish[r].place = rs.getString(3)+rs.getString(4)+rs.getString(5);
                //placeid + week + timecode
                r++;
            }

            //��l��ballid
            rs = s.stat.executeQuery("select ballid from tmpapply group by ballid");
            for(int i=0;i<all_deptid;i++){
                if(rs.next()){//�٦�
                    allteam[i].account = rs.getString(1);
                }
            }

            //��l�Ƴ��a�γQ��ܼ�(�@�}�l���s)
            rs = s.stat.executeQuery("select placeid,week,timecode from tmp_place where side='h' and term ="+term);

            for(int i=0;i<all_place;i++){
                if(rs.next()){//�٦�
                    allplace[i].loca = rs.getString(1)+rs.getString(2)+rs.getString(3);
                    allplace[i].check = 0;
                }
            }

            ballot();
            c_detpid();
            int i = 0;
            for(int x=0;x<all_deptid;x++){
            	System.out.println("finalf: "+final_wish[x].account);
            }
            if(allget()==1){ 
                //��X
                output.put("statuscode", "200");
                output.put("message", "���@���\");
                result.put(output);

                while(i<all_deptid && final_wish[i]!= null){
                	
                    output = new JSONObject();
                    output.put("account",final_wish[i].account );
                    output.put("���@��", final_wish[i].loca);
                    result.put(output);
                    
                    System.out.println(i);
                    System.out.println(final_wish[i].account);
                    System.out.println(final_wish[i].loca);
                    i++;
                }

            }else{
                //��X���~
                output.put("statuscode", "5001");
            	output.put("message", "���A�����~1");
            	System.out.println("error");
            }
            /*�����귽*/
          rs.close();
          s.stat.close();
          re.setResponse(result.toString());
          return re.builder.build();
        } catch (SQLException e) {
        	output.put("statuscode", "500");
			output.put("message","SQL���~");
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
			err.printStackTrace();
	        return re.builder.build();
		}
    }
    
    /*����*/
    public void ballot(){
        //��Ҧ��b���ԥX�ýƻs��s��array�� 
    	account_check = new String[all_deptid];
        for(int i=0;i<all_deptid;i++){//��J�Ҧ���account
            account_check[i] = allteam[i].account;
        }
        //��������(�~�P)
        int c1;
        int c2;
        for(int i=0;i<1000;i++){
            c1 = ((int)(Math.random()*1000+1))% all_deptid;
            c2 = ((int)(Math.random()*1000+1))% all_deptid;
            String tmp = account_check[c1];
            account_check[c1] = account_check[c2];
            account_check[c2] = tmp;
           
        }
        for(int i=0; i < all_deptid; i++){
            System.out.println(account_check[i]);
        }
        //�q�Y�}�l��o���@��
        int now = 1;//�ثe�ҽ������@��(����wish�������@)
        int wishget = 3;//�C�t��i�񪺧��@�Ǽ�
        int limit = 1;//�C�@�ӳ��̦h�i�H�Q���o������


        while(now <= wishget){//���Ʀ��ʧ@ ����wish�����Q���� �B �C�Өt��Ҧ����a
        	int l = 0;//�����ثe�Ҩ��쪺���@��
            for(int i=0; i<all_deptid; i++){//��t��ƺ��ɴ��U�@��wish
                if(l>=all_deptid){
                    break;
                }
            	String tmp = account_check[l];

                /*�P�_�O�_�w�����L���a*/
                //�S�� ����a�ݩ���Өt��O���U�� �øѼаO�ӳ��a�w�Q��L

                if(judgedeptid(tmp)!= 1){//�Өt���èS����L
                	final_wish[i].account = account_check[l];//l�����wish������

                    for(int j=0;j<all_wish;j++){
                    	
                    	//�o�䪺�ʧ@����������wish�ðO���L�ҵ��������a
                        if(allwish[j].account.equals(final_wish[i].account) && allwish[j].wish==now){
                        	//���a��F�W���A����F�W���h������
                        	if(judgeplace(allwish[j].place,limit)== 1){
                        		if(now!=1){
                                    i++;
                                    l++;
                                }else{
                                    i--;
                                    l++;
                                }
                                break;
                            }
                            //�S����F�W���A����
                            //�̲׬����������Өt����o�����a
                            final_wish[i].loca = allwish[j].place;
                            tail++;
                            //�������a���o��
                            for(int k=0; k<all_place; k++){
                                if(allplace[k].loca.equals(final_wish[i].loca)){
                                        allplace[k].check = (allplace[k].check)+1;
                                        System.out.println("account: "+final_wish[i].account);
                                        System.out.println("place get: "+allwish[j].place);
                                        System.out.println(" i: "+i);
                                        break;
                                }
                            }
                            break;
                        }else{
                        	continue;
                        }
                    }
                }else{//���h���L
                	if(now!=1){
                        i++;
                        l++;
                    }else{
                        i--;
                        l++;
                    }
                }
            }
            now++;
            
        }
    }
    //ballot finish
    
    public int judgedeptid(String account){//�P�_ 
        //1.�t���O�_������o
        for(int i=0;i<all_deptid;i++){
            if(account.equals(final_wish[i].account)){
            	return 1;
            }else if(final_wish[i].account!= null){
            	continue;
            }else{
            	break;
            }
        }
        //�W���ⶵ�Y�ŦX�h������o(����J�}�C��)
        return 0;
    }
    
    public int judgeplace(String place,int limit){
        //2.���a�O�_�B��(�̦h���G �i��0.1.2) **�ݬ����ثe��L���t����
        for(int i=0;i<all_place;i++){
            if(place.equals(allplace[i].loca) && allplace[i].check>=limit){//���a�B��
                return 1;
            }
        }
        System.out.println("judgeplaceFN");
        return 0;
    }
    public int allget(){//�ˬd�O�_�����t���������a
        for(int i=0;i<all_deptid;i++){
            if(final_wish[i].loca==null){
                give(i);//�S���A�H������
            }
        }
        return 1;
    }
    public void give(int index){
        for(int i=0;i<all_place;i++){
            if(allplace[i].check == 0){
                final_wish[index].loca = allplace[i].loca;
                allplace[i].check = allplace[i].check+1;
            }
        }
    }
    public void c_detpid(){
        for(int j=0;j<all_deptid;j++){
            if(tail > (all_deptid-1)){
            	System.out.println("b"+tail);
            	 break;
            }  
            System.out.println("F"+tail);
            for(int i=0;i<all_deptid;i++){
                if(account_check[j].equals(final_wish[i].account)){
                    break;
                }else if(i==all_deptid-1){
                	System.out.println(tail);
                    final_wish[tail].account = account_check[j];
                    tail++;
                    break;
                }
            }
        } 
    }
}