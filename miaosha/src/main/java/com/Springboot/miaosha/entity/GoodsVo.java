package com.Springboot.miaosha.entity;

import java.sql.Date;
import java.sql.Timestamp;


public class GoodsVo extends Goods {
	 private Integer stockCount;
	 private Timestamp  startDate;
	 private Timestamp endDate;
	 private Double miaoshaPrice;
	 
	 
	 
	 public Double getMiaoshaPrice() {
		return miaoshaPrice;
	}
	public void setMiaoshaPrice(Double miaoshaPrice) {
		this.miaoshaPrice = miaoshaPrice;
	}
	

	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}


}
