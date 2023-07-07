package com.example.springpractice.module2;


import java.util.ArrayList;
import java.util.List;

/**
 * 模板方法设计模式
 */
public class TestMethodTemplate {
    public static void main(String[] args) {
        MyBeanFactory beanFactory = new MyBeanFactory();
        //依赖注入的方法不需要修改Bean生产的逻辑
        beanFactory.addBeanPostProcessor(bean -> System.out.println("解析@Autowired"));
        beanFactory.addBeanPostProcessor(bean -> System.out.println("解析@Resource"));
        beanFactory.getBean();
    }

    static class MyBeanFactory{
        public Object getBean(){
            Object bean = new Object();
            System.out.println("构造"+bean);
            System.out.println("依赖注入"+bean);
            for(BeanPostProcessor processor:processors){
                processor.inject(bean);
            }
            System.out.println("初始化"+bean);
            return bean;
        }


        private List<BeanPostProcessor> processors = new ArrayList<>();

        public void addBeanPostProcessor(BeanPostProcessor processor){
            processors.add(processor);
        }
        static interface BeanPostProcessor{
            public void inject(Object bean);//对于依赖注入的扩展
        }
    }
}
