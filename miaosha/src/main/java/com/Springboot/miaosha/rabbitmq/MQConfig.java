package com.Springboot.miaosha.rabbitmq;



import groovy.lang.Binding;
import groovy.transform.builder.Builder;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {
	
	
	public final static String MIAOSHAQUEUE="miaoshaqueue";
	public final static String QUEUE="queue";
	public final static String TOPIC_QUEUE1="topicqueue1";
	public final static String TOPIC_QUEUE2="topicqueue2";
	public final static String TOPIC_EXCHANGE="topicExchange";
	public final static String FANOUT_EXCHANGE="fanoutExchange";
	public final static String ROUTING_KEY1="topic.key1";
	public final static String ROUTING_KEY2="topic.#";//
	
	@Bean
    public Queue miaoshaqueue(){
    	//queue队列名称 默认交换机
    	return new Queue(MIAOSHAQUEUE,true);
    }
	
	
    @Bean
    public Queue queue(){
    	//queue队列名称 默认交换机
    	return new Queue(QUEUE,true);
    }
    
    /*
     * topic模式 交换机 EXchange  先把数据放到交换机  然后通过bading交给queue
     * */
    @Bean
    public Queue topicqueue1(){
    	return new Queue(TOPIC_QUEUE1,true);
    }
    @Bean
    public Queue topicqueue2(){
    	return new Queue(TOPIC_QUEUE2,true);
    }
    @Bean
    public TopicExchange topicExchange(){
    	return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public org.springframework.amqp.core.Binding topicBinding1(){
    	return BindingBuilder.bind(topicqueue1()).to(topicExchange()).with("topic.key1");
    }
    @Bean
    public org.springframework.amqp.core.Binding topicBinding2(){
    	return BindingBuilder.bind(topicqueue2()).to(topicExchange()).with("topic.#");
    }
    /*
     * fanout模式 交换机 EXchange  
     * */
    @Bean
    public FanoutExchange fanoutExchange(){
    	return new FanoutExchange(FANOUT_EXCHANGE);
    }
    
    @Bean
    public org.springframework.amqp.core.Binding fanoutBinding1(){
    	return BindingBuilder.bind(topicqueue1()).to(fanoutExchange());
    }
    @Bean
    public org.springframework.amqp.core.Binding fanoutBinding2(){
    	return BindingBuilder.bind(topicqueue2()).to(fanoutExchange());
    }
    
    
}
