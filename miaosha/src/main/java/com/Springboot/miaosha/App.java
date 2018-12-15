package com.Springboot.miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.Springboot.miaosha.*")//扫描：该包下相应的class,主要是MyBatis的持久化类. @ImportSource加载xml文件 extends SpringBootServletInitializer
public class App
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
    
   /* @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
    	return builder.sources(App.class);
    }*/
}
