package com.Springboot.miaosha.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.StringUtils;

public  class mobileUtil {
     public static Boolean validateMobile(String mobile){
    	 if("".equals(mobile) && StringUtils.isNullOrEmpty(mobile)){
    		 return false;
    	 }
    	 Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    	 Matcher m = p.matcher(mobile);
    	 System.out.println(m.matches()+"---");
    	 return m.matches();
    	}
}
