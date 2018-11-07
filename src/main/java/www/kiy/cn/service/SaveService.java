package www.kiy.cn.service;

import java.util.List;
import java.util.Map;

import www.kiy.cn.HotKey.eSqlType;
import www.kiy.cn.youki.JMap;

public interface SaveService {
	
	
	
	/**
	 * 
	 * @param data {表明}
	 * @return
	 */
	public JMap tbSave(JMap data);
	public JMap tbSave(JMap config,JMap data);
	
	public JMap tbSave(JMap config,JMap data,eSqlType type);
	//public JMap tbSave(String tblName,List<JMap> lst); 
	Map<JMap, Map<String, Map<String, List<JMap>>>>  tbSaaSSaveByJdbc(JMap config, Map<String, String[]> dicRelation, Map<String, List<JMap>> data) throws Exception;
	
 
	
}
