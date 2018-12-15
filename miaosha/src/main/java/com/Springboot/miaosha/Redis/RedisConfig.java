package com.Springboot.miaosha.Redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix="redis")//读取application。properties的redis信息
public class RedisConfig {
   private String host;
   private int port;
   private int maxactive;
   private int maxwait;
   private int maxidle;
   private int minidle;
   private int timeout;
   public String getHost() {
	return host;
}
public void setHost(String host) {
	this.host = host;
}
public int getPort() {
	return port;
}
public void setPort(int port) {
	this.port = port;
}
public int getMaxactive() {
	return maxactive;
}
public void setMaxactive(int maxactive) {
	this.maxactive = maxactive;
}
public int getMaxwait() {
	return maxwait;
}
public void setMaxwait(int maxwait) {
	this.maxwait = maxwait;
}
public int getMaxidle() {
	return maxidle;
}
public void setMaxidle(int maxidle) {
	this.maxidle = maxidle;
}
public int getMinidle() {
	return minidle;
}
public void setMinidle(int minidle) {
	this.minidle = minidle;
}
public int getTimeout() {
	return timeout;
}
public void setTimeout(int timeout) {
	this.timeout = timeout;
}

   
}
