课上讲的算法以及Luovain算法代码放在了src\main\java路径下
读后感放在了resources路径下(在课上已经交过纸质版，这是电子版)
redis代码的实现思路如下：我并没有实现读取Action.xml和Counter.xml的方法而是把Counter放入代表用户的.xml文件(形如Counter数字.xml)里代表用户的登录次数
,之后在User类中根据此json文件构造实体类之后，在MyJedis.java中实现对jedis方法的封装，使其能够根据读取到的xml文件构造用户实体类，在FileListener和FileMonitor里实现了当代表用户的文件(形如Counter数字.xml如Counter0.xml)被改动时
程序可以在不停止运行的情况下实时的感知到,当用户每次使用封装好的方法对key/value进行incr操作时，方法会将时间记录到List,Set和Zset里(Zset中小时作为权重排序)
在MainTest和RedisDemoApplication都有main函数调用


                                                        ————童振宇 18231500
