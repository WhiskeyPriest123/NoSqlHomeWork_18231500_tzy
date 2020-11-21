package com.bjtu.redis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainTest {
    public static void main(String[] args)  {
        ArrayList<String> FilePath=MyJedis.readfile("src/main/resources");
        for(int i=0;i<FilePath.size();i++){
            System.out.println(FilePath.get(i));
        }
        MyJedis myjedis=new MyJedis(FilePath);//构造实例
        Scanner Input=new Scanner(System.in);
        System.out.println("输入你想查询的键");
        String NO=Input.nextLine();
        myjedis.setCount(NO);
        System.out.println("当前键的信息");
        System.out.println(myjedis.showCount(NO));
        System.out.println("输入你想查的List历史记录的范围");//登录记录
        int NO1=Input.nextInt();
        myjedis.showList(NO1);
        System.out.println("以下是Set");
        myjedis.showSet();
        System.out.println("以下是Zset");
        myjedis.showZset();
    }
}
