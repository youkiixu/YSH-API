package www.kiy.cn.service.web.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import www.kiy.cn.models.MMenu;
import www.kiy.cn.models.MNotices;
import www.kiy.cn.service.SaaSService;
import www.kiy.cn.service.web.MenuAuthorService;
import www.kiy.cn.web.HotKey;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.SetLog;

public class MenuAuthorServiceImpl implements  MenuAuthorService{
	
	@Autowired
	SaaSService saas;  
	public JMap HomeMenuAuthor(Object json) throws Exception{
		JMap par= SetLog.ObjectToMap(json);
		Object obj= par.get(MMenu.idRole);
		if(obj==null){
			 par.put(MMenu.bShow, true);
		}
		List<JMap> list=  saas.getDataTableByMethod(HotKey.eSaaSAppKey.YSH00000006.name(),HotKey.QCRMMenuAuthor , par);
		if(list.size()==0)
			return SetLog.writeMapError(MNotices.Menu.NonAuthor);
		
		return par;
	}
	
}
