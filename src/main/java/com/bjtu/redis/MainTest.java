package com.bjtu.redis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.io.monitor.FileEntry;
/*
*
* 作者：童振宇
* 学号：18231500
*
*

*
* */

public class MainTest {
    public static void print123(){
        System.out.println();
        System.out.println();
        System.out.println("作者：童振宇,学号：18231500");
        System.out.println("选择你的操作");
        System.out.println("1.点击一次");
        System.out.println("2.点击多次");
        System.out.println("3.查看指定键");
        System.out.println("4.查看所有List");
        System.out.println("5.查看所有Set");
        System.out.println("6.查看所有ZSet(把小时作为权重排序)");
        System.out.println("7.指定时间区间");
        System.out.println("8.指定用户的登录记录");
        System.out.println("0.退出");
    }
    public static void main(String[] args) throws Exception {
        FileMonitor m = new FileMonitor(500);//设置监控的间隔时间，初始化监听
        m.monitor("src/main/resources", new FileListener()); //指定文件夹，添加监听
        m.start();//开启监听
        ArrayList<String> FilePath=MyJedis.ReadFileName("src/main/resources");//打开文件
        System.out.println("共有"+Integer.valueOf(FilePath.size())+"个Json文件");
        MyJedis myjedis=new MyJedis(FilePath);//构造实例
        Scanner Input=new Scanner(System.in);
        print123();
        String type=Input.nextLine();
        while (!type.equals("0")){
            if(type.equals("1")) {
                System.out.println("输入你想点击的键(只点一次)");
                String NO=Input.nextLine();
                myjedis.Click(NO);
            }
            else if(type.equals("2")) {
                System.out.println("输入你想点击的键");
                String NO2=Input.nextLine();
                System.out.println("输入你想点击的次数");
                int ClickNumber=Integer.valueOf(Input.nextLine());
                myjedis.Click(NO2,ClickNumber);
            }
            else if(type.equals("3")) {
                System.out.println("输入你想查询的键");
                String NO3=Input.nextLine();
                System.out.println("当前键的信息");
                System.out.println(myjedis.getCount(NO3));
            }
            else if(type.equals("4")){
                System.out.println("以下是所有List记录");//登录记录
                List<String> list=myjedis.showList();
                if (list.size()==0){
                    System.out.println("Not Found");
                }
                else{
                    for(int i=0; i<list.size(); i++) {
                        System.out.println("列表项为: "+list.get(i));
                    }
                }
            }
            else if(type.equals("5")){
                System.out.println("以下是Set");
                Set<String> set=myjedis.showSet();
                System.out.println(set);
            }
            else if(type.equals("6")){
                System.out.println("以下是Zset(以小时为顺序排序)");
                Set<String> zset=myjedis.showZset();
                System.out.println(zset);
            }
            else if(type.equals("7")){
                System.out.println("输入时间区间");
                System.out.println("输入Begin");
                int Begin=Integer.valueOf(Input.nextLine());
                System.out.println("输入End");
                int End=Integer.valueOf(Input.nextLine());
                List<String> list=myjedis.showGiventime(Begin,End);
                if (list.size()==0){
                    System.out.println("Not Found");
                }
                else{
                    for(int i=0; i<list.size(); i++) {
                        System.out.println("列表项为: "+list.get(i));
                    }
                }

            }
            else if(type.equals("8")){
                System.out.println("输入键");
                String key=Input.nextLine();
                System.out.println("以下是该用户所有记录");//登录记录
                List<String> list=myjedis.showUserList(key+"list");
                if (list.size()==0){
                    System.out.println("Not Found");
                }
                else {
                    for (int i = 0; i < list.size(); i++) {
                        System.out.println("列表项为: " + list.get(i));
                    }
                }
            }
            print123();
            type=Input.nextLine();
        }
        System.out.println("GoodBye");
    }
}
