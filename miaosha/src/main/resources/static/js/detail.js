  $(function(){
	 detailinit(); 
  });
  function detailinit(){
	  var remiaoSeconds =$("#remiaoSeconds").val();
	  var timeout;
	  if(remiaoSeconds>0){//秒杀未开始
		  $("#miaoshaBtn").attr("disabled",true);
		  timeout = setTimeout(function(){
			  $("#remiaoSeconds").val(remiaoSeconds-1);
			  $("#countDown").text(remiaoSeconds-1);
			  detailinit();
		  },1000);
	  }else if(remiaoSeconds == 0){//秒杀进行中
		  $("#miaoshaBtn").attr("disabled",false);
		  
	  if(timeout){
		  clearTimeout(timeout);
	  }
		  $("#zhuantai").text("秒杀进行中");
		  
		  $("#verifyCodelmg").attr("src","/miaoSha/verifyCodelmg?goodsid="+$("#goodsid").val());
		  $("#verifyCodelmg").show();
		  $("#verifyCode").show();
	  }else{//秒杀已结束
		  $("#miaoshaBtn").attr("disabled",true);
		  $("#zhuantai").text("秒杀已结束");
		  $("#verifyCodelmg").hide();
		  $("#verifyCode").hide();
	  }
  }
function obtainPath(){
	  var goodsid =$("#goodsid").val();
	  var verifyCode =$("#verifyCode").val();
	  if(verifyCode=="" || verifyCode==null){
		  layer.msg("请输入验证码!");
		  return false;
	  }
		$.ajax({
	        url: "/miaosha/obtainPath",
	        type: "get",
	        data: {
	        	goodsid:goodsid,
	        	verifyCode:verifyCode
	        },
	        success: function(data) {
	        	if(data == "verifyCodefalse"){
	        		layer.msg("验证码不正确");
	        		return false;
	        	}
	        	if(data.code == "-2" || data.code == "-1"){
	        		layer.msg(data.mes);
	        		return false;
	        	}
	        	miaosha(data);
	        },
	        error: function(data) {
	            layer.msg("秒杀请求失败");
	        }
	    });
	}
function reImg(){
	$("#verifyCode").val("");
	 $("#verifyCodelmg").attr("src","/miaoSha/verifyCodelmg?goodsid="+$("#goodsid").val()+"&timestamp="+new Date().getTime());
}
function miaosha(path){
	var goodsid =$("#goodsid").val();
	if(goodsid!="" && goodsid!=null){
		 $.ajax({
		        url: "/miaosha/"+path+"/miaoshaAction",
		        type: "POST",
		        data: {
		        	goodsid:goodsid
		        },
		        success: function(data) {
		            if(data=="10"){
		            	layer.msg("排队中.....");
		            	MiaoshaResule(goodsid);
		            }
		            if(data=="11"){
		            	layer.alert("不能重复秒杀",{icon:0});
		            	return false;
		            }
		            if(data=="12"){
		            	layer.alert("非法请求",{icon:0});
		            	return false;
		            }
		            if(data=="13"){
		            	layer.alert("商品已经没有库存了T_T!",{icon:0});
		            	return false;
		            }
		            
		        },
		        error: function(data) {
		            console.log(data);
		            layer.msg("秒杀请求失败");
		        }
		    });
	}
}
function MiaoshaResule(goodsid){
	$.ajax({
        url: "/miaosha/miaoshaResule",
        type: "get",
        data: {
        	goodsid:goodsid
        },
        success: function(data) {
            if(data=="0"){
            	setTimeout(function(){
            		MiaoshaResule(goodsid);
            	}, 250);
            }
            else if(data=="-1"){
            	layer.msg("很遗憾  秒杀已经结束了！");
            }
            else{
            	layer.confirm('秒杀成功跳转到订单页面？', {
            		  btn: ['确定','取消'] //按钮
            		}, function(){
            			window.location.href="/orderInfo/success?orderld="+data
            		}, function(){
            			reImg();
            		  layer.closeAll();
            		});
            }
        },
        error: function(data) {
            console.log(data);
            layer.msg("秒杀请求失败");
        }
    });
}
function checklist(){
	window.location.href="/togoodslist"
}