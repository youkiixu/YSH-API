package www.kiy.cn.service;

import java.util.List;

import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.YSHException;

public interface SaaSService {
	public JMap getAppConfig(String appid);

	public JMap getAppConfig(String appid, String domain);

	public Object getSaaSData(String strAppid,JMap m) throws YSHException;
	

	public List<List<JMap>> getDataSetByMethod(String strAppid,String strMethod, JMap param) throws YSHException;

	public List<JMap> getDataTableByMethod(String strAppid,String strMethod, JMap param) throws YSHException;

}
