package com.bjtu.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {

    @JSONField(name = "Count")
    private int Count;
    @JSONField(name = "Action")//最近一次登录
    private String Action;
    @JSONField(name = "Name")
    private String Name;

    private String FileName="";

    //两种构造方法
    //一种通过元素构造
    //一种通过json文件构造
    public void setCount(){
        this.Count++;
    }
    public void setName(String Name){
        this.Name=Name;
    }
    public void setFileName(String FileName){
        this.FileName=FileName;
    }
    public void setAction(){//获得当前时间
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();// 获取当前时间
        this.Action=sdf.format(date);
    }


    public int getCount(){
        return this.Count;
    }
    public String getName(){
        return this.Name;
    }
    public String getAction(){
        return this.Action;
    }
    public String getFileName(){
        return this.FileName;
    }
    public User(String JsonObject){
        super();
        this.Count=JSON.parseObject(JsonObject, User.class).Count;
        this.Name=JSON.parseObject(JsonObject, User.class).Name;
        this.Action = JSON.parseObject(JsonObject, User.class).Action;
    }
    public User(int Count, String Name, String Action) {
        super();
        this.Count=Count;
        this.Name=Name;
        this.Action=Action;
    }

    @Override
    public String toString() {
        return "User{" +
                "Count=" + Count +
                ", Name='" + Name + '\'' +
                ", Action='" + Action + '\'' +
                ", FileName="  +FileName+
                '}';
    }
}