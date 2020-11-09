package com.bjtu.redis;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.Scanner;

public class MainTest {
    public static void main(String[] args) {
        File file1=new File("src/main/resources/0.json");
        String[] FileName=new String[5];
        for(int i=0;i<5;i++){
            FileName[i]="src/main/resources/"+Integer.toString(i)+".json";
        }
        JedisTest MyJedis=new JedisTest(FileName);
        Scanner Input=new Scanner(System.in);
        System.out.println("输入你想查询的键");
        String NO=Input.nextLine();
        MyJedis.setCount(NO);
        System.out.println(MyJedis.showCount(NO));
        System.out.println("输入你想查的历史记录的范围");//登录记录
        int NO1=Input.nextInt();
        MyJedis.showList(NO1);

    }
}
