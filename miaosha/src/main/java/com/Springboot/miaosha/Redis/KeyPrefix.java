package com.Springboot.miaosha.Redis;

public interface KeyPrefix {
     
	public int expirSesonds();
	
	public String getPrefix();	
	
}
