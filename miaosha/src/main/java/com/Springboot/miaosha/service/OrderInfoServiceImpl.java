package com.Springboot.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Springboot.miaosha.dao.OrderInfoDao;
import com.Springboot.miaosha.entity.OrderInfo;

@Service
public class OrderInfoServiceImpl {

	@Autowired
	private OrderInfoDao orderdao;
	
	public OrderInfo findId(long orderid){
		return orderdao.findId(orderid);
	}
	
}
