package com.Springboot.miaosha.Redis;

public class OrderKey extends BasePrefix {

	public OrderKey(String prefix) {
		super(prefix);
		// TODO Auto-generated constructor stub
	}
	
	public static OrderKey getById =new OrderKey("id");
	public static OrderKey getByName =new OrderKey("name");
	
/*	@Override
	public String getPrefix() {
		String name = getClass().getSimpleName();//获取类名
		return name+":"+"0key";
	}*/

}
