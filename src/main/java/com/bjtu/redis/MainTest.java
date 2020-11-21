package com.bjtu.redis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.io.monitor.FileEntry;

public class MainTest {
    public static void main(String[] args) throws Exception {

        //初始化监听
        FileMonitor m = new FileMonitor(1000);//设置监控的间隔时间
        //指定文件夹，添加监听
        m.monitor("src/main/resources", new FileListener());
        //开启监听
        m.start();
        ArrayList<String> FilePath=MyJedis.readfile("src/main/resources");
        System.out.println("共有"+Integer.valueOf(FilePath.size())+"个Json文件");
        MyJedis myjedis=new MyJedis(FilePath);//构造实例
        while (true){
            Scanner Input=new Scanner(System.in);
            //System.out.println("我想多点几次");
            //String NO2=Input.nextLine();
            //int ClickNumber=Input.nextInt();
            //myjedis.setCount(NO2,ClickNumber);
            System.out.println("输入你想点击的键");
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
           // myjedis.showGiventime(1,20);
        }
    }
}
