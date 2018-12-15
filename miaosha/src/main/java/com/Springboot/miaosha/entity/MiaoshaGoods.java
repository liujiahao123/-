package com.Springboot.miaosha.entity;

import java.sql.Date;

import org.assertj.core.internal.Integers;

public class MiaoshaGoods {
	 private Long id;
	 private Long goodsId;
	 private Integer stockCount;
	 private Date strtDate;
	 private Date endDate;
	 private Double miaoshaPrice;
	 
	 

	public Double getMiaoshaPrice() {
		return miaoshaPrice;
	}
	public void setMiaoshaPrice(Double miaoshaPrice) {
		this.miaoshaPrice = miaoshaPrice;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStrtDate() {
		return strtDate;
	}
	public void setStrtDate(Date strtDate) {
		this.strtDate = strtDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
