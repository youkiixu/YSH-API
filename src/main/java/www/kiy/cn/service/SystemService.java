package www.kiy.cn.service;

import java.util.List;

import www.kiy.cn.HotKey.eSqlType;
import www.kiy.cn.youki.JMap; 

public interface SystemService {

	List<JMap> getDataTableByMethod(String strName) throws Exception;

	List<JMap> getDataTableByMethod(String strName, JMap map) throws Exception;

	List<JMap> getDataTableByMethod(JMap config, String strName, JMap map) throws Exception;

	 List<List<JMap>> getDataSetByMethod(JMap config, String strMethod, JMap param) throws Exception;

	List<List<JMap>> getDataSetByMethod(String strName, JMap map) throws Exception;

	List<List<JMap>> getDataSetByMethod(String strName) throws Exception;
	List<?> getDataTableBySystemDao(final JMap config,String strName, final JMap map) throws Exception;

	List<JMap> QueryCacheListFromMap(String key, String uid, JMap config, String strMethod, JMap param)
			throws Exception;

	List<JMap> QueryCacheListFromMap(String key, JMap config, String strMethod, JMap param) throws Exception;

	List<JMap> QueryCacheListFromMap(String key, String strMethod, JMap param) throws Exception;

	List<JMap> getDBConfig(int DBId, String strAppid, int intSysProject, String strDBName, boolean bDefault)
			throws Exception;

	List<JMap> getDBConfig(String strAppid, int intSysProject, String strDBName, boolean bDefault) throws Exception;

	JMap getDBConfig(String strAppid, String strDBName, boolean bDefault) throws Exception;

	JMap getDBConfig(String strAppid, boolean bDefault) throws Exception;

	JMap getDBConfig(int DBId) throws Exception;

	List<?> getDataTableBySystemDao(JMap config, String strName, JMap map, eSqlType type) throws Exception;

	List<JMap> getDataTableByMethod(JMap config, String strName, JMap map, eSqlType type) throws Exception;

	List<JMap> getDataTableByMethod(String strName, JMap map, eSqlType type) throws Exception;
 

	JMap tbSave(JMap config, String returnID_tblName, JMap data, JMap relation) throws Exception;

	List<?> QuerySql(JMap config, String sql, JMap par) throws Exception;

	List<?> QuerySql(JMap config, String sql, JMap par, eSqlType type) throws Exception;

	List<?> QuerySql(JMap config, String sql, String sqlCount, boolean bPage, JMap par, eSqlType type) throws Exception;

	List<?> QuerySql(String strKey, String strServerName, String strDBName, String strUserID, String strPassword,
			String sql, String sqlCount, boolean bPage, JMap par, eSqlType type) throws Exception;
	
	
}
