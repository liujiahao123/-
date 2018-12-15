package com.Springboot.miaosha.result;

/**
 * @author 20160709
 *
 */
public class CodeMes {
    private int code;
    private String mes;
    
    
    public static CodeMes SUCCESSCodeMes = new CodeMes(0,"SUCCESS");
    public static CodeMes SERVICE_ERROR = new CodeMes(110,"服务器超时");
    public static CodeMes MOBILE_EXISTS = new CodeMes(111,"手机号不存在");
    public static CodeMes MOBILE_ERROR = new CodeMes(112,"手机号格式不正确");
    public static CodeMes PASS_ERROR = new CodeMes(113,"密码不正确");
    public static CodeMes MOBILE_NULL = new CodeMes(114,"手机号为空");
    public static CodeMes PASS_NULL = new CodeMes(115,"密码不能为空");
    public static CodeMes USER_NULL = new CodeMes(116,"用户不存在");
    
    
    
    
    
	public CodeMes(int code, String mes) {
		this.code=code;
		this.mes=mes;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	
}
