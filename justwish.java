import java.util.Scanner; 
class Node{
    String wish;
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
public class Apply_choose{
    String term;//�]�w�Ǵ�
    String kind;//�]�w�y��
    //�t���ơA�T�w��վ�TEAM���}�C�j�p
    int all_deptid ;
            
    //�b�����a�Ƭ� ���a*�ɬq��*�P��(�@�줭)
    int all_place;
    
    //�b���`���@��
    int all_wish;
    public Response apply_choose(String input)throws Exception{
        JSONObject output = new JSONObject();
	    NewResponse re =  new NewResponse();
        try {
    	    SqlDB  s = new SqlDB();
	        ResultSet rs;
	        /*���椺�e*/
	        //�ѪR json �r��Amessage ��json�r��
	        JSONObject sendin = new JSONObject(input);
	        
	        /*���������*/
	        term = sendin.getString("term");
            kind = sendin.getString("kind");
        
        
            all_deptid = stat.executeQuery("select count(*) as �ƥ�"+
            "from ( select tmpapply.ballid from tmpapply , account"+
            "where tmpapply.ballid = account.ballid and term = "+term+"and account.kind ='"+kind"'"+
            "group by tmpapply.ballid) as A");
            
            //�b�����a�Ƭ� ���a*�ɬq��*�P��(�@�줭)
            all_place = 3*2*5;
        
            //�b���`���@��
            all_wish = (int)stat.executeQuery("select count(*) as ���@�Ǽ� from tmpapply,account"+
            "where tmpapply.ballid = account.ballid and tmpapply.ballid='h' and tmpapply.term ="+term+"and account.kind ='"+kind+"'"
            );
        
            Node[] allwish = new Node[all_wish];//�Ҧ����@�Ǫ�������T����
            Team[] allteam = new Team[all_deptid];//�t���N�X(���X�Өt��)
            Location[] allplace = new Location[all_place];//���h�ֳ��a�A�H�γQ��L�X��
            Record[] final_wish = new Record[all_deptid];//�̲׿�X���G
            init();
            ballot();
            int i = 0;
            if(allget()==1){ 
                //��X
                output.put("statuscode", "200");
                output.put("message", "���@���\");
                while(final_wish[i]!=null){
                    output = new JSONObject();
                    output.put("account",final_wish[i].account );
                    output.put("���@��", final_wish[i].local);
                }
            }else(){
                //��X���~
                output.put("statuscode", "500");
            	output.put("message", "���A�����~");
            }
            /*�����귽*/
          rs.close();
          s.stat.close();
          re.setResponse(output.toString());
          return re.builder.build();
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
    /*��l��*/
    public void init(){
        /*�������@�ǩ�J��J���O SQL*/
        //��l�� allwish(�Ҧ����@�ǩ�m)
        rs = stat.executeQuery("select tmpapply.ballid,tmpapply.wish,tmpapply.placeid,tmpapply.week"+
            "from tmpapply,account"+"where tmpapply.ballid = account.ballid and tmpapply='h' and term = "+term+" and account.kind = '"+kind+"'"+
            "order by wish,placeid,week,timecode");
        for(int i=0;i<all_wish;i++){
            if(rs.next()){//�٦�
                allwish[i].account = rs.getString(1);
                allwish[i].wish = rs.getString(2);
                allwish[i].place = rs.getString(3)+rs.getString(4)+rs.getString(5);
                //placeid + week + timecode 
            }
        }
        
        //��l��ballid
        rs = stat.executeQuery("select ballid from tmpapply group by ballid");
        for(int i=0;i<all_deptid;i++){
            if(rs.next()){//�٦�
                allteam[i].account = rs.getString(1);
            }
        }
        
        //��l�Ƴ��a�γQ��ܼ�(�@�}�l���s)
        rs = stat.executeQuery("select placeid,week,timecode from tmp_place where side='h' and term ="+term);
        for(int i=0;i<all_place;i++){
            if(rs.next()){//�٦�
                allplace[i].loca = rs.getString(1)+rs.getString(2)+rs.getString(3);
                allplace[i].check = 0;
            }
        }
    }
    
    /*����*/
    public void ballot(){
        //��Ҧ��b���ԥX�ýƻs��s��array�� 
        String[] account_check = new String[all_deptid];
        for(int i=0;i<all_deptid;i++){//��J�Ҧ���account
            account_check[i] = all_deptid[i].account;
        }
        //��������(�~�P)
        for(int i=0;i<1000;i++){
            c1 = rand()%all_deptid;
            c2 = rand()%all_deptid;
            tmp = account_check[c1];
            account_check[c1] = account_check[c2];
            account_check[c2] = tmp;
            
        }
        
        
        //�q�Y�}�l��o���@��
        int now = 1;//�ثe�ҽ������@��(����wish�������@)
        int wishget = 3;//�C�t��i�񪺧��@�Ǽ�
        int limit = 1;//�C�@�ӳ��̦h�i�H�Q���o������
        while(now != wishget){//���Ʀ��ʧ@ ����wish�����Q���� �B �C�Өt��Ҧ����a
            for(int i=0; i<all_deptid; i++){//��t��ƺ��ɴ��U�@��wish
                final_wish[i].account = account_check[i];
                
                /*�P�_�O�_�w�����L���a*/
                //�S�� ����a�ݩ���Өt��O���U�� �øѼаO�ӳ��a�w�Q��L
                if(judgedeptid(String account)!= 1){//�Өt���èS����L  
                    for(int j=0;j<all_wish;i++){
                        if(allwish[j].account == final_wish[i].account && allwish[j].wish==now){
                            if(judgeplace(allwish[i].account == 1)){
                                i++;
                                break;
                            }
                            final_wish.[i].loca = allwish[i].place;//�̲׬����������Өt����o�����a
                            for(int k=0; k<all_place; k++){
                                if(allplace[k].local == final_wish.[i].loca){
                                        allplace[k].check = allplace[k].check+1;
                                }
                            }
                        }
                    }
                }else{//���h���L
                    i++;
                }
            }
            now++;
        }
    }
    public int judgedeptid(String account){//�P�_ 
        //1.�t���O�_������o
        for(int i=0;i<all_deptid;i++){
            if(final_wish[i].account==account){
                return 1;
            }else if(final_wish[i].account==null){
                break;
            }
        }
        //�W���ⶵ�Y�ŦX�h������o(����J�}�C��)
    }
    public int judgeplace(String place,int limit){
        //2.���a�O�_�B��(�̦h���G �i��0.1.2) **�ݬ����ثe��L���t����
        for(int i=0;i<all_place;i++){
            if(allplace[i].place == place && allplace[i].check>=limit){//���a�B��
                return 1;
            }else{
                return 0;
            }
        }
    }
    public int allget(){//�ˬd�O�_�����t���������a
        for(int i=0;i<all_deptid;i++){
            if(final_wish[i].loca==null){
                give(int i);//�S���A�H������
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
}