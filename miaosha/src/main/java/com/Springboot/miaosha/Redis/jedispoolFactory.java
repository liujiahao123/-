package com.Springboot.miaosha.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
@Service
public class jedispoolFactory {
	
	@Autowired
	RedisConfig redisConfig;
	
	
	@Bean
    public JedisPool jedisPoolFactory(){
    	JedisPoolConfig poolConfig=new JedisPoolConfig();
    	poolConfig.setMaxTotal(redisConfig.getMaxactive());
    	poolConfig.setMaxWaitMillis(redisConfig.getMaxwait()*1000);
    	poolConfig.setMaxIdle(redisConfig.getMaxidle());
    	JedisPool jedisPool =new JedisPool(poolConfig,redisConfig.getHost(),redisConfig.getPort(),redisConfig.getTimeout()*1000);
    	return jedisPool;
    }
}
