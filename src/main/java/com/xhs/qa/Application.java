package com.xhs.qa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created on 18/3/12 14:38
 *
 * @author sunyumei
 */

@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application extends SpringApplication {
    public static void main(String[] args){
        Application.run(Application.class,args);
    }
}