package com.Springboot.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5 {

	
	  private static String salt="1a2b3c4d5";
	  
	  public static String md5(String str){
		  return DigestUtils.md5Hex(str);
	  }
	
	  public static String inputPassToFormPass(String inputPass){
		  String sta=""+salt.charAt(0)+salt.charAt(5)+inputPass+salt.charAt(3)+salt.charAt(6);
		  return md5(sta);
	  }
	  
	  public static String FormPassToDBPass(String formPass,String salt){
		  String sta=""+salt.charAt(0)+salt.charAt(5)+formPass+salt.charAt(3)+salt.charAt(6);
		  return md5(sta);
	  }
	  
	  public static String inputPassToDBPass(String inputPass,String salt) {//两次加密后存入数据库的密码
		String md51pass =inputPassToFormPass(inputPass);
		String dbpass = FormPassToDBPass(md51pass,salt);
		return dbpass;
	 }
	  public static void main(String[] args) {
		  System.out.println(inputPassToFormPass("123456"));//efb7d9a70f4a29f1ad40e052faee3441
		  System.out.println(FormPassToDBPass("efb7d9a70f4a29f1ad40e052faee3441","1a2b3c4d5"));//5c12ca0e7c295ac17757a70c5401e064
	}
	
}
