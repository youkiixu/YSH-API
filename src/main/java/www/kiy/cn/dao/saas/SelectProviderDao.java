package www.kiy.cn.dao.saas;

import www.kiy.cn.youki.JMap;

public class SelectProviderDao {
	
	public String getDataTableBySystemDao (  final JMap map){
        
        String strSql = map.getWithRemoveKey("strSysSqlKey").toString();
        return strSql;
    }
}
