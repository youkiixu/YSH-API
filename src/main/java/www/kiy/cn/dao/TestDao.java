package www.kiy.cn.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import www.kiy.cn.youki.JMap;

@Mapper
public interface TestDao {

	// 注意 mybatis 数据库表中外键关系存在时 useGeneratedKeys="true" keyProperty="id"
	// 不能使用，需要删除表外键关系
	/*@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "Id")
	@Insert("insert into tblA values(#{a})")
	int tbSaveTest(JMap map);*/
	 
	int insertTableA(JMap map); 
	
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "Id")
	@Insert("insert into tblB values(#{a},#{idA})")//参数
	int tbSaveTestB(JMap map);
}
