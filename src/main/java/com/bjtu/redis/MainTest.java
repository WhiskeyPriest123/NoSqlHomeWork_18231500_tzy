package com.bjtu.redis;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.Scanner;

public class MainTest {
    public static void main(String[] args) {
        String[] FileName=new String[5];//暂时先用五个测试
        for(int i=0;i<5;i++){
            FileName[i]="src/main/resources/"+Integer.toString(i)+".json";
        }
        JedisTest MyJedis=new JedisTest(FileName);//构造实例
        Scanner Input=new Scanner(System.in);
        System.out.println("输入你想查询的键");
        String NO=Input.nextLine();
        MyJedis.setCount(NO);
        System.out.println("当前键的信息");
        System.out.println(MyJedis.showCount(NO));
        System.out.println("输入你想查的List历史记录的范围");//登录记录
        int NO1=Input.nextInt();
        MyJedis.showList(NO1);
        System.out.println("以下是Set");
        MyJedis.showSet();
        System.out.println("以下是Zset");
        MyJedis.showZset();


    }
}
