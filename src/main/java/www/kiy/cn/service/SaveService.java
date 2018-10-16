package www.kiy.cn.service;

import java.util.List;

import www.kiy.cn.youki.JMap;

public interface SaveService {
	
	
	public JMap tbSave(JMap map);
	
	public JMap tbSave(List<JMap> lst);
	
	public JMap tbSaveList(JMap map);
	
	public JMap tbSaveList(List<JMap> lst);
	
	
}
