package com.bjtu.redis;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.File;

public class MainTest {
    public static void main(String[] args) {
        File file1=new File("src/main/resources/0.json");
        String[] FileName=new String[5];
        for(int i=0;i<5;i++){
            FileName[i]="src/main/resources/"+Integer.toString(i)+".json";
        }
        JedisTest MyJedis=new JedisTest(FileName);
    }
}
