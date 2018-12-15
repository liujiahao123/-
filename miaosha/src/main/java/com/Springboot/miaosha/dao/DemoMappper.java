package com.Springboot.miaosha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import com.Springboot.miaosha.entity.Demo;

@Mapper
public interface DemoMappper {
	
	//#{name}:参数占位符
	@Select("select * from Demo where name=#{name}")
	public List<Demo> likeName(@Param("name") String name);
	
	
	@Select("select * from Demo where id = #{id}")
	public Demo getById(long id);
	
	@Select("select name from Demo where id = #{id}")
	public String getNameById(long id);
	
	@Delete("delete from demo WHERE id=#{id}")
	public void delete(long id);

	
	/**
	 * 保存数据.
	 */
	@Insert("insert into Demo(name) values(#{name})")
	@Options(useGeneratedKeys=true,keyProperty="id",keyColumn="id")
	public void save(Demo demo);
	
}
