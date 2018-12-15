package com.Springboot.miaosha.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.Springboot.miaosha.entity.MiaoshaOrder;
import com.Springboot.miaosha.entity.OrderInfo;


@Mapper
public interface OrderDao {

	@Select("SELECT * FROM miaosha_order WHERE user_id=#{userid} and goods_id=#{goodsid}")
	MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userid")long userid, @Param("goodsid")String goodsid);
   
	@Insert("INSERT INTO order_info(user_id,goods_id,delivery_addr_id,goods_name,goods_count,goods_price,order_channel,status,create_date) "
			+ "VALUES(#{userId},#{goodsId},#{deliveryAaddrId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
	@SelectKey(keyColumn="id",keyProperty="id",resultType=long.class,before=false,statement="select last_insert_id()")
	long insert(OrderInfo orderInfo);

	@Insert("INSERT INTO miaosha_order(user_id,order_id,goods_id) VALUES(#{userId},#{orderId},#{goodsId})")
	void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

}
