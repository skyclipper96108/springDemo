package com.example.springpractice;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestApplicationContext02 {

    public static void main(String[] args) {
//        testClassPathXmlApplicationContext();
//        testMockClassPathXmlApplicationContext();
//        testAnnotationConfigApplicationContext();
        testAnnotationConfigServletWebServerApplicationContext();
    }

    //beanFactory通过reader读取xml模拟
    private static void testMockClassPathXmlApplicationContext(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //读取之前
        for(String name:beanFactory.getBeanDefinitionNames()){
            System.out.println(name);
        }
        System.out.println("*****************************");
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        //读取类的定义信息
        reader.loadBeanDefinitions(new ClassPathResource("spa01.xml"));
        //读取之前
        for(String name:beanFactory.getBeanDefinitionNames()){
            System.out.println(name);
        }
    }
    // 1.较为经典的容器，基于classpath下xml格式的配置文件来创建
    private static void testClassPathXmlApplicationContext(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spa01.xml");
        for(String name:context.getBeanDefinitionNames()){
            System.out.println(name);
        }
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    // 2.基于磁盘路径下xml 格式的配置文件来创建
    private static void testFileSystemXmlApplicationContext(){
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("src\\main\\resources\\spa01.xml");
    }

    // 3.较为经典的容器，基于Java配置类来创建(自带AnnotationConfigUtils)
    private static void testAnnotationConfigApplicationContext(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        for(String name:context.getBeanDefinitionNames()){
            System.out.println(name);
        }
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    //4.较为经典的容器，基于Java的配置类Servlet的容器，用于WEB环境
    private static void testAnnotationConfigServletWebServerApplicationContext(){
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

    }
    @Configuration
    static class WebConfig{
        @Bean
        public ServletWebServerFactory servletWebServerFactory(){
            //spring内嵌的tomcat服务器
            return new TomcatServletWebServerFactory();
        }
        @Bean
        public DispatcherServlet dispatcherServlet(){
            //分发请求
            return new DispatcherServlet();
        }
        @Bean
        public DispatcherServletRegistrationBean registerBean(DispatcherServlet dispatcherServlet){
            //dispatcherServlet给路径后注入到tomcat容器中
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }
        @Bean("/hello")
        public Controller controller1(){
            return new Controller() {
                @Override
                public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
                    response.getWriter().println("hello");
                    return null;
                }
            };
        }
    }

    @Configuration
    static class Config{
        //普通配置类
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }
        @Bean
        public Bean2 bean2(Bean1 bean1){
            Bean2 bean2 = new Bean2();
            bean2.setBean1(bean1);
            return bean2;
        }
    }


    static class Bean1 {

    }

    static class Bean2{

        private Bean1 bean1;

        public void setBean1(Bean1 bean1){
            this.bean1=bean1;
        }
        public Bean1 getBean1(){
            return bean1;
        }
    }
}
