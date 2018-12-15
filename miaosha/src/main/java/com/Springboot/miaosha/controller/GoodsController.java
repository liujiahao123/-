package com.Springboot.miaosha.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.Springboot.miaosha.Redis.GoodsKey;
import com.Springboot.miaosha.Redis.RedisService;
import com.Springboot.miaosha.Redis.miaoshaUserKey;
import com.Springboot.miaosha.entity.GoodsVo;
import com.Springboot.miaosha.entity.MiaoShaUser;
import com.Springboot.miaosha.service.GoodsServiceImpl;
import com.Springboot.miaosha.service.OrderInfoServiceImpl;
import com.Springboot.miaosha.service.OrderServiceImpl;
import com.Springboot.miaosha.util.UUIDUtil;
import com.Springboot.miaosha.xianliu.HttpCountLimit;
import com.mysql.jdbc.StringUtils;


@Controller
public class GoodsController {

	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private GoodsServiceImpl goodsService;
	@Autowired
	private OrderServiceImpl orderService;
	@Autowired
	private OrderInfoServiceImpl OrderinfoServiceImpl;
	
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
	
	@Autowired
	ApplicationContext applicationContext;
	
	//拍卖列表页
	@RequestMapping(value ="/togoodslist",produces="text/html")
	@ResponseBody
	public String togoodslist(HttpServletResponse response,HttpServletRequest request,ModelMap modelMap,@CookieValue(value=UUIDUtil.JESSION_TOKEN,required=false) String coockToken,
			@RequestParam(value=UUIDUtil.JESSION_TOKEN,required=false) String requesToken){
		if(StringUtils.isNullOrEmpty(coockToken) && StringUtils.isNullOrEmpty(requesToken)){
			return "login";
		}

		String html=redisService.get(GoodsKey.getGoodsList, "", String.class);
		if(!StringUtils.isNullOrEmpty(html)){
			return html;
		}
		String token=StringUtils.isNullOrEmpty(requesToken)?coockToken:requesToken;
		MiaoShaUser user = redisService.get(miaoshaUserKey.token, token, MiaoShaUser.class);
		List<GoodsVo> list = goodsService.listGoods();
		if(user !=null){
			modelMap.addAttribute("user", user);
			modelMap.addAttribute("GoodsVoList", list);
			
			//手动渲染页面
			SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), modelMap,applicationContext);
			html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
			if(!StringUtils.isNullOrEmpty(html)){
				redisService.set(GoodsKey.getGoodsList,"",html);
			}
			return html;
			//return "goods_list";
		}else{
			return "login";
		}
		
	}
	
	//进入商品详情页
	@RequestMapping(value = "/goods/to_derail/{id}",produces="text/html")
	@ResponseBody
	public String togoodsDetail(HttpServletResponse response,HttpServletRequest request,ModelMap modelMap,@CookieValue(value=UUIDUtil.JESSION_TOKEN,required=false) String coockToken,
			@RequestParam(value=UUIDUtil.JESSION_TOKEN,required=false) String requesToken,@PathVariable("id")String goodsid){
		if(StringUtils.isNullOrEmpty(coockToken) && StringUtils.isNullOrEmpty(requesToken)){
			return "login";
		}
		//查看redis中是否有缓存
				String html=redisService.get(GoodsKey.getGoodsDetail, ""+goodsid, String.class);
				if(!StringUtils.isNullOrEmpty(html)){
					return html;
				}
		String token=StringUtils.isNullOrEmpty(requesToken)?coockToken:requesToken;
		MiaoShaUser user = redisService.get(miaoshaUserKey.token, token, MiaoShaUser.class);
		modelMap.addAttribute("user", user);
		GoodsVo goodsVo = goodsService.getGoods(Long.valueOf(goodsid));
		if(goodsVo == null){
			return "togoodslist";
		}
		Timestamp date =goodsVo.getStartDate();
		modelMap.addAttribute("goodsVo", goodsVo);
		long starAt =goodsVo.getStartDate().getTime();//秒杀开始时间
		long endAt =goodsVo.getEndDate().getTime();//结束开始时间
		long now =System.currentTimeMillis();//当前时间
		
		 /* Date nowTime = new Date(now);
		  Date starAtTime = new Date(starAt);
		  SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  String nowTime1 = sdFormatter.format(nowTime);
		  String starAtTime1 = sdFormatter.format(starAtTime);
		System.out.println(starAtTime1+"开始时间");
		System.out.println(nowTime1+"当前时间");*/
		int miaoshaState =0;
		int remiaoSeconds = 0;
		//获取秒杀状态
		if(now < starAt){//秒杀没有开始 倒计时
			miaoshaState =0;
			remiaoSeconds = (int)(starAt-now)/1000;
		}else if(now > endAt){//秒杀已经结束 倒计时
			miaoshaState =2;
			remiaoSeconds = -1;
		}else{//秒杀进行中
			miaoshaState =1;
		}
		modelMap.addAttribute("miaoshaState", miaoshaState);
		modelMap.addAttribute("remiaoSeconds", remiaoSeconds);
		
		SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), modelMap,applicationContext);
		html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
		if(!StringUtils.isNullOrEmpty(html)){
			redisService.set(GoodsKey.getGoodsDetail,""+goodsid,html);
		}
		return html;
		//return "goods_detail";
	}
	

}
