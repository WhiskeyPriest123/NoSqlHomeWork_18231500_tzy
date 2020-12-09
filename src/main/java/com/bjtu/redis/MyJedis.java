package com.bjtu.redis;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MyJedis {
    private Jedis jedis;
    private ArrayList<String> JsonContent=new ArrayList<>();
    private ArrayList<User> Users=new ArrayList<>();
    public static String ReadJson(String fileName){//读取Json文件
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
    private boolean WriteJson(String filePath, String Content) {//往json里写文件
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
    //往json里写文件
    public static ArrayList<String> ReadFileName(String filepath) {//根据文件夹名读取文件名
        ArrayList<String> output=new ArrayList<String>();
        File file = new File(filepath);
        String[] filelist = file.list();
        for (int i = 0; i < filelist.length; i++) {
            File readfile = new File(filepath + "\\" + filelist[i]);
            if (!readfile.isDirectory()) {
                String FileName=readfile.getName();
                if(Pattern.matches("Counter[0-9]+\\.json$",FileName)){//正则表达式读取指定文件格式
                    output.add(readfile.getPath());
                }
            }
        }
        return output;
    }

    public MyJedis(ArrayList<String> JsonFileName) {//构造器
        jedis = JedisInstance.getInstance().getResource();//获得资源线程池
        //JsonContent=new String[JsonFileName.size()];//Json内容
        //Users=new User[JsonFileName.size()];//用户
        for (int i = 0; i < JsonFileName.size(); i++) {
            JsonContent.add(ReadJson(JsonFileName.get(i)));
            Users.add(new User(JsonContent.get(i)));
            Users.get(i).setFileName(JsonFileName.get(i));
            System.out.println(Users.get(i));
            //jedis.set(Users[i].getNo()+"Name",Users[i].getName());
        }
    }

    public void Click(String key){//查询时加一
        int IntNo=Integer.parseInt(key);
        if(jedis.get(key)==null){
            jedis.set(key,"1");
        }
        else {
            jedis.incr(key);
            Users.get(IntNo).setAction();
            Users.get(IntNo).setCount(jedis.get(key));
            String jsonOutput= JSON.toJSONString(Users.get(IntNo));//json序列化
            WriteJson("src/main/resources/Counter"+key+".json",jsonOutput);
        }
        //依次增加查询次数
        jedis.lpush(key+"list",Users.get(IntNo).getAction());//该用户的登录时间
        jedis.sadd("MySet",Users.get(IntNo).getAction());
        jedis.zadd("MyZset",Integer.parseInt(Users.get(IntNo).getAction().substring(0,2)),Users.get(IntNo).getAction());//按照小时的顺序来排序
        jedis.lpush("MyList", Users.get(IntNo).getAction());
    }


    public void Click(String key,int CountNumber){//多次查询
        int IntNo=Integer.parseInt(key);
        if(jedis.get(key)==null){
            jedis.set(key,Integer.toString(CountNumber));
        }
        else {
            for(int i=0;i<CountNumber;i++){//增加好几次
                jedis.incr(key);
            }
            Users.get(IntNo).setAction();
            Users.get(IntNo).setCount(jedis.get(key));
            String jsonOutput= JSON.toJSONString( Users.get(IntNo));//json序列化
            WriteJson("src/main/resources/"+key+".json",jsonOutput);
        }
        //增加用户的访问时间
        jedis.lpush(key+"list",Users.get(IntNo).getAction());//该用户的登录时间
        //依次增加查询次数

        jedis.sadd("MySet", Users.get(IntNo).getAction());
        jedis.zadd("MyZset",Integer.parseInt(Users.get(IntNo).getAction().substring(0,2)), Users.get(IntNo).getAction());//按照小时作为权重,顺序来排序
        jedis.lpush("MyList",  Users.get(IntNo).getAction());
    }

    public String getCount(String key){//获得查询次数
        if(jedis.get(key)==null){
            return "ERROR";
        }
        else {
            return jedis.get(key);
        }
    }

    public List<String>showUserList(String key){
        List<String> list = jedis.lrange(key,0,-1);
        WriteJson("src/main/resources/User"+key+"List.json",JSON.toJSONString(list));//把set写入json里
        /*for(int i=0; i<list.size(); i++) {
            System.out.println("列表项为:"+list.get(i));
        }*/
        return list;
    }

    public List<String> showList(){//展示列表里的内容
        List<String> list = jedis.lrange("MyList",0,-1);
        WriteJson("src/main/resources/List.json",JSON.toJSONString(list));//把set写入json里
        return list;
    }

    public List<String> showGiventime(int begin,int end){
        if (end<begin){//保证end比begin大
            int temp=begin;
            begin=end;
            end=temp;
        }
        List<String> list = jedis.lrange("MyList",0,-1);
        List<String> result = new ArrayList<>() ;
        for(int i=0; i<list.size(); i++) {
            String temp=list.get(i);
            if(Integer.parseInt(temp.substring(0,2))<end&&Integer.parseInt(temp.substring(0,2))>=begin) {
                result.add(temp);
            }
        }
        WriteJson("src/main/resources/"+begin+"To"+end+".json",JSON.toJSONString(result));
        return result;
    }

    public Set<String> showSet(){//展示set
        Set<String> set = jedis.smembers("MySet");
        WriteJson("src/main/resources/Set.json",JSON.toJSONString(set));//把set写入json里
        return set;
    }

    public Set<String> showZset(){//把小时设为权重标准进行排序
        Set<String> set = jedis.zrangeByScore("MyZset",0,24);
        WriteJson("src/main/resources/Zset.json",JSON.toJSONString(set));//把set写入json里
        return set;
    }

}
