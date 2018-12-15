package com.Springboot.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Springboot.miaosha.Redis.RedisService;
import com.Springboot.miaosha.Redis.miaoshaUserKey;
import com.Springboot.miaosha.dao.UserDao;
import com.Springboot.miaosha.entity.MiaoShaUser;


@Service
public class MiaoShaUserService {
	
	 private static final String JESSION_TOKEN="token";
	 
     @Autowired
     private UserDao userdao;
     
     @Autowired
     RedisService redisService;
     
     
     public MiaoShaUser getById(long id){
    	 MiaoShaUser user =redisService.get(miaoshaUserKey.getById, ""+id, MiaoShaUser.class);
    	 if(user != null){
    		 return user;
    	 }
    	 user = userdao.getById(id);
    	 if(user != null){
    		 redisService.set(miaoshaUserKey.getById, ""+id, user);
    	 }
    	 return user;
     }
}
