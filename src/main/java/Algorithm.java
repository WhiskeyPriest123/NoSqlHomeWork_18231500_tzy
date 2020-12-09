import javax.swing.tree.TreeNode;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Algorithm {
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

//    public List<String> binaryTreePaths(TreeNode root){//求二叉树的路径
//        List<String> paths=new ArrayList<>();
//        if(root==null){
//            return paths;
//        }
//        List<String> leftPaths=binaryTreePaths(root.left);
//        List<String> rightPaths=binaryTreePaths(root.right);
//        for(String path:leftPaths){
//            paths.add(root.val+"->"+path);
//        }
//        for(String path:rightPaths){
//            paths.add(root.val+"->"+path);
//        }
//        if(paths.size()==0){
//            paths.add(""+root.val);
//        }
//        return paths;
//    }

//    private int minSum;//找中间子树
//    private TreeNode minRoot;
//    public TreeNode findSubtree(TreeNode root){
//        minSum=999999999;
//        minRoot=null;
//        getSum(root);
//        return minRoot;
//    }
//    public int getSum(TreeNode root){
//        if(root==null){
//            return 0;
//        }
//        int sum=getSum(root.left)+getSum(root.right);
//        if(sum<midSum){
//            minSum=sum;
//            minRoot=root;
//        }
//        return sum;
//    }


//    public static void main(String[] args) {
//        String[] input=new String[]{"r","r","b","g","b","r","g"};
//        Arrays.asList(reRank2(input)).forEach(e->System.out.println(e));
//    }
}
