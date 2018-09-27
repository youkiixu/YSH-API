package www.kiy.cn.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import www.kiy.cn.youki.JMap;

@Mapper
public interface SysInvokeMethodDao {
	
	List<JMap> getEntityByName(@Param("strName") String strName);
	
	List<JMap> SysAppConfigInfo(@Param("appid")String appid , @Param("domain")String domain);
}
