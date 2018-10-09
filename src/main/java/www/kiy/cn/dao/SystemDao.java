package www.kiy.cn.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import www.kiy.cn.youki.JMap;

@Mapper
public interface SystemDao { 
	
	 List<List<JMap>> sysQueryAndFliter(@Param("strName") String strName);

	 @SelectProvider(type=SelectProviderDao.class,method="getDataTableBySystemDao")
	 List<?> getDataByMethod(JMap map );
	 
	 @SelectProvider(type=SelectProviderDao.class,method="getDataTableBySystemDao")
	 List<JMap> getDataSetByMethod(@Param("strName") String strName);
	 
	 
//	@Select(" \r\n select * from SysQueryMethod with(nolock) where strName= #{strName} \r\n select  f.* from SysSqlFliter f with(nolock) inner join  SysQueryMethod  s with(nolock)  on s.uid=f.uidVouch where s.strName=#{strName} and bNoUsed=0  ")
//	// @ResultType(JMap.class)
//	@ResultMap({ "SysQueryMethod_M", "SysSqlFliter_M" })
//	 List<List<JMap>> getSystemDataSetByName(@Param("strName") String strName);
	 
	List<JMap> getEntityByName(@Param("strName") String strName);

	// List<JMap> SysAppConfigInfo(@Param("appid")String appid , @Param("domain")String domain);

	List<JMap> SysAppConfigInfo(JMap map);

}
