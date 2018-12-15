package com.Springboot.miaosha.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Springboot.miaosha.dao.GoodsDao;
import com.Springboot.miaosha.entity.Goods;
import com.Springboot.miaosha.entity.GoodsVo;
import com.Springboot.miaosha.entity.MiaoshaGoods;


@Service
public class GoodsServiceImpl {
	
    @Autowired
    private GoodsDao goodsdao;
    
    
    public List<GoodsVo> listGoods(){
    	return goodsdao.listGoods();
    }
    
    public GoodsVo getGoods(long goodsid){
    	return goodsdao.getGoods(goodsid);
    }

	public Boolean reduceStock(GoodsVo goodsVo) {
		MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
		miaoshaGoods.setGoodsId(goodsVo.getId());
		int ret = goodsdao.reduceStock(miaoshaGoods);
		return ret>0;
		
	}
}
