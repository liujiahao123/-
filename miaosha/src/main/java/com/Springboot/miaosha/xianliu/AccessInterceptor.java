package com.Springboot.miaosha.xianliu;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.Springboot.miaosha.Redis.MiaoshaKey;
import com.Springboot.miaosha.Redis.RedisService;
import com.Springboot.miaosha.Redis.miaoshaUserKey;
import com.Springboot.miaosha.entity.MiaoShaUser;
import com.Springboot.miaosha.util.UUIDUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


@Service
public class AccessInterceptor extends HandlerInterceptorAdapter{
	@Autowired
	private RedisService redisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		StringBuffer key=new StringBuffer();
		key=request.getRequestURL();
		JSONObject jsonObject=new JSONObject();
		System.out.println(handler instanceof HandlerMethod);
		if(handler instanceof HandlerMethod){
			MiaoShaUser user=null;
			String redisUserString = csname(request);
			if(redisUserString!=null && redisUserString != ""){
				 user = redisService.get(miaoshaUserKey.token, redisUserString, MiaoShaUser.class);
				 if(user == null){
					 jsonObject.put("code", "-1");
					 jsonObject.put("mes", "你还没有完成登录!");
					 render(response,jsonObject);
				 }
			}
			
			HandlerMethod hm=(HandlerMethod) handler;
			HttpCountLimit hl = hm.getMethodAnnotation(HttpCountLimit.class);
			if(hl == null){
				return true;
			}
			int sec = hl.second();
			boolean boo = hl.Signin();
			int count = hl.count();
			key.append("_"+user.getId());
			MiaoshaKey mk = MiaoshaKey.withExpire(sec);
			Integer num=redisService.get(mk, key.toString(), Integer.class);
			if(num==null){
				redisService.set(mk, key.toString(), 1);
			}else if(num<count){
				redisService.incr(mk, key.toString());
			}else{
				jsonObject.put("code", "-2");
				 jsonObject.put("mes", "操作太快休息休息!");
				 render(response,jsonObject);
				 return false;
			}
		}
		return true;
	}
	public String csname(HttpServletRequest request) {
		Cookie[] cs=request.getCookies();
		if(null!=cs && cs.length>0){
			for (Cookie css : cs) {
				if(css.getName().equals(UUIDUtil.JESSION_TOKEN)){
					return css.getValue();
				}
			}
		}
		return null;
	}
	private void render(HttpServletResponse response,JSONObject jsonObject){
		try {
			response.setContentType("application/json;charset=UTF-8");
			OutputStream outputStream =response.getOutputStream();
			String string=JSON.toJSONString(jsonObject);
			outputStream.write(string.getBytes());
			outputStream.flush();
			outputStream.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
