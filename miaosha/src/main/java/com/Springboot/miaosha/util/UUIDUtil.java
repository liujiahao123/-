package com.Springboot.miaosha.util;

import java.util.UUID;

public class UUIDUtil {
   
	public static final String JESSION_TOKEN = "token";
	
	public static String uuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
