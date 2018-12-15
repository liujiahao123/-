package com.Springboot.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.Springboot.miaosha.Redis.OrderKey;
import com.Springboot.miaosha.Redis.RedisService;
import com.Springboot.miaosha.Redis.UserKey;
import com.Springboot.miaosha.entity.Demo;
import com.Springboot.miaosha.result.CodeMes;
import com.Springboot.miaosha.result.Result;



@RestController
public class helloController {

	@Autowired
	private RedisService redisService;
	
	
	@RequestMapping("hello")
	@ResponseBody
	public Result<String> hello(){
		return Result.success("hello LJH");
	}
	
	
	@RequestMapping("hello2")
	@ResponseBody
	public Result<String> hello2(){
		return Result.error(CodeMes.SERVICE_ERROR);
	}
	
	@RequestMapping("redis/get")
	@ResponseBody
	public Result<Demo> redisGet(){
		Demo val = redisService.get(OrderKey.getById,"2", Demo.class);
		return Result.success(val);
	}
	@RequestMapping("redis/set")
	@ResponseBody
	public Result<Boolean> redisSet(){
		Demo demo =new Demo();
		demo.setId(5);
		demo.setName("刘家豪");
		System.out.println(OrderKey.getById+"kkkk");
		Boolean val = redisService.set(OrderKey.getById,"2", demo);//UserKey:id1
		return Result.success(true);
	}


}
