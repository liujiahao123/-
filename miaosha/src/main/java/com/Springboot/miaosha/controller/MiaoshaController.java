package com.Springboot.miaosha.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Springboot.miaosha.Redis.GoodsKey;
import com.Springboot.miaosha.Redis.MiaoshaKey;
import com.Springboot.miaosha.Redis.RedisService;
import com.Springboot.miaosha.Redis.miaoshaUserKey;
import com.Springboot.miaosha.entity.GoodsVo;
import com.Springboot.miaosha.entity.MiaoShaUser;
import com.Springboot.miaosha.entity.MiaoshaMes;
import com.Springboot.miaosha.entity.MiaoshaOrder;
import com.Springboot.miaosha.entity.OrderInfo;
import com.Springboot.miaosha.rabbitmq.MQSender;
import com.Springboot.miaosha.service.GoodsServiceImpl;
import com.Springboot.miaosha.service.MiaoshaServiceImpl;
import com.Springboot.miaosha.service.OrderInfoServiceImpl;
import com.Springboot.miaosha.service.OrderServiceImpl;
import com.Springboot.miaosha.util.Md5;
import com.Springboot.miaosha.util.UUIDUtil;
import com.Springboot.miaosha.xianliu.HttpCountLimit;
import com.mysql.jdbc.StringUtils;


@Controller
public class MiaoshaController implements InitializingBean{
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private GoodsServiceImpl goodsService;
	@Autowired
	private OrderServiceImpl orderService;
	@Autowired
	private OrderInfoServiceImpl OrderinfoServiceImpl;
	@Autowired
	private MiaoshaServiceImpl miaoshaServiceImpl;
	
	@Autowired
	MQSender mqSender;
	
	private Map<Long, Boolean> locahashMap =new HashMap<Long, Boolean>(); 
	
	/*将秒杀商品的数量初始化进redis中*/
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> list = goodsService.listGoods();
		if(list == null){
			return;
		}else{
			for (GoodsVo goodsVo:list) {
				redisService.set(GoodsKey.getGoodsStock, ""+goodsVo.getId(), goodsVo.getStockCount());
				locahashMap.put(goodsVo.getId(), false);
			}
		}
	}
	
	@RequestMapping("/miaosha/{path}/miaoshaAction")
	@ResponseBody
	public String miaosha(HttpServletResponse response,ModelMap modelMap,@CookieValue(value=UUIDUtil.JESSION_TOKEN,required=false) String coockToken,
			@RequestParam(value=UUIDUtil.JESSION_TOKEN,required=false) String requesToken,String goodsid,@PathVariable("path") String path){
		if(StringUtils.isNullOrEmpty(coockToken) && StringUtils.isNullOrEmpty(requesToken)){
			return "login";
		}
		
		String token=StringUtils.isNullOrEmpty(requesToken)?coockToken:requesToken;
		MiaoShaUser user = redisService.get(miaoshaUserKey.token, token, MiaoShaUser.class);
		/*验证path*/
		Boolean isBoolean = miaoshaServiceImpl.checkPath(goodsid,user.getId(),path);
		if(!isBoolean){
			return "12";
		}
		
		//内存标记  减少redis访问
		boolean isover = locahashMap.get(Long.valueOf(goodsid));
		if(isover){
			modelMap.addAttribute("mes", "商品已经没有库存了T_T!");
			return "13";
		}
		
		/*减redis库存*/ 
		long stock = redisService.decr(GoodsKey.getGoodsStock,goodsid);
		if(stock < 0){
			locahashMap.put(Long.valueOf(goodsid), true);
			modelMap.addAttribute("mes", "商品已经没有库存了!");
		}
		/*判断用户是不是已经秒杀过了*/
		MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsid);
		if(miaoshaOrder !=null ){
			modelMap.addAttribute("mes", "单个用户只能对同一款商品只能下一个订单!");
			return "11";
		}
		/*入队*/
		MiaoshaMes mes = new MiaoshaMes();
		mes.setGoodsId(Long.valueOf(goodsid));
		mes.setUser(user);
		mqSender.sendMiaoshaMes(mes);
		return "10";//表示排队中

		/*//判断库存  方案1
		GoodsVo goodsVo = goodsService.getGoods(Long.valueOf(goodsid));
		if(goodsVo.getStockCount() <=0 ){
			modelMap.addAttribute("mes", "商品已经没有库存了!");
			return "miaoshaOver";//秒杀失败
		}
		//判断同一个用户多次秒杀用户id和商品Id
		//System.out.println(user.getId()+"================"+goodsid);
		MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsid);
		if(miaoshaOrder !=null ){
			modelMap.addAttribute("mes", "单个用户只能对同一款商品只能下一个订单!");
			return "miaoshaOver";
		}
		//减库存  下订单  写入秒杀订单  事务
		OrderInfo orderinfo =  miaoshaServiceImpl.miaosha(user,goodsVo);
		modelMap.addAttribute("goodsVo",goodsVo);
		modelMap.addAttribute("orderinfo",orderinfo);
		return "orderInfo";*/

	}
	@RequestMapping("/miaosha/miaoshaResule")
	@ResponseBody
	public String miaoshaResule(HttpServletResponse response,ModelMap modelMap,@CookieValue(value=UUIDUtil.JESSION_TOKEN,required=false) String coockToken,
			@RequestParam(value=UUIDUtil.JESSION_TOKEN,required=false) String requesToken,String goodsid){
		if(StringUtils.isNullOrEmpty(coockToken) && StringUtils.isNullOrEmpty(requesToken)){
			return "login";
		}
		String token=StringUtils.isNullOrEmpty(requesToken)?coockToken:requesToken;
		MiaoShaUser user = redisService.get(miaoshaUserKey.token, token, MiaoShaUser.class);
		long result= miaoshaServiceImpl.orderid(user.getId(),goodsid);
		return result+"";
	}
	
	@RequestMapping("/miaosha/obtainPath")
	@HttpCountLimit(count=5,second=6,Signin = true)
	@ResponseBody
	public String obtainPath(HttpServletResponse response,ModelMap modelMap,@CookieValue(value=UUIDUtil.JESSION_TOKEN,required=false) String coockToken,
			@RequestParam(value=UUIDUtil.JESSION_TOKEN,required=false) String requesToken,String goodsid,@RequestParam("verifyCode")int verifyCode){
		if(StringUtils.isNullOrEmpty(coockToken) && StringUtils.isNullOrEmpty(requesToken)){
			return "login";
		}
		String token=StringUtils.isNullOrEmpty(requesToken)?coockToken:requesToken;
		MiaoShaUser user = redisService.get(miaoshaUserKey.token, token, MiaoShaUser.class);
		boolean iscode=miaoshaServiceImpl.isTrueCode(user,goodsid,verifyCode);
		if(!iscode){
			return "verifyCodefalse";
		}
		String pathString = Md5.md5(UUIDUtil.uuid())+"1a2b3c";
		redisService.set(MiaoshaKey.isPath, ""+user.getId()+"_"+goodsid, pathString);
		return pathString;
	}
	
	@RequestMapping("/orderInfo/success")
	public String hrefOrderdetail(HttpServletResponse response,ModelMap modelMap,@CookieValue(value=UUIDUtil.JESSION_TOKEN,required=false) String coockToken,
			@RequestParam(value=UUIDUtil.JESSION_TOKEN,required=false) String requesToken,String orderld){
		if(StringUtils.isNullOrEmpty(coockToken) && StringUtils.isNullOrEmpty(requesToken)){
			return "login";
		}
		OrderInfo orderinfo = OrderinfoServiceImpl.findId(Long.valueOf(orderld));
		if(orderinfo !=null){
			modelMap.addAttribute("orderinfo",orderinfo);
		}
		GoodsVo goodsVo = goodsService.getGoods(orderinfo.getGoodsId());
		modelMap.addAttribute("goodsVo",goodsVo);
		return "orderInfo";
		
	}
	
	@RequestMapping("/miaoSha/verifyCodelmg")
	public String verifyCodelmg(HttpServletResponse response,ModelMap modelMap,@CookieValue(value=UUIDUtil.JESSION_TOKEN,required=false) String coockToken,
			@RequestParam(value=UUIDUtil.JESSION_TOKEN,required=false) String requesToken,long goodsid){
		if(StringUtils.isNullOrEmpty(coockToken) && StringUtils.isNullOrEmpty(requesToken)){
			return "login";
		}
		String token=StringUtils.isNullOrEmpty(requesToken)?coockToken:requesToken;
		MiaoShaUser user = redisService.get(miaoshaUserKey.token, token, MiaoShaUser.class);
		BufferedImage bufferedImage = miaoshaServiceImpl.getverifyCodelmg(user,goodsid);
		try {
			OutputStream out = response.getOutputStream();
			ImageIO.write(bufferedImage, "JPEG", out);
			out.flush();
			out.close();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return "验证码出错";
		}

		
	}

	
}
