package com.Springboot.miaosha.Redis;

public class MiaoshaKey extends BasePrefix {

	private MiaoshaKey(Integer expireSesonds,String prefix) {
		super(expireSesonds,prefix);
	}
	
	public static MiaoshaKey isOver =new MiaoshaKey(0,"ms");
	public static MiaoshaKey isPath =new MiaoshaKey(60,"mp");
	public static MiaoshaKey verifyCode =new MiaoshaKey(300,"vc");
	public static MiaoshaKey withExpire(int s){
		return new MiaoshaKey(s,"we");
	}

}
