package maze;

/**
 * Created by Administrator on 2018/7/29.
 */

import java.util.List;
import java.util.Scanner;

public class Maze {
    public static void main(String[] args) {

        Scanner sc=new Scanner(System.in);
        int rowNumner=sc.nextInt();
        int[][] numArrs=new int[rowNumner][];
        for(int i=0;i<rowNumner;i++){
            int l=sc.nextInt();
            numArrs[i]=new int[l];
            for(int j=0;j<l;j++){
                numArrs[i][j]=sc.nextInt();
            }
        }

    }
        //int rowNumner=7;
       // int colNumber=7;

//        int[][] connPoint={{0,1},{0,2}};
//        int[][] connPoint1={{0,0},{1,0}};
//        int[][] connPoint2={{0,1},{1,1}};
//        int[][] connPoint3={{0,2},{1,2}};
//        int[][] connPoint4={{1,0},{1,1}};
//        int[][] connPoint5={{1,1},{1,2}};
//        int[][] connPoint6={{1,1},{2,1}};
//        int[][] connPoint7={{1,2},{2,2}};
//        int[][] connPoint8={{2,0},{2,1}};


       // List<int[][]>connList1=new ArrayList<int[][]>();
//        connList1.add(connPoint);
//        connList1.add(connPoint1);
//        connList1.add(connPoint2);
//        connList1.add(connPoint3);
//        connList1.add(connPoint4);
//        connList1.add(connPoint5);
//        connList1.add(connPoint6);
//        connList1.add(connPoint7);
//        connList1.add(connPoint8);
       // create(rowNumner,colNumber,connList1);


    public static void create(int rowNum,int colNum,List<int[][]> connList){
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<colNum;j++){
                if((i%2!=0 && j%2!=0) || judgeIsRoad(connList,i,j)){
                    System.out.print("R ");
                }else{
                    System.out.print("W ");
                }
            }
            System.out.println();
        }
    }
    public static boolean judgeIsRoad(List<int[][]> connList,int row,int col){
        if(connList==null || connList.isEmpty()){
            System.out.println("Maze format error.");
            return false;
        }
        for(int[][] arry:connList){
            int x1=arry[0][0];
            int y1=arry[0][1];
            int x2=arry[1][0];
            int y2=arry[1][1];
            if(x1==x2){
                if(row==x1*2+1 && (y1+y2+1)==col){
                    return true;
                }
            }else if(y1==y2){
                if(col==y1*2+1 && (x1+x2+1)==row){
                    return true;
                }
            }
        }
        return false;
    }

}
