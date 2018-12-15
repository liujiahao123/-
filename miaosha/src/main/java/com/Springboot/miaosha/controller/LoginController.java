package com.Springboot.miaosha.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Springboot.miaosha.Redis.RedisService;
import com.Springboot.miaosha.Redis.miaoshaUserKey;
import com.Springboot.miaosha.entity.MiaoShaUser;
import com.Springboot.miaosha.result.CodeMes;
import com.Springboot.miaosha.result.Result;
import com.Springboot.miaosha.service.MiaoShaUserService;
import com.Springboot.miaosha.util.Md5;
import com.Springboot.miaosha.util.UUIDUtil;
import com.Springboot.miaosha.util.mobileUtil;
import com.mysql.jdbc.StringUtils;


@Controller
public class LoginController {

	
	

	@Autowired
    private MiaoShaUserService miaoShaUserService;
	
	@Autowired
	private RedisService redisService;
	
	@RequestMapping("/tologin")
	public String home(ModelMap modelMap) {
		return "login";
	}
	
	@RequestMapping("/dologin")
	@ResponseBody
	public Result<Boolean> dologin(HttpServletResponse response,String mobile,String password) {
		if("".equals(mobile)){
			return Result.error(CodeMes.MOBILE_NULL);
		} 
        if("".equals(password)){
        	return Result.error(CodeMes.PASS_NULL);
		}
        if(!mobileUtil.validateMobile(mobile)){
        	return Result.error(CodeMes.MOBILE_ERROR);
        }
        MiaoShaUser DBuser = miaoShaUserService.getById(Long.valueOf(mobile));
        if(DBuser ==null){
        	return Result.error(CodeMes.USER_NULL);
        }
        String dBPass =DBuser.getPassword();
        String dBsalt=DBuser.getSalt();
        String FormToDbpass = Md5.FormPassToDBPass(password, dBsalt);
        if(!dBPass.equals(FormToDbpass)){
        	return Result.error(CodeMes.PASS_ERROR);
        }
        String token=UUIDUtil.uuid();
        redisService.set(miaoshaUserKey.token, token, DBuser);
        Cookie cookie=new Cookie(UUIDUtil.JESSION_TOKEN, token);
        cookie.setMaxAge(3600*24);
        cookie.setPath("/");
        response.addCookie(cookie);
		return Result.success(true);
	}
	

	
	@RequestMapping("/logout")
	@ResponseBody
	public String logout(HttpServletResponse response,@CookieValue(value=UUIDUtil.JESSION_TOKEN,required=false) String coockToken,
			@RequestParam(value=UUIDUtil.JESSION_TOKEN,required=false) String requesToken){
		if(StringUtils.isNullOrEmpty(coockToken) && StringUtils.isNullOrEmpty(requesToken)){
			return "true";
		}
		String token=StringUtils.isNullOrEmpty(requesToken)?coockToken:requesToken;
		redisService.del(miaoshaUserKey.token,token);
		Cookie cookie=new Cookie(UUIDUtil.JESSION_TOKEN, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
		return "true";
	}
	
	
	public void addCookie(MiaoShaUser DBuser,HttpServletResponse response) {
		String token=UUIDUtil.uuid();
        redisService.set(miaoshaUserKey.token, token, DBuser);
        Cookie cookie=new Cookie(UUIDUtil.JESSION_TOKEN, token);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);
	}
}
