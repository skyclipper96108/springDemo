package com.example.springpractice.module3;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DigAutowired {
    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("bean2",new Bean2()); //创建过程，依赖注入，初始化
        beanFactory.registerSingleton("bean3",new Bean3());
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());//@Value
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders); //${}解析器

        //1.查找哪些属性、方法加了@Autowired，这称之为InjectionData
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);

        Major major = new Major();
//        System.out.println(major);
//        processor.postProcessProperties(null,major,"major");
//        System.out.println(major);
        // 执行依赖注入(postProcessProperties)注入细节：
        Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);
        InjectionMetadata metadata = (InjectionMetadata)findAutowiringMetadata.invoke(processor, "major", Major.class, null);
        System.out.println(metadata.toString());


        //2.调用InjectionMetadata来进行依赖注入，注入时按照类型查找
        metadata.inject(major,"major",null);
        System.out.println(major);

        //3.如何按类型查找值
        Field bean3 = Major.class.getDeclaredField("bean3");
        //Spring查找到作用域会将其封装
        DependencyDescriptor dd1 = new DependencyDescriptor(bean3, false);
        Object o = beanFactory.doResolveDependency(dd1, null, null, null);
        //拿到实例化的对象
        System.out.println(o);

        Method setBean2 = Major.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor dd2 = new DependencyDescriptor(new MethodParameter(setBean2, 0), false);
        //根据方法参数类型找到对象
        Object o1 = beanFactory.doResolveDependency(dd2, null, null, null);
        System.out.println(o1);
    }
}
