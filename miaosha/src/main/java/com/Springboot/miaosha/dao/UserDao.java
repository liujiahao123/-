package com.Springboot.miaosha.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.Springboot.miaosha.entity.MiaoShaUser;


@Mapper
public interface UserDao {
	
	@Select("select * from miaosha_user where id = #{id}")
	public MiaoShaUser getById(long id);
	
	
	
	
}
