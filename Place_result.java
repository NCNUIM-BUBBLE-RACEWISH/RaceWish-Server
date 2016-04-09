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
    int now;//�p��ثe��ĴX��
    int d;//���W�t�����`��
    int m;//�@�Өt�����X��wish
    int l;//�@�@���h�֭ӳ��a
    int n = d*m;//�`�@�n�}���}�C   
    Team[] deptid = new Team[d];
    Location[] place = new Location[l];
    Node[] wish = new Node[n];
    
    /*���wish�ۦP place�ۦP => ����*/
    public int choose(int start){
        int count = 0;//�p����wish���h�֨t��
        for(int i = start;wish[i].place==wish[i+1].place;i++){
            count++;
        }
        //�qstart�}�l �~���|���D���ƪ�
        int tmp = (int)(Math.random()*count+start);
        //�ˬd�O�_�ӭ�wish�ҳQ��L
        if(check_wish(start,count)==1){//�ҳQ��L
            return;
        }
        //���w�����L�N����
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
    
    /*�T�{��L�P�_*/
    //��return �í���
    //�S�� �T�{�üаO
    public void check(String deptid,int tmp){
        for(int i=0; i<d; i++){
            if(deptid[i].deptid.equals(deptid)){
                if(deptid[i].loca == null){//�S�����L�A�h�\�J
                    deptid[i].loca = wish[tmp].place;
                    //�аO�t�� �� ���a
                    for(int j=0;j<l;j++){//
                        if(place[j].place.equals(deptid[i].loca)){
                            place[j].user = deptid[i].deptid;
                        }
                    }
                    return 0;
                }else{//���L
                    return 1;
                }
            }
        }
    }
    /*�����⧹�᪺�G���ˬd*/
    public void last_check(){
        //�q�Y����ˬd�O�_�Ҧ��t���Ҧ����a
        for(int i=0;i<d;i++){
            if(deptid[i].loca==null){
                //�q�Ѿl�����a����
                int tmp = (int)(Math.random()*l-1);
                //���S���H�ϥΪ���
                while(place[tmp].place != null){
                    int tmp = (int)(Math.random()*l-1); 
                }
                //�аO
                deptid[i].loca = place[tmp].place;
                place[tmp].user = deptid[i].deptid;
            }
        }
    }
    public static void main(){
        //�Ĥ@������@�ǡA�åB���аO
        for(int i=0; now <= n; i = i+next){
            int next = choose(i);
        }
        //�qdeptid����X�٨S�Q��L���A�åB�q�Ѿl�����a����
        last_check();
    }
}