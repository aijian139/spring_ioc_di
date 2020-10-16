package com.yj.spring;

import com.yj.service.UserService;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 模拟 ioc 和 di 的底层代码实现
public class ApplicationContextUtil {
    // 原理
    // 1.模拟spring容器 新建一个map 容器
    // 2.加载解析 xml 文件 通过反射的方式的获取对象 存入到容器中

    //创建map 容器
    private HashMap<String,Object> map = new HashMap<>();

    // 通过构造函数来初始化
    public ApplicationContextUtil() throws Exception {
        //初始化所有的bean(对象)放在容器中
        //操作xml文件     jdom
        SAXBuilder sx = new SAXBuilder();
        //读取配置beans.xml配置文件，转换成输入流InputStream
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
        //获取文档对象模型
        Document document = sx.build(in);
        //获取xml文件的根节点
        Element element = document.getRootElement();
        //获取根节点下面的所有的子节点
        List<Element> list = element.getChildren();
        // for  foreach 迭代器
        for(Element ment : list){
            String id = ment.getAttributeValue("id");
            String clazz = ment.getAttributeValue("class");
            //System.out.println("id = " + id);
            //System.out.println("class = " + clazz);
            //利用反射获取 类的对象
            Object obj = Class.forName(clazz).newInstance();
            //放到容器中
            map.put(id, obj);
            //在服务器启动的时候，将所有需要使用到的对象实例化放到容器中，在需要用到的地方从容器中来获取
            //IOC:Inversion   Of  Control  控制反转
            //获取bean节点下面的子节点property   service才有property
            List<Element> mentList = ment.getChildren();

            for(Element et : mentList){
                String name = et.getAttributeValue("name");
                String ref = et.getAttributeValue("ref");
                //通过set方法来注入dao
                //1.获取set方法名
                String methodName = "set" + name.substring(0,1).toUpperCase() + name.substring(1);
                //2.取出set方法所需要的对象  setUao(实参)
                Object beanObject = map.get(ref);
                //3.获取这个方法   setUao(UserDAO)
                Method method = obj.getClass().getMethod(methodName,  beanObject.getClass());
                //4.调用方法   执行方法
                method.invoke(obj, beanObject);
                //DI :Dependency   Injection 依赖注入
                //service正常运行的时候，需要将所使用dao注入
            }
        }

    }

    public static void main(String[] args) throws Exception {
        ApplicationContextUtil applicationContextUtil = new ApplicationContextUtil();
        UserService userService = (UserService) applicationContextUtil.map.get("userService");
        userService.addUser();
    }
}
