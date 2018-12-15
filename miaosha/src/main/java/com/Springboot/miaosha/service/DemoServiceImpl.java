package com.Springboot.miaosha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Springboot.miaosha.dao.DemoMappper;
import com.Springboot.miaosha.entity.Demo;

@Service
public class DemoServiceImpl {

	@Autowired
	private DemoMappper demoMappper;
	
	public List<Demo> likeName(String name){
		return demoMappper.likeName(name);
	}
	
	
	public void save(Demo demo){
		demoMappper.save(demo);
	}
	
	
	public void delete(long id){
		demoMappper.delete(id);
	}
	
}
