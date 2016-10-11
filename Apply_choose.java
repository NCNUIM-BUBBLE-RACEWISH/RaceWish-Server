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
    String term;//設定學期
    String kind;//設定球種
    //系隊數，確定後調整TEAM的陣列大小
    int all_deptid ;
            
    //半場場地數為 場地*時段數*星期(一到五)
    int all_place;
    
    //半場總志願數
    int all_wish;
    
    Node[] allwish;
    Team[] allteam ;
    Location[] allplace;
    Record[] final_wish ;
    
    int tail = 0;//紀錄擺放的尾端
    //account_check 交換順序
    String[] account_check;
    @POST
    @Path("/ballot")
    @Consumes("application/json; charset=UTF-8")
    //產生的輸出格式，為json;並使用，UTF-8的編碼
    @Produces("application/json; charset=UTF-8")
    public Response apply_choose(String input)throws Exception{
    	JSONObject output = new JSONObject();
	    NewResponse re =  new NewResponse();
        try {
    	    SqlDB  s = new SqlDB();
	        ResultSet rs = null;
	        /*執行內容*/
	        //解析 json 字串，message 為json字串
	        JSONObject sendin = new JSONObject(input);
	        JSONArray result = new JSONArray();
	        /*抓對應的值*/
	        term = sendin.getString("term");
            kind = sendin.getString("kind");
            
            rs = s.stat.executeQuery("select count(*) as 數目  from ( select tmpapply.ballid from tmpapply , account  where tmpapply.ballid = account.ballid and term = "
            		+ term +" and account.kind = '"+kind+"'  group by tmpapply.ballid) as A");
            while(rs.next()) {
            	all_deptid = rs.getInt(1);
            }
            
            //半場場地數為 場地*時段數*星期(一到五)
            all_place = 17;
            
            //半場總志願數
            rs = s.stat.executeQuery("select count(*) as 志願序數 from tmpapply , account "+
            "where tmpapply.ballid = account.ballid and tmpapply.side='h' and tmpapply.term ="+term+" and account.kind ='"+kind+"'"
            );
            
            while(rs.next()) {
            	all_wish = rs.getInt(1);
            }
            
            allwish = new Node[all_wish];//所有志願序的相關資訊紀錄
            allteam = new Team[all_deptid];//系隊代碼(有幾個系隊)
            allplace = new Location[all_place];//有多少場地，以及被選過幾個
            final_wish = new Record[all_deptid];//最終輸出結果

            //初始化所有基礎型態 NODE TEAM LOCATION RECORD
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
            /*全部志願序放入放入指令 SQL*/
            //初始化 allwish(所有志願序放置)
            rs = s.stat.executeQuery("select tmpapply.ballid,tmpapply.wish,tmpapply.placeid,tmpapply.week,tmpapply.timecode "+
                "from tmpapply,account "+"where tmpapply.ballid = account.ballid and tmpapply.side='h' and term = "+term+" and account.kind = '"+kind+"' "+
                "order by wish,placeid,week,timecode");


            int r=0;
            while(rs.next()){//還有
            	if(r>=all_wish){
            		break;
            	}
                allwish[r].account = rs.getString(1);
                allwish[r].wish = rs.getInt(2);
                allwish[r].place = rs.getString(3)+rs.getString(4)+rs.getString(5);
                //placeid + week + timecode
                r++;
            }

            //初始化ballid
            rs = s.stat.executeQuery("select ballid from tmpapply group by ballid");
            for(int i=0;i<all_deptid;i++){
                if(rs.next()){//還有
                    allteam[i].account = rs.getString(1);
                }
            }

            //初始化場地及被選擇數(一開始為零)
            rs = s.stat.executeQuery("select placeid,week,timecode from tmp_place where side='h' and term ="+term);

            for(int i=0;i<all_place;i++){
                if(rs.next()){//還有
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
                //輸出
                output.put("statuscode", "200");
                output.put("message", "志願成功");
                result.put(output);

                while(i<all_deptid && final_wish[i]!= null){
                	
                    output = new JSONObject();
                    output.put("account",final_wish[i].account );
                    output.put("志願序", final_wish[i].loca);
                    result.put(output);
                    
                    System.out.println(i);
                    System.out.println(final_wish[i].account);
                    System.out.println(final_wish[i].loca);
                    i++;
                }

            }else{
                //輸出錯誤
                output.put("statuscode", "5001");
            	output.put("message", "伺服器錯誤1");
            	System.out.println("error");
            }
            /*關閉資源*/
          rs.close();
          s.stat.close();
          re.setResponse(result.toString());
          return re.builder.build();
        } catch (SQLException e) {
        	output.put("statuscode", "500");
			output.put("message","SQL錯誤");
			re.setResponse(output.toString());
	        return re.builder.build();
		} catch (JSONException e) {
			output.put("statuscode", "400");
			output.put("message","錯誤要求");
			re.setResponse(output.toString());
	        return re.builder.build();
		}catch(Exception err){
			output.put("statuscode", "500");
			output.put("message","伺服器錯誤");
			re.setResponse(output.toString());
			err.printStackTrace();
	        return re.builder.build();
		}
    }
    
    /*抽籤*/
    public void ballot(){
        //把所有帳號拉出並複製到新的array裡 
    	account_check = new String[all_deptid];
        for(int i=0;i<all_deptid;i++){//放入所有的account
            account_check[i] = allteam[i].account;
        }
        //全部打亂(洗牌)
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
        //從頭開始獲得志願序
        int now = 1;//目前所輪的志願序(此時wish全部為一)
        int wishget = 3;//每系對可填的志願序數
        int limit = 1;//每一個場最多可以被取得的次數


        while(now <= wishget){//重複此動作 直到wish全部被拿完 且 每個系對皆有場地
        	int l = 0;//紀錄目前所取到的志願序
            for(int i=0; i<all_deptid; i++){//當系對數滿時換下一個wish
                if(l>=all_deptid){
                    break;
                }
            	String tmp = account_check[l];

                /*判斷是否已有拿過場地*/
                //沒有 把場地屬於哪個系對記錄下來 並解標記該場地已被選過

                if(judgedeptid(tmp)!= 1){//該系隊並沒有選過
                	final_wish[i].account = account_check[l];//l為領取wish的順續

                    for(int j=0;j<all_wish;j++){
                    	
                    	//這邊的動作為找到對應的wish並記錄他所給予的場地
                        if(allwish[j].account.equals(final_wish[i].account) && allwish[j].wish==now){
                        	//場地到達上限，有到達上限則不給予
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
                            //沒有到達上限，給予
                            //最終紀錄中紀錄該系隊獲得的場地
                            final_wish[i].loca = allwish[j].place;
                            tail++;
                            //紀錄場地取得數
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
                }else{//有則跳過
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
    
    public int judgedeptid(String account){//判斷 
        //1.系隊是否重複獲得
        for(int i=0;i<all_deptid;i++){
            if(account.equals(final_wish[i].account)){
            	return 1;
            }else if(final_wish[i].account!= null){
            	continue;
            }else{
            	break;
            }
        }
        //上面兩項若符合則不能獲得(不放入陣列中)
        return 0;
    }
    
    public int judgeplace(String place,int limit){
        //2.場地是否額滿(最多為二 可為0.1.2) **需紀錄目前選過的系隊數
        for(int i=0;i<all_place;i++){
            if(place.equals(allplace[i].loca) && allplace[i].check>=limit){//場地額滿
                return 1;
            }
        }
        System.out.println("judgeplaceFN");
        return 0;
    }
    public int allget(){//檢查是否全部系隊都有場地
        for(int i=0;i<all_deptid;i++){
            if(final_wish[i].loca==null){
                give(i);//沒有，隨機給場
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