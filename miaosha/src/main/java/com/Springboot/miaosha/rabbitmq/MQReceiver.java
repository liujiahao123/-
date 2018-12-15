package com.Springboot.miaosha.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Springboot.miaosha.Redis.RedisService;
import com.Springboot.miaosha.entity.GoodsVo;
import com.Springboot.miaosha.entity.MiaoShaUser;
import com.Springboot.miaosha.entity.MiaoshaMes;
import com.Springboot.miaosha.entity.MiaoshaOrder;
import com.Springboot.miaosha.service.GoodsServiceImpl;
import com.Springboot.miaosha.service.MiaoshaServiceImpl;
import com.Springboot.miaosha.service.OrderInfoServiceImpl;
import com.Springboot.miaosha.service.OrderServiceImpl;


@Service
public class MQReceiver {
	@Autowired
	private GoodsServiceImpl goodsService;
	@Autowired
	private OrderServiceImpl orderService;
	@Autowired
	private OrderInfoServiceImpl OrderinfoServiceImpl;
	@Autowired
	private MiaoshaServiceImpl miaoshaServiceImpl;
	
	@RabbitListener(queues=MQConfig.MIAOSHAQUEUE)
	public void miaoshareveice(String message){
		System.out.println(message+"====reveice");
		MiaoshaMes  mesobj = RedisService.stringToBean(message, MiaoshaMes.class);
		MiaoShaUser user=mesobj.getUser();
		long  goodsid=mesobj.getGoodsId(); 
		GoodsVo goodsVo = goodsService.getGoods(Long.valueOf(goodsid));
		if(goodsVo.getStockCount() <=0 ){
			return;//秒杀失败
		}
		MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),String.valueOf(goodsid));
		if(miaoshaOrder !=null ){
			return;
		}
		miaoshaServiceImpl.miaosha(user,goodsVo);
		
	}
	
	
	
	
	
	
	
	/*@RabbitListener(queues=MQConfig.QUEUE)
	public void reveice(String message){
		System.out.println(message+"====reveice");
	}
	
	@RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
	public void topicreveice1(String message){
		System.out.println(message+"====topic queue reveice1");
	}
	        打印数据 发送两条数据第一个queue只拿到了第一个消息  第二个使用通配符拿到了两条  send===hello sendtopic mq
			hello sendtopic mqTOP1====topic queue reveice2
			hello sendtopic mqTOP1====topic queue reveice1
			hello sendtopic mqTOP2====topic queue reveice2
	@RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
	public void topicreveice2(String message){
		System.out.println(message+"====topic queue reveice2");
	}*/
}
