package www.kiy.cn.service.web.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import www.kiy.cn.models.MMenu;
import www.kiy.cn.models.MNotices;
import www.kiy.cn.service.SaaSService;
import www.kiy.cn.service.web.MenuAuthorService;
import www.kiy.cn.web.HotKey;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.ListMap;
import www.kiy.cn.youki.SetLog;
@Service
public class MenuAuthorServiceImpl implements MenuAuthorService {
	
	private int index=0;
	@Autowired
	SaaSService saas;
	@Override
	public JMap HomeMenuAuthor(Object json) throws Exception {
		//{Account=YJAdmin, Email=, Id=1, Mobile=13412341234, NickName=admin, Password=f7756580a585c30e89aad755dbe617d1, RealName=admin, RoleId=1, RoleName=超级管理员}
		
		JMap par = SetLog.ObjectToMap(json); 
		JMap p = new JMap();
		int role =  Convert.ToInt32(par.get(MMenu.RoleId));
		p.put(MMenu.$idRole,role );
		p.put(MMenu.$userId,  par.get(MMenu.Id)); 
		if (role != 1) {
			par.put(MMenu.bShow, true);
		}
		//List<JMap> list = saas.getDataTableByMethod(HotKey.eSaaSAppKey.YSH00000006.name(), HotKey.QCRMMenuAuthor, par);
		StringBuilder uid = new StringBuilder();
		Iterator<String> it= par.keySet().iterator();
		while(it.hasNext()){
			String ele= it.next();
			uid.append(par.get(ele));
		}
		List<JMap> list  = saas.getDataTableByMethodByCache(uid.toString(), HotKey.eSaaSAppKey.YSH00000006.name(), HotKey.QCRMMenuAuthor, p);
		if (list.size() == 0)
			return SetLog.writeMapError(MNotices.Menu.NonAuthor);
		String strPreParentCode = Convert.ToString(list.get(0).get(MMenu.strParentCode));
		index=0;
		ListMap menu = getMapRecursion(list,  strPreParentCode);
		JMap m = SetLog.writeMapSuccess(null, menu);
		return m;
	} 
	@Override
	public ListMap getMapRecursion(List<JMap> dt,  String strPreParentCode) {
		ListMap lst = new ListMap();
		String preCode = "";
		JMap preMap = null;
		for (; index < dt.size(); index++) {
			JMap curRow = dt.get(index);
			String strCode = curRow.get(MMenu.strCode).toString();
			String strParentCode = curRow.get(MMenu.strParentCode).toString();
			if (strParentCode.equals(strPreParentCode)) {
				String strUrl =Convert.ToString( curRow.get(MMenu.strUrl));
				JMap m = new JMap();
				m.put(MMenu.id, strCode);
				m.put(MMenu.text, curRow.get(MMenu.strText));
				m.put(MMenu.strParent, strParentCode);
				if (!Convert.isNullOrEmpty(strUrl)) {
					m.put(MMenu.strUrl, strUrl);
				}
				lst.add(m);
				preCode = strCode;
				preMap = m;
			} else {
				if (preCode.equals(strParentCode)) {
					ListMap children = getMapRecursion(dt, preCode);
					preMap.put(MMenu.state, MMenu.closed);
					preMap.put(MMenu.children, children);
				} else {
					index--;
					return lst;
				}
			}
		} 
		return lst;
	}
}
