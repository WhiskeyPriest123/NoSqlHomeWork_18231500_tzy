import java.lang.reflect.Array;
import java.util.Arrays;

public class HomeWork {
    public static String[] reRank(String[] input){
        if(input==null||input.length==0) {//顺序不能换
            return new String[0];
        }
        int CountR=0;
        int CountB=0;
        int CountG=0;

        for(int i=0;i<input.length;i++) {
            if(input[i]=="r"){
                CountR++;
            }
            else if(input[i]=="b"){
                CountB++;
            }
            else if(input[i]=="g"){
                CountG++;
            }
        }
        String[] output=new String[input.length];
        for(int i=0;i<CountR;i++) {
            output[i]="R";
        }
        for(int i=CountR;i<CountR+CountB;i++) {
            output[i]="B";
        }
        for(int i=CountR+CountB;i<CountR+CountB+CountG;i++) {
            output[i]="G";
        }
        return output;
    }

    public static String[] reRank2(String[] input){
        if(input==null||input.length==0) {//顺序不能换
            return new String[0];
        }
        int i=0;
        int left=0;
        int right=input.length-1;
        while (i<=right){
            if(input[i].equals("r")){
                String t1=input[i];
                input[i]=input[left];
                input[left]=t1;
                i++;
                left++;
            }
            else if(input[i].equals("g")){
                i++;
            }
            else if(input[i].equals("b")) {
                String t2=input[i];
                input[i]=input[right];
                input[right]=t2;
                right--;
            }
        }
        return input;
    }


    public static void main(String[] args) {
        String[] input=new String[]{"r","r","b","g","b","r","g"};
        Arrays.asList(reRank2(input)).forEach(e->System.out.println(e));
    }
}
