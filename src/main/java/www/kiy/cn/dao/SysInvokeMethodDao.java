package www.kiy.cn.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import www.kiy.cn.youki.JMap;

@Mapper
public interface SysInvokeMethodDao {
	
	List<JMap> getEntityByName(String strName);
	
}
