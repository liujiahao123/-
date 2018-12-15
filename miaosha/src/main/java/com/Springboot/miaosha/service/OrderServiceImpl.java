package com.Springboot.miaosha.service;


import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Springboot.miaosha.dao.OrderDao;
import com.Springboot.miaosha.entity.GoodsVo;
import com.Springboot.miaosha.entity.MiaoShaUser;
import com.Springboot.miaosha.entity.MiaoshaOrder;
import com.Springboot.miaosha.entity.OrderInfo;

@Service
public class OrderServiceImpl {

	@Autowired
	
	OrderDao orderDao;
	
	
	public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userid, String goodsid) {
		// TODO Auto-generated method stub
		//查询redis不走mysql
		return orderDao.getMiaoshaOrderByUserIdGoodsId(userid,goodsid);
	}

    @Transactional
	public OrderInfo createOrder(MiaoShaUser user, GoodsVo goodsVo) {
		// TODO Auto-generated method stub
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAaddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goodsVo.getId());
		orderInfo.setGoodsName(goodsVo.getGoodsName());
		orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		orderDao.insert(orderInfo);
		MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
		miaoshaOrder.setGoodsId(goodsVo.getId());
		miaoshaOrder.setOrderId(orderInfo.getId());
		miaoshaOrder.setUserId(user.getId());
		orderDao.insertMiaoshaOrder(miaoshaOrder);
		//写入redis
		return orderInfo;
	}




	
	
}
