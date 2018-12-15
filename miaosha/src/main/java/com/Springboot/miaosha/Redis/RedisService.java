package com.Springboot.miaosha.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.Springboot.miaosha.entity.Demo;
import com.Springboot.miaosha.entity.MiaoShaUser;
import com.alibaba.fastjson.JSON;


@Service
public class RedisService {
    @Autowired
    JedisPool jedisPool;
  
    
    public <T> T get(KeyPrefix keyPrefix,String key, Class<T> clazz){
    	Jedis jedis=null;
    	try {
    		jedis = jedisPool.getResource();
    		String keyString = keyPrefix.getPrefix()+key;
    		String str = jedis.get(keyString);
    		T t = stringToBean(str,clazz);
    		return t;
		} finally {
		 returnToPoll(jedis);
		}
    }
    public <T> Boolean set(KeyPrefix keyPrefix,String key, T value){
    	Jedis jedis=null;
    	try {
    		jedis = jedisPool.getResource();
    		String str = beanToString(value);
    		if(str==null || str.length()<=0){
    			return false;
    		}
    		String keyString = keyPrefix.getPrefix()+key;//生成真的key keyPrefix.getPrefix()获取类名  ： id +1
    		System.out.println(keyString+"====keyString");
    		int time = keyPrefix.expirSesonds();
    		if(time<=0){
    			jedis.set(keyString, str);
    		}else{
    			jedis.setex(keyString, time, str);
    		}
    		
    		return true;
		} finally {
		 returnToPoll(jedis);
		}
    }
    
    
    public <T> Boolean exists(KeyPrefix keyPrefix,String key){
    	Jedis jedis=null;
    	try {
    		jedis = jedisPool.getResource();
    		String keyString = keyPrefix.getPrefix()+key;//生成真的key
    		return jedis.exists(keyString);
		} finally {
		 returnToPoll(jedis);
		}
    }
    
    public  Long del(KeyPrefix keyPrefix,String key){
    	Jedis jedis=null;
    	try {
    		jedis = jedisPool.getResource();
    		return jedis.del(keyPrefix.getPrefix()+key);
		} finally {
		  returnToPoll(jedis);
		}
    }
    
    public <T> Long incr(KeyPrefix keyPrefix,String key){
    	Jedis jedis=null;
    	try {
    		jedis = jedisPool.getResource();
    		String keyString = keyPrefix.getPrefix()+key;//生成真的key
    		return jedis.incr(keyString);
		} finally {
		 returnToPoll(jedis);
		}
    }
    
    public <T> Long decr(KeyPrefix keyPrefix,String key){
    	Jedis jedis=null;
    	try {
    		jedis = jedisPool.getResource();
    		String keyString = keyPrefix.getPrefix()+key;//生成真的key
    		return jedis.decr(keyString);
		} finally {
		 returnToPoll(jedis);
		}
    }
    
    
    public static <T> String beanToString(T value){
    	if(value==null){
    		return null;
    	}
    	Class<?> clazz = value.getClass();
    	if(clazz == int.class || clazz==Integer.class){
    		return value.toString();
    	}else if(clazz == String.class){
    		return (String) value;
    	}else if(clazz == long.class || clazz==Long.class){
    		return value+"";
    	}else{
    		return JSON.toJSONString(value);
    	}
    	
    }
    public static <T> T stringToBean(String str,Class<T> clazz) {
	    if(str ==null ||str.length()<=0 || clazz ==null){
	    	return null;
	    }
	    if(clazz == int.class || clazz==Integer.class){
    		return (T)Integer.valueOf(str);
    	}else if(clazz == String.class){
    		return (T)str;
    	}else if(clazz == long.class || clazz==Long.class){
    		return (T)Long.valueOf(str);
    	}else{
    		return JSON.toJavaObject(JSON.parseObject(str), clazz);
    	}

	}

	private void returnToPoll(Jedis jedis) {
		if(jedis !=null){
			jedis.close();
		}
		
	}



	
     
}
