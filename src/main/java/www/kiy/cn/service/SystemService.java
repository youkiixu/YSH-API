package www.kiy.cn.service;

import java.util.List;

import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.YSHException;

public interface SystemService {

	 List<JMap> getDataTableByMethod(String strName) throws YSHException;

	 List<JMap> getDataTableByMethod(String strName, JMap map) throws YSHException;

	 List<JMap> getDataTableByMethod(JMap config, String strName, JMap map) throws YSHException;

	 List<List<JMap>> getDataSetByMethod(JMap config, String strMethod, JMap param) throws YSHException;

	List<List<JMap>> getDataSetByMethod(String strName, JMap map) throws YSHException;

	List<List<JMap>> getDataSetByMethod(String strName) throws YSHException;
	List<?> getDataTableBySystemDao(final JMap config,String strName, final JMap map) throws YSHException;
	
}
