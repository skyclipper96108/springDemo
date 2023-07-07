package com.example.springpractice.module3;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

public class GenericApplication {
    public static void main(String[] args) {
        // GenericApplicationContext是一个干净的容器
        GenericApplicationContext context = new GenericApplicationContext();

        //用原始方法注册三个bean
        context.registerBean("major",Major.class);
        context.registerBean("bean1",Bean2.class);
        context.registerBean("bean2",Bean3.class);

        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);//Autowired Value
        context.registerBean(CommonAnnotationBeanPostProcessor.class);//Resource @PostConstruct @PreDestroy
        //初始化容器
        context.refresh();//执行beanFactory后处理器，添加bean后处理器，初始化所有单例


        //销毁容器
        context.close();
    }
}
