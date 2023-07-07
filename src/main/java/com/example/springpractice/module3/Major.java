package com.example.springpractice.module3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

public class Major {

    private static final Logger log = LoggerFactory.getLogger(Major.class);

    private Bean2 bean2;
    @Autowired
    public void setBean2(Bean2 bean2){
        log.debug("@Autowired生效：{}",bean2);
        this.bean2=bean2;
    }

    private Bean3 bean3;

    @Resource
    public void setBean3(Bean3 bean3){
        log.debug("@Resource生效：{}",bean3);
        this.bean3=bean3;
    }

    private String home;

    @Autowired
    public void setHome(@Value("${JAVA_HOME}") String home){
        log.debug("@Value依赖注入：{}",home);
        this.home=home;
    }

    @PostConstruct
    public void init(){
        log.debug("@PostConstruct 生效");
    }

    @PreDestroy
    public void destory(){
        log.debug("@PreDestory 生效");
    }
    @Override
    public String toString(){
        return "Major{"+"bean2="+bean2+",bean3="+bean3+",home"+home+"}";
    }
}
