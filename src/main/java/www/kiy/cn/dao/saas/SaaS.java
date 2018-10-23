package www.kiy.cn.dao.saas;

import java.util.List;
 
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider; 

import www.kiy.cn.youki.JMap; 

@Mapper
public interface SaaS { 
	
	 List<List<JMap>> sysQueryAndFliter(@Param("strName") String strName);

	 @SelectProvider(type=SqlProviderDao.class,method="getDataTableBySystemDao")
	 @ResultMap({ "dynamic", "dynamic2" })
	 List<List<JMap>> getDataByMethod(JMap map );
	 @SelectProvider(type=SqlProviderDao.class,method="getDataTableBySystemDao")
	 List<JMap> getDataTableByMethod(JMap map);
	  
//	@Select(" \r\n select * from SysQueryMethod with(nolock) where strName= #{strName} \r\n select  f.* from SysSqlFliter f with(nolock) inner join  SysQueryMethod  s with(nolock)  on s.uid=f.uidVouch where s.strName=#{strName} and bNoUsed=0  ")
//	// @ResultType(JMap.class)
//	@ResultMap({ "SysQueryMethod_M", "SysSqlFliter_M" })
//	 List<List<JMap>> getSystemDataSetByName(@Param("strName") String strName);
	 
	 List<JMap> getEntityByName(@Param("strName") String strName);

	// List<JMap> SysAppConfigInfo(@Param("appid")String appid , @Param("domain")String domain);
	 
	 List<JMap> SysAppConfigInfo(JMap map);
	 
	 
	 @InsertProvider(type= SqlProviderDao.class, method="getDataTableBySystemDao")
	 @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "Id") 
	 int tbSave(JMap map);

}
