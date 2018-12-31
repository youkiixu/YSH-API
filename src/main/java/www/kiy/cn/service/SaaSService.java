package www.kiy.cn.service;

import java.util.List;
import java.util.Map;

import www.kiy.cn.HotKey.eSqlType;
import www.kiy.cn.youki.JMap; 

public interface SaaSService {
	public JMap getAppConfig(String appid);

	public JMap getAppConfig(String appid, String domain);

	public Object getSaaSData(String strAppid,JMap m) throws Exception;
	

	public List<List<JMap>> getDataSetByMethod(String strAppid,String strMethod, JMap param) throws Exception;

	public List<JMap> getDataTableByMethod(String strAppid,String strMethod, JMap param) throws Exception;

	Object getSaaSData(String strAppid, JMap map, eSqlType type) throws Exception;

	JMap tbSaaSSave(String strAppid, String tblName, JMap data) throws Exception;

	JMap tbSaaSSave(String strAppid, String tblName, List<JMap> data) throws Exception;

	JMap tbSaaSSaveForList(String strAppid, JMap data) throws Exception;

	JMap tbSaaSSaveForList(String strAppid, JMap data, eSqlType type) throws Exception;

	JMap tbSaaSSave(String strAppid, String returnID_tblName, JMap data, String tblHeadName, String tblColumns)
			throws Exception;

	JMap tbSaaSSave(String strAppid, String strDBName, String returnID_tblName, JMap data, JMap relation)
			throws Exception;

	JMap tbSaaSSave(JMap config, String returnID_tblName, JMap data, JMap relation, eSqlType type) throws Exception;
 
	List<JMap> getDataTableByMethodByCache(String uid, String key, String strAppid, String strMethod, JMap param)
			throws Exception;

	List<JMap> getDataTableByMethodByCache(String uid, String strAppid, String strMethod, JMap param) throws Exception;

	 

}
