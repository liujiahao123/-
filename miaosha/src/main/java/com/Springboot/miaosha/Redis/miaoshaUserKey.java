package com.Springboot.miaosha.Redis;

public class miaoshaUserKey extends BasePrefix {

	private miaoshaUserKey(String prefix) {
		super(prefix);
	}
	
	public static miaoshaUserKey token =new miaoshaUserKey("tk");
	public static miaoshaUserKey getByName =new miaoshaUserKey("name");
	public static miaoshaUserKey getById =new miaoshaUserKey("id");
	
	

}
