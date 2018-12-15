package com.Springboot.miaosha.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Springboot.miaosha.Redis.RedisService;
import com.Springboot.miaosha.entity.MiaoshaMes;


@Service
public class MQSender {
	
	@Autowired
	AmqpTemplate amqpTemplate;
	
	
	 /* 异步秒杀*/
		public void sendMiaoshaMes(MiaoshaMes mes) {
			 String obj = RedisService.beanToString(mes);
			   System.out.println("MIAOSHAQUEUE==="+obj);
			   amqpTemplate.convertAndSend(MQConfig.MIAOSHAQUEUE,obj);
		}
	
	
	
	
	
	
	
	
	
	
	
	
 /*  public void send(Object message){
	   String obj = RedisService.beanToString(message);
	   System.out.println("send==="+obj);
	   amqpTemplate.convertAndSend(MQConfig.QUEUE,obj);
   }
   
   
   public void sendtopic(Object message){
	   String obj = RedisService.beanToString(message);
	   System.out.println("send==="+obj);
		   amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",message+"TOP1");//topicBinding1() topicBinding2()
		   amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",message+"TOP2");//topicBinding2()
   }
   
   public void sendtofanout(Object message){
	   String obj = RedisService.beanToString(message);
	   System.out.println("sendTOP1fanout==="+obj);
	   for (int i=0;i<100;i++){
	   amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",message+"TOP1fanout"+i);
	   }
	   
   }*/

 
}
