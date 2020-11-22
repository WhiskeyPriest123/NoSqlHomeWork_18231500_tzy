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
        FileMonitor m = new FileMonitor(500);//设置监控的间隔时间，初始化监听
        m.monitor("src/main/resources", new FileListener()); //指定文件夹，添加监听
        m.start();//开启监听
        ArrayList<String> FilePath=MyJedis.ReadFileName("src/main/resources");//打开文件
        System.out.println("共有"+Integer.valueOf(FilePath.size())+"个Json文件");
        MyJedis myjedis=new MyJedis(FilePath);//构造实例
        Scanner Input=new Scanner(System.in);
        while (true){
            System.out.println("输入你想点击的键");
            String NO2=Input.nextLine();
            System.out.println("输入你想点击的次数");
            int ClickNumber=Integer.valueOf(Input.nextLine());
            myjedis.setCount(NO2,ClickNumber);
            System.out.println("输入你想点击的键");
            String NO=Input.nextLine();
            myjedis.setCount(NO);
            System.out.println("点击你想查询的键");
            System.out.println("当前键的信息");
            System.out.println(myjedis.showCount(NO));
            System.out.println("输入你想查的List历史记录的范围");//登录记录
            int NO1=Integer.valueOf(Input.nextLine());
            myjedis.showList(NO1);
            System.out.println("以下是Set");
            myjedis.showSet();
            System.out.println("以下是Zset(以小时为顺序排序)");
            myjedis.showZset();
            System.out.println("输入时间区间");
            System.out.println("输入Begin");
            int Begin=Integer.valueOf(Input.nextLine());
            System.out.println("输入End");
            int End=Integer.valueOf(Input.nextLine());
            System.out.println(Begin+End);
            myjedis.showGiventime(Begin,End);
        }
    }
}
