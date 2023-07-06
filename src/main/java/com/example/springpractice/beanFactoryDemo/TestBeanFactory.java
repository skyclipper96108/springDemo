package com.example.springpractice.beanFactoryDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class TestBeanFactory {
    /**
     * BeanFactory不会做的事
     * 不会主动调用BeanFactory处理器
     * 不会主动添加Bean后处理器
     * 不会主动初始化单例
     * 不会解析beanFactory
     */
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //根据Bean的定义去创建Bean对象(class,scope,初始化,销毁)
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();
        beanFactory.registerBeanDefinition("config",beanDefinition);

        //Beanfactory本身不具备解析Bean注解的功能 需要通过该工具类添加一些常用后处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        //将后处理器取出并与Beanfactory建立联系————internalConfigurationAnnotationProcessor
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(beanFactoryPostProcessor -> {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });
        //此时未进行依赖注入 需要internalAutowiredAnnotationProcessor(Bean的后处理器) 针对bean的生命周期的各个阶段进行扩展
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanPostProcessor -> {
            System.out.println(">>>>"+beanPostProcessor);
            //其中默认AutowiredAnnotationBeanPostProcessor优先于CommonAnnotationBeanPostProcessor挂载
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        });
        for(String name:beanFactory.getBeanDefinitionNames()){
            System.out.println(name);
        }
        beanFactory.preInstantiateSingletons();//准备好所有的单例(饿汉式)
        System.out.println("*********************************");
        System.out.println(beanFactory.getBean(Bean1.class).getBean2());
    }
    @Configuration
    static class Config{
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }
        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }
    }

    static class Bean1{
        private static final Logger log = LoggerFactory.getLogger(Bean1.class);
        public Bean1(){log.debug("构建Bean1");}
        //bean1的构造方法依赖bean2
        @Autowired
        private Bean2 bean2;
        public Bean2 getBean2(){return bean2;}
    }
    static class Bean2{
        private static final Logger log = LoggerFactory.getLogger(Bean2.class);
        public Bean2(){log.debug("构建Bean2");}
    }
}
