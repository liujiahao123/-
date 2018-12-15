package com.Springboot.miaosha.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.Springboot.miaosha.entity.OrderInfo;

public interface OrderInfoDao {

	@Select("SELECT * from order_info where id = #{orderid}")
	OrderInfo findId(@Param("orderid")long orderid);
}
