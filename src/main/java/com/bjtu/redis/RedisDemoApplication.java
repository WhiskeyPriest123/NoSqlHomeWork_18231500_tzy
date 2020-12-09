package com.bjtu.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static com.bjtu.redis.MainTest.print123;

/**
 *  SpringBootApplication
 * 用于代替 @SpringBootConfiguration（@Configuration）、 @EnableAutoConfiguration 、 @ComponentScan。
 * <p>
 * SpringBootConfiguration（Configuration） 注明为IoC容器的配置类，基于java config
 * EnableAutoConfiguration 借助@Import的帮助，将所有符合自动配置条件的bean定义加载到IoC容器
 * ComponentScan 自动扫描并加载符合条件的组件
 */
@SpringBootApplication
public class RedisDemoApplication {
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
                if(Integer.valueOf(NO)>=FilePath.size()){
                    System.out.println("ERROR");
                }
                else {
                    myjedis.Click(NO);
                }
                //myjedis.Click(NO);
            }
            else if(type.equals("2")) {
                System.out.println("输入你想点击的键");
                String NO2=Input.nextLine();
                System.out.println("输入你想点击的次数");
                int ClickNumber=Integer.valueOf(Input.nextLine());
                if(Integer.valueOf(NO2)>=FilePath.size()){
                    System.out.println("ERROR");
                }
                else {
                    myjedis.Click(NO2,ClickNumber);
                }
                //myjedis.Click(NO2,ClickNumber);
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
                    System.out.println("该时间段共有"+list.size()+"个元素");
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
                        System.out.println("列表项为:" + list.get(i));
                    }
                }
            }
            print123();
            type=Input.nextLine();
        }
        System.out.println("GoodBye");
    }
}

/*

总结：

1、获取运行环境信息和回调接口。例如ApplicationContextIntializer、ApplicationListener。
完成后，通知所有SpringApplicationRunListener执行started()。

2、创建并准备Environment。
完成后，通知所有SpringApplicationRunListener执行environmentPrepared()

3、创建并初始化 ApplicationContext 。例如，设置 Environment、加载配置等
完成后，通知所有SpringApplicationRunListener执行contextPrepared()、contextLoaded()

4、执行 ApplicationContext 的 refresh，完成程序启动
完成后，遍历执行 CommanadLineRunner、通知SpringApplicationRunListener 执行 finished()

参考：
https://blog.csdn.net/zxzzxzzxz123/article/details/69941910
https://www.cnblogs.com/shamo89/p/8184960.html
https://www.cnblogs.com/trgl/p/7353782.html

分析：

1） 创建一个SpringApplication对象实例，然后调用这个创建好的SpringApplication的实例方法

public static ConfigurableApplicationContext run(Object source, String... args)

public static ConfigurableApplicationContext run(Object[] sources, String[] args)

2） SpringApplication实例初始化完成并且完成设置后，就开始执行run方法的逻辑了，
方法执行伊始，首先遍历执行所有通过SpringFactoriesLoader可以查找到并加载的
SpringApplicationRunListener，调用它们的started()方法。


public SpringApplication(Object... sources)

private final Set<Object> sources = new LinkedHashSet<Object>();

private Banner.Mode bannerMode = Banner.Mode.CONSOLE;

...

private void initialize(Object[] sources)

3） 创建并配置当前SpringBoot应用将要使用的Environment（包括配置要使用的PropertySource以及Profile）。

private boolean deduceWebEnvironment()

4） 遍历调用所有SpringApplicationRunListener的environmentPrepared()的方法，通知Environment准备完毕。

5） 如果SpringApplication的showBanner属性被设置为true，则打印banner。

6） 根据用户是否明确设置了applicationContextClass类型以及初始化阶段的推断结果，
决定该为当前SpringBoot应用创建什么类型的ApplicationContext并创建完成，
然后根据条件决定是否添加ShutdownHook，决定是否使用自定义的BeanNameGenerator，
决定是否使用自定义的ResourceLoader，当然，最重要的，
将之前准备好的Environment设置给创建好的ApplicationContext使用。

7） ApplicationContext创建好之后，SpringApplication会再次借助Spring-FactoriesLoader，
查找并加载classpath中所有可用的ApplicationContext-Initializer，
然后遍历调用这些ApplicationContextInitializer的initialize（applicationContext）方法
来对已经创建好的ApplicationContext进行进一步的处理。

8） 遍历调用所有SpringApplicationRunListener的contextPrepared()方法。

9） 最核心的一步，将之前通过@EnableAutoConfiguration获取的所有配置以及其他形式的
IoC容器配置加载到已经准备完毕的ApplicationContext。

10） 遍历调用所有SpringApplicationRunListener的contextLoaded()方法。

11） 调用ApplicationContext的refresh()方法，完成IoC容器可用的最后一道工序。

12） 查找当前ApplicationContext中是否注册有CommandLineRunner，如果有，则遍历执行它们。

13） 正常情况下，遍历执行SpringApplicationRunListener的finished()方法、
（如果整个过程出现异常，则依然调用所有SpringApplicationRunListener的finished()方法，
只不过这种情况下会将异常信息一并传入处理）


private <T> Collection<? extends T> getSpringFactoriesInstances(Class<T> type)

private <T> Collection<? extends T> getSpringFactoriesInstances(Class<T> type,
			Class<?>[] parameterTypes, Object... args)

public void setInitializers

private Class<?> deduceMainApplicationClass()

public ConfigurableApplicationContext run(String... args)

private void configureHeadlessProperty()

private SpringApplicationRunListeners getRunListeners(String[] args)

public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader)


*/
