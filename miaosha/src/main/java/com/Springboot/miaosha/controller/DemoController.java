package com.Springboot.miaosha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Springboot.miaosha.entity.Demo;
import com.Springboot.miaosha.result.Result;
import com.Springboot.miaosha.service.DemoServiceImpl;
import com.github.pagehelper.PageHelper;

@RestController
public class DemoController {
	
	@Autowired
	private DemoServiceImpl demoServiceImpl;
	
	@RequestMapping("/likeName")
	public Result<List<Demo>> likeName(String name){
		/*
		 * 第一个参数：第几页;
		 * 第二个参数：每页获取的条数.
		 */
		PageHelper.startPage(1, 2);
		List<Demo> list = demoServiceImpl.likeName(name);
		return Result.success(list);
	}
	
	@RequestMapping("/save")
	@Transactional
	public Demo save(){
		Demo demo = new Demo();
		demo.setName("张三");
		demoServiceImpl.save(demo);
		return demo;
	}
	
	
}
