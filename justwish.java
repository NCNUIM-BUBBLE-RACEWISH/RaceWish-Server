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
    String term;//設定學期
    String kind;//設定球種
    //系隊數，確定後調整TEAM的陣列大小
    int all_deptid ;
            
    //半場場地數為 場地*時段數*星期(一到五)
    int all_place;
    
    //半場總志願數
    int all_wish;
    public Response apply_choose(String input)throws Exception{
        JSONObject output = new JSONObject();
	    NewResponse re =  new NewResponse();
        try {
    	    SqlDB  s = new SqlDB();
	        ResultSet rs;
	        /*執行內容*/
	        //解析 json 字串，message 為json字串
	        JSONObject sendin = new JSONObject(input);
	        
	        /*抓對應的值*/
	        term = sendin.getString("term");
            kind = sendin.getString("kind");
        
        
            all_deptid = stat.executeQuery("select count(*) as 數目"+
            "from ( select tmpapply.ballid from tmpapply , account"+
            "where tmpapply.ballid = account.ballid and term = "+term+"and account.kind ='"+kind"'"+
            "group by tmpapply.ballid) as A");
            
            //半場場地數為 場地*時段數*星期(一到五)
            all_place = 3*2*5;
        
            //半場總志願數
            all_wish = (int)stat.executeQuery("select count(*) as 志願序數 from tmpapply,account"+
            "where tmpapply.ballid = account.ballid and tmpapply.ballid='h' and tmpapply.term ="+term+"and account.kind ='"+kind+"'"
            );
        
            Node[] allwish = new Node[all_wish];//所有志願序的相關資訊紀錄
            Team[] allteam = new Team[all_deptid];//系隊代碼(有幾個系隊)
            Location[] allplace = new Location[all_place];//有多少場地，以及被選過幾個
            Record[] final_wish = new Record[all_deptid];//最終輸出結果
            init();
            ballot();
            int i = 0;
            if(allget()==1){ 
                //輸出
                output.put("statuscode", "200");
                output.put("message", "志願成功");
                while(final_wish[i]!=null){
                    output = new JSONObject();
                    output.put("account",final_wish[i].account );
                    output.put("志願序", final_wish[i].local);
                }
            }else(){
                //輸出錯誤
                output.put("statuscode", "500");
            	output.put("message", "伺服器錯誤");
            }
            /*關閉資源*/
          rs.close();
          s.stat.close();
          re.setResponse(output.toString());
          return re.builder.build();
        } catch (SQLException e) {
        	output.put("statuscode", "500");
			output.put("message","伺服器錯誤");
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
	        return re.builder.build();
		}
    }
    /*初始化*/
    public void init(){
        /*全部志願序放入放入指令 SQL*/
        //初始化 allwish(所有志願序放置)
        rs = stat.executeQuery("select tmpapply.ballid,tmpapply.wish,tmpapply.placeid,tmpapply.week"+
            "from tmpapply,account"+"where tmpapply.ballid = account.ballid and tmpapply='h' and term = "+term+" and account.kind = '"+kind+"'"+
            "order by wish,placeid,week,timecode");
        for(int i=0;i<all_wish;i++){
            if(rs.next()){//還有
                allwish[i].account = rs.getString(1);
                allwish[i].wish = rs.getString(2);
                allwish[i].place = rs.getString(3)+rs.getString(4)+rs.getString(5);
                //placeid + week + timecode 
            }
        }
        
        //初始化ballid
        rs = stat.executeQuery("select ballid from tmpapply group by ballid");
        for(int i=0;i<all_deptid;i++){
            if(rs.next()){//還有
                allteam[i].account = rs.getString(1);
            }
        }
        
        //初始化場地及被選擇數(一開始為零)
        rs = stat.executeQuery("select placeid,week,timecode from tmp_place where side='h' and term ="+term);
        for(int i=0;i<all_place;i++){
            if(rs.next()){//還有
                allplace[i].loca = rs.getString(1)+rs.getString(2)+rs.getString(3);
                allplace[i].check = 0;
            }
        }
    }
    
    /*抽籤*/
    public void ballot(){
        //把所有帳號拉出並複製到新的array裡 
        String[] account_check = new String[all_deptid];
        for(int i=0;i<all_deptid;i++){//放入所有的account
            account_check[i] = all_deptid[i].account;
        }
        //全部打亂(洗牌)
        for(int i=0;i<1000;i++){
            c1 = rand()%all_deptid;
            c2 = rand()%all_deptid;
            tmp = account_check[c1];
            account_check[c1] = account_check[c2];
            account_check[c2] = tmp;
            
        }
        
        
        //從頭開始獲得志願序
        int now = 1;//目前所輪的志願序(此時wish全部為一)
        int wishget = 3;//每系對可填的志願序數
        int limit = 1;//每一個場最多可以被取得的次數
        while(now != wishget){//重複此動作 直到wish全部被拿完 且 每個系對皆有場地
            for(int i=0; i<all_deptid; i++){//當系對數滿時換下一個wish
                final_wish[i].account = account_check[i];
                
                /*判斷是否已有拿過場地*/
                //沒有 把場地屬於哪個系對記錄下來 並解標記該場地已被選過
                if(judgedeptid(String account)!= 1){//該系隊並沒有選過  
                    for(int j=0;j<all_wish;i++){
                        if(allwish[j].account == final_wish[i].account && allwish[j].wish==now){
                            if(judgeplace(allwish[i].account == 1)){
                                i++;
                                break;
                            }
                            final_wish.[i].loca = allwish[i].place;//最終紀錄中紀錄該系隊獲得的場地
                            for(int k=0; k<all_place; k++){
                                if(allplace[k].local == final_wish.[i].loca){
                                        allplace[k].check = allplace[k].check+1;
                                }
                            }
                        }
                    }
                }else{//有則跳過
                    i++;
                }
            }
            now++;
        }
    }
    public int judgedeptid(String account){//判斷 
        //1.系隊是否重複獲得
        for(int i=0;i<all_deptid;i++){
            if(final_wish[i].account==account){
                return 1;
            }else if(final_wish[i].account==null){
                break;
            }
        }
        //上面兩項若符合則不能獲得(不放入陣列中)
    }
    public int judgeplace(String place,int limit){
        //2.場地是否額滿(最多為二 可為0.1.2) **需紀錄目前選過的系隊數
        for(int i=0;i<all_place;i++){
            if(allplace[i].place == place && allplace[i].check>=limit){//場地額滿
                return 1;
            }else{
                return 0;
            }
        }
    }
    public int allget(){//檢查是否全部系隊都有場地
        for(int i=0;i<all_deptid;i++){
            if(final_wish[i].loca==null){
                give(int i);//沒有，隨機給場
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