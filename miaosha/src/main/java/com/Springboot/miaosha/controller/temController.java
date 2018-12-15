package com.Springboot.miaosha.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Springboot.miaosha.rabbitmq.MQSender;
import com.Springboot.miaosha.result.Result;

@Controller
@RequestMapping("/tem")
public class temController {
	
	
	@Autowired
	MQSender mqSender;
	
	
	@RequestMapping("/thymeleaf")
	public String home(ModelMap modelMap) {
        modelMap.put("name", "Magical Sam");
        List<String> list = new ArrayList<>();
        list.add("sam a");
        list.add("sam b");
        list.add("sam c");
        list.add("sam d");
        modelMap.put("list", list);
		return "index";
	}
	/*@RequestMapping("/mq")
	@ResponseBody
	public Result<String> mq(ModelMap modelMap) {
		mqSender.send("hello mq");
		return Result.success("mq");
	}
	
	@RequestMapping("/mq/topic")
	@ResponseBody
	public Result<String> topic(ModelMap modelMap) {
		mqSender.sendtopic("hello sendtopic mq");
		return Result.success("mq");
	}
	
	@RequestMapping("/mq/fanout")
	@ResponseBody
	public Result<String> fanout(ModelMap modelMap) {
		mqSender.sendtofanout("hello sendfanout mq");
		return Result.success("mq");
	}*/
}
