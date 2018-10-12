package www.kiy.cn.service;

import java.util.List;

import www.kiy.cn.youki.JMap; 

public interface SaaSService {
	public JMap getAppConfig(String appid);

	public JMap getAppConfig(String appid, String domain);

	public Object getSaaSData(String strAppid,JMap m) throws Exception;
	

	public List<List<JMap>> getDataSetByMethod(String strAppid,String strMethod, JMap param) throws Exception;

	public List<JMap> getDataTableByMethod(String strAppid,String strMethod, JMap param) throws Exception;

	Object getSaaSData(String strAppid, JMap map, String type) throws Exception;

}
