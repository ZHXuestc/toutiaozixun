package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Maze1 {
    public static void main(String[] args) {
        //String command = "3,3\n0,1 0,2;0,0 1,0;0,1 1,1;0,2 1,2;1,0 1,1;1,1 1,2;1,1 2,1;1,2 2,2;2,0 2,1";
        String command="5,5\n0,1 1,1;1,1 2,1;2,1 2,2;2,2 2,3;2,3 2,4;2,4 3,4;3,4 4,4;0,4 0,3;0,3 0,2;0,2 1,2;1,2 2,2;2,2 3,2;3,2 3,1;3,1 3,0";
        System.out.println(command);
        String[] commends=checkInput(command);
        String[] sizes=commends[0].split(",");
        int rowNumber=0;
        int colNumber=0;
        rowNumber=Integer.parseInt(sizes[0].trim());
        colNumber=Integer.parseInt(sizes[1].trim());
        rowNumber=rowNumber*2+1;
        colNumber=colNumber*2+1;
        List<int[][]> connList1=parseConnPoint(commends[1]);
        create(rowNumber,colNumber,connList1);
    }
    public static List<int[][]> parseConnPoint(String str){
        List<int[][]>connList1=new ArrayList<int[][]>();
        String[] strs=str.split(";");
        for(String cp:strs){
            //0,1 0,2
            String[] cps=cp.split(" ");
            //0,1
            int[][] connPoint=new int[2][2];
            for(int i=0;i<cps.length;i++){
                String[] point=cps[i].split(",");
                connPoint[i][0]=Integer.parseInt(point[0].trim());
                connPoint[i][1]=Integer.parseInt(point[1].trim());
            }
            checkPoint(connPoint);
            connList1.add(connPoint);
        }
        return connList1;
    }

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

    public static String[] checkInput(String command){
        String[] commands=command.split("\n");
        String pattern1="\\d,\\d";
        Pattern r = Pattern.compile(pattern1);
        Matcher m = r.matcher(commands[0]);
        if(!m.matches()){
            throw new RuntimeException("Incorrect command format");
        }

        return commands;
    }
    public static boolean checkPoint(int[][] points){
        int x1=points[0][0];
        int y1=points[0][1];
        int x2=points[1][0];
        int y2=points[1][1];
        if(x1==x2 && Math.abs(y1-y2)!=1){
            throw new RuntimeException("Maze format Error");
        }
        if(y1==y2 && Math.abs(x1-x2)!=1){
            throw new RuntimeException("Maze format Error");
        }
        return false;
    }
}
