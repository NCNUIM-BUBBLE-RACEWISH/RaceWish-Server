import java.util.Scanner;
class Node{
    int wish;
    String deptid;
    String place;
}
class Team{
    int deptid;
    String loca;
}
class Location{
    String loca;
    int user;
}
public class Place_result{
    int now;//計算目前到第幾個
    int d;//報名系隊的總數
    int m;//一個系隊有幾個wish
    int l;//一共有多少個場地
    int n = d*m;//總共要開的陣列   
    Team[] deptid = new Team[d];
    Location[] place = new Location[l];
    Node[] wish = new Node[n];
    
    /*找到wish相同 place相同 => 抽籤*/
    public int choose(int start){
        int count = 0;//計算單個wish有多少系隊
        for(int i = start;wish[i].place==wish[i+1].place;i++){
            count++;
        }
        //從start開始 才不會取道重複的
        int tmp = (int)(Math.random()*count+start);
        //檢查是否該個wish皆被抽過
        if(check_wish(start,count)==1){//皆被抽過
            return;
        }
        //當有已有中過就重抽
        while(check(wish[tmp].deptid,tmp) == 1){
            tmp = (int)(Math.random()*count+start);
        }
        return count+1;
    }
    /*check all wishes have already use or not*/
    public void check_wish(int start,int count){
        for(int i=start; i<count+1; i++){
            for(int j=0; j<d; j++){
                if(deptid[j].deptid.equals(wish[i].deptid)){
                    if(deptid[j].loca==null){
                        break;
                    }
                }
            }
        }
        return 1;
    }
    
    /*確認抽過與否*/
    //有return 並重抽
    //沒有 確認並標記
    public void check(String deptid,int tmp){
        for(int i=0; i<d; i++){
            if(deptid[i].deptid.equals(deptid)){
                if(deptid[i].loca == null){//沒有中過，則擺入
                    deptid[i].loca = wish[tmp].place;
                    //標記系隊 及 場地
                    for(int j=0;j<l;j++){//
                        if(place[j].place.equals(deptid[i].loca)){
                            place[j].user = deptid[i].deptid;
                        }
                    }
                    return 0;
                }else{//中過
                    return 1;
                }
            }
        }
    }
    /*全部抽完後的二次檢查*/
    public void last_check(){
        //從頭到尾檢查是否所有系隊皆有場地
        for(int i=0;i<d;i++){
            if(deptid[i].loca==null){
                //從剩餘的場地中找
                int tmp = (int)(Math.random()*l-1);
                //找到沒有人使用的場
                while(place[tmp].place != null){
                    int tmp = (int)(Math.random()*l-1); 
                }
                //標記
                deptid[i].loca = place[tmp].place;
                place[tmp].user = deptid[i].deptid;
            }
        }
    }
    public static void main(){
        //第一次抽志願序，並且做標記
        for(int i=0; now <= n; i = i+next){
            int next = choose(i);
        }
        //從deptid中找出還沒被抽過的，並且從剩餘的場地中給
        last_check();
    }
}