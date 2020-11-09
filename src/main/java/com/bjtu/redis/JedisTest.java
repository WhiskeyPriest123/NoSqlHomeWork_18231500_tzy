package com.bjtu.redis;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.jws.soap.SOAPBinding;
import java.io.*;
import java.util.List;
import java.util.Set;

public class JedisTest {
    private Jedis jedis;
    private String[] JsonContent;
    private User[] Users;
    private String ReadJson(String fileName){//读取Json文件
        String jsonString = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            fileReader.close();
            jsonString = sb.toString();
            return jsonString;
        } catch (IOException e) {
            System.out.println("读取Json文件出错");
            e.printStackTrace();
            return null;
        }
    }
    // 把json格式的字符串写到文件
    private boolean WriteJson(String filePath, String Content) {
        FileWriter fw;
        try {
            fw = new FileWriter(filePath);
            PrintWriter out = new PrintWriter(fw);
            out.write(Content);
            out.println();
            fw.close();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public JedisTest(String[] JsonFileName) {//构造器
        jedis = JedisInstance.getInstance().getResource();//获得资源线程池
        JsonContent=new String[JsonFileName.length];//Json内容
        Users=new User[JsonFileName.length];//用户
        for (int i = 0; i < JsonFileName.length; i++) {
            JsonContent[i]=ReadJson(JsonFileName[i]);
            Users[i]=new User(JsonContent[i]);
            Users[i].setFileName(JsonFileName[i]);
            System.out.println(Users[i]);
            //jedis.set(Users[i].getNo(),"1");
            jedis.set(Users[i].getNo()+"Name",Users[i].getName());
            String jsonOutput= JSON.toJSONString(Users[i]);//json序列化
            WriteJson("src/main/resources/test.json",jsonOutput);
        }
    }

    public void setCount(String key){
        int IntNo=Integer.parseInt(key);
        if(jedis.get(key)==null){
            jedis.set(key,"1");
        }
        else {
            Users[IntNo].setAction();
            String jsonOutput= JSON.toJSONString(Users[IntNo]);//json序列化
            WriteJson("src/main/resources/"+key+".json",jsonOutput);
            jedis.incr(key);
        }
        jedis.sadd("MySet",Users[IntNo].getAction());
        jedis.zadd("MyZset",Integer.parseInt(Users[IntNo].getAction().substring(0,2)),Users[IntNo].getAction());//按照小时的顺序来排序
        jedis.lpush("MyList", Users[IntNo].getAction());
    }

    public String showCount(String key){
        if(jedis.get(key)==null){
            return "ERROR";
        }
        else {
            return jedis.get(key);
        }
    }
    public List<String> showList(int Number){
        List<String> list = jedis.lrange("MyList",0,Number);
        for(int i=0; i<list.size(); i++) {
            System.out.println("列表项为: "+list.get(i));
        }
        return list;
    }

    public Set<String> showSet(){
        Set<String> set = jedis.smembers("MySet");
        System.out.println(set);
        return set;
    }

    public Set<String> showZset(){
        Set<String> set = jedis.zrangeByScore("MyZset",0,24);
        System.out.println(set);
        return set;
    }


}
