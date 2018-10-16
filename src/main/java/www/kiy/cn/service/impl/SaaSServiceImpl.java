package www.kiy.cn.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import www.kiy.cn.HotKey;
import www.kiy.cn.HotKey.eSqlType;
import www.kiy.cn.dao.saas.SaaS;
import www.kiy.cn.service.SaaSService;
import www.kiy.cn.service.SystemService;
import www.kiy.cn.youki.CacheInfo;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap; 
import www.kiy.cn.youki.Pub;
import www.kiy.cn.youki.SetLog; 

@Service
public class SaaSServiceImpl implements SaaSService {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private CacheInfo cacheInfo;
	@Resource
	SaaS systemDao;
	@Autowired
	private SystemService systemService;

	public JMap getAppConfig(String appid) {
		return getAppConfig(appid, null);
	}

	public JMap getAppConfig(String strAppid, String strDomain) {

		JMap map = cacheInfo.getMap(HotKey.mDomainConfig);
		if (map == null) {
			map = new JMap();
		}

		JMap configInfo = map.getMap(strAppid != null ? strAppid : strDomain);

		//if (configInfo == null) 
		{
			JMap m = new JMap();
			m.put("appid", strAppid);
			m.put("domain", strDomain);

			List<JMap> lst = systemDao.SysAppConfigInfo(m);
			if (lst.size() == 0)
				return SetLog.writeMapError("找不到对应配置信息");
			configInfo = lst.get(0);
			map.put(strAppid != null ? strAppid : strDomain, configInfo);
			//cacheInfo.putJMap(HotKey.mDomainConfig, map);
		}
		return configInfo;
	}
	
	

	@Override
	public Object getSaaSData(String strAppid,JMap map) throws Exception {
		return getSaaSData( strAppid, map,eSqlType.Jdbc);
	}
	@Override
	public Object getSaaSData(String strAppid,JMap map,eSqlType type)  throws Exception {
			 
		// TODO Auto-generated method stub
		JMap param = null;

		if (map.get("strMethod") != null) {
			String strMethod = map.get("strMethod").toString();
			if (map.containsKey("param"))
				param = SetLog.ObjectToMap(map.get("param")); 
//			if (param != null && ((param.containsKey("@rowIndex") && param.containsKey("@pageRecord"))
//					|| (param.containsKey("$rowIndex") && param.containsKey("$pageRecord")))) {
//				obj = getDataSetByMethod(strAppid,strMethod,param);
//				
//			} else {
//				obj = getDataTableByMethod(strAppid,strMethod, param);
//			} 
			 Object  obj=systemService.getDataTableBySystemDao(this.getAppConfig(strAppid),strMethod, param,type);
			 
			
			if(obj.getClass().getName().equals("JMap")){
				return obj;
			}
			return SetLog.writeMapSuccess("Success", obj); 
		} else if (map.get("uid") != null || map.get("key") != null) {
			param = new JMap();
			JMap mp = new JMap();
			if (map.containsKey("param")) {

				mp = SetLog.ObjectToMap(map.get("param"));
				if (!mp.containsKey("strAppid"))
					mp.put("strAppid", strAppid);
				// param.put("intSysProject", intSysProject);
			} else {
				mp.put("strAppid", strAppid);
			}
			param.put("param", mp);

			String uid = Convert.ToString(map.get("uid"));
			String key = Convert.ToString(map.get("key"));
			String cKey = uid == null ? String.format("%s%s", strAppid, key) : uid;  
			JMap dt = SetLog.ObjectToMap(HotKey.mSysInvokeMethod);
			if (dt == null) {
				dt = new JMap();
			}
			JMap InfoMethod;
			if (dt.containsKey(cKey)) {
				InfoMethod = SetLog.ObjectToMap(dt.get(cKey)); // xxxxxx
			} else {
				List<JMap> lst = systemService.getDataTableByMethod(HotKey.QgetSysInvokeMethod, map,type);

				if (lst.size() == 0)
					return SetLog.writeMapError("方法主键不存在");

				InfoMethod = lst.get(0);
				dt.put(cKey, InfoMethod);
				cacheInfo.putJMap(HotKey.mSysInvokeMethod, dt);
			}

			Object oo = Pub.getInstance().InvokeMethod(applicationContext,
					Convert.ToString(InfoMethod.get("strFilePath")), InfoMethod.get("strPackage").toString(),
					InfoMethod.get("strClassName").toString(), InfoMethod.get("strMethod").toString(), param);
			return oo;
		}
		return null;
	}

	
	@Override
	public List<List<JMap>> getDataSetByMethod(String strAppid,String strMethod, JMap param) throws Exception {
		// TODO Auto-generated method stub
		JMap config = systemService.getDBConfig(strAppid, true);
		List<List<JMap>> lst= systemService.getDataSetByMethod(config, strMethod, param); 
		return lst;
	}

	@Override
	public List<JMap> getDataTableByMethod(String strAppid,String strMethod, JMap param) throws Exception {
		// TODO Auto-generated method stub
		JMap config = this.getAppConfig(strAppid); 
		List<JMap> lst= systemService.getDataTableByMethod(config, strMethod, param);
		
		return lst;
	}
	
}
