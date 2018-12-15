package com.Springboot.miaosha.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Springboot.miaosha.Redis.MiaoshaKey;
import com.Springboot.miaosha.Redis.RedisService;
import com.Springboot.miaosha.Redis.miaoshaUserKey;
import com.Springboot.miaosha.entity.GoodsVo;
import com.Springboot.miaosha.entity.MiaoShaUser;
import com.Springboot.miaosha.entity.MiaoshaOrder;
import com.Springboot.miaosha.entity.OrderInfo;


@Service
public class MiaoshaServiceImpl {

	@Autowired
	GoodsServiceImpl goodsServiceImpl;
	
	@Autowired
	OrderServiceImpl orderServiceImpl;
	@Autowired
	private RedisService redisService;
	
	
	@Transactional
	public OrderInfo miaosha(MiaoShaUser user, GoodsVo goodsVo) {
		//减少库存
		Boolean success = goodsServiceImpl.reduceStock(goodsVo);
		//两张表都需要插入数据  miaosha_order he order_info
		if(success){
			OrderInfo orderInfo = orderServiceImpl.createOrder(user,goodsVo);
			return orderInfo;
		}else {
			//说明库存已经没有了
			setGoodsOver(goodsVo.getId());
			return null;
		}	
	}
	
	private void setGoodsOver(Long goodsid) {
    redisService.set(MiaoshaKey.isOver, goodsid+"", true);
		
	}
	
	private Boolean setGoodsOver(String goodsid) {
		// TODO Auto-generated method stub
		return redisService.exists(MiaoshaKey.isOver, goodsid);
	}

	public Long orderid(Long userid, String goodsid) {
		MiaoshaOrder miaoshaOrder = orderServiceImpl.getMiaoshaOrderByUserIdGoodsId(userid, goodsid);
		if(miaoshaOrder!=null){
			return miaoshaOrder.getOrderId();
		}else{
			Boolean isover = setGoodsOver(goodsid);
			if(isover){
				return (long) -1;
			}else{
				return (long) 0;
			}
		}
	}

	public boolean checkPath(String goodsid, Long id, String path) {
		// TODO Auto-generated method stub
		String eqString = redisService.get(MiaoshaKey.isPath, id+"_"+goodsid, String.class);
		return path.equals(eqString);
	}

	public BufferedImage getverifyCodelmg(MiaoShaUser user, long goodsId) {
		if(user==null || goodsId < 0){
			return null;
		}
		int width=80; 
		int height = 32;
		BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g =image.getGraphics();
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		g.setColor(Color.black);
		g.drawRect(0, 0, width-1, height-1);
		Random rdm=new Random();
		for (int i = 0; i < 50; i++) {
			int x=rdm.nextInt(width);
			int y=rdm.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}
		String verifyCode = chuxianverifyCode(rdm);
		g.setColor(new Color(0,100,0));
		g.setFont(new Font("Candara",Font.BOLD,24));
		g.drawString(verifyCode, 8, 24);
		g.dispose();
		int rmd=calc(verifyCode);
		redisService.set(MiaoshaKey.verifyCode, user.getId()+","+goodsId,rmd);
		return image;
	}
	
/*	public static void main(String[] args) {
		System.out.println(calc("1+3+1"));
	}*/
	private int calc(String exp) {
		try {
			ScriptEngineManager engineManager = new ScriptEngineManager();
			ScriptEngine engine = engineManager.getEngineByName("JavaScript");
			double hahDouble = (Double) engine.eval(exp);
			int i = (int)hahDouble;  
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	private static char[] ops = new char[]{'+','-','*'};

	private String chuxianverifyCode(Random rdm) {
		int number1 = rdm.nextInt(10);
		int number2 = rdm.nextInt(10);
		int number3 = rdm.nextInt(10);
		char op1=ops[rdm.nextInt(3)];
		char op2=ops[rdm.nextInt(3)];
		String exp=""+number1 + op1 +number2 +op2 + number3;
		return exp;
	}

	public boolean isTrueCode(MiaoShaUser user, String goodsid, int verifyCode) {
		Integer num = redisService.get(MiaoshaKey.verifyCode, user.getId()+","+goodsid, int.class);
		if(num == null || num-verifyCode!=0){
			return false;
		}
		redisService.del(MiaoshaKey.verifyCode, user.getId()+","+goodsid);
		return true;
		
	}



}
