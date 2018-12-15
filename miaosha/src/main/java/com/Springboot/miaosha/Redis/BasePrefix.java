package com.Springboot.miaosha.Redis;

public abstract class BasePrefix implements KeyPrefix {

	private int expireSesonds;
	private String prefix;
	
	public BasePrefix(int expireSesonds, String prefix){
		this.expireSesonds=expireSesonds;
		this.prefix=prefix;
	}
	
	public BasePrefix(String prefix){
		this(0, prefix);
	}

	
	@Override
	public int expirSesonds() {
		// TODO Auto-generated method stub
		return expireSesonds;
	}

	@Override
	public String getPrefix() {
		String name = getClass().getSimpleName();//获取类名
		return name+":"+prefix;
	}

}
