package www.kiy.cn.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import www.kiy.cn.HotKey;
import www.kiy.cn.dao.SysInvokeMethodDao;
import www.kiy.cn.youki.CacheInfo;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.SetLog;

@Component
public class BaseRequest {

	@Autowired
	private CacheInfo cacheInfo;
	@Resource
	SysInvokeMethodDao sysInvokeMethod;
	
	

	/**
	 * 通过POST形式获取
	 * 
	 * @param req
	 * @return
	 */
	public JMap getRequestDataMap(HttpServletRequest request) {
		ServletInputStream stream = null;
		JMap map = null;
		try {

			stream = request.getInputStream();
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			byte[] b = new byte[1024];

			int count = -1;
			while ((count = stream.read(b, 0, 1024)) > 0)
				bStream.write(b, 0, count);
			b = null;
			String strJson = new String(bStream.toByteArray(), "utf-8");
			if (!strJson.isEmpty()) {   
				map = SetLog.ObjectToMap(strJson);
				JMap par = getRequestParameterMap(request);
				if (par.size() > 0) {
					map.putAll(par);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 通过GET形式获取
	 * 
	 * @param req
	 * @return
	 */
	public JMap getRequestParameterMap(HttpServletRequest req) {

		JMap map = new JMap();
		Enumeration<String> e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String str = e.nextElement();
			String paramValue = "";
			try {
				paramValue = URLDecoder.decode(req.getParameter(str), "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			if (str.equals("strSysJson")) {
				map.putAll(SetLog.ObjectToMap(paramValue));
			} else {
				map.put(str, paramValue);
			}

		}
		return map;
	}

	public JMap CheckSaaSLegal(JMap par) {  
		if (par == null || par.size() == 0) {
			SetLog.Info("BaseRequest", "空数据");
			return SetLog.writeMapError("空数据");
		}
		String appid = String.valueOf(par.get("appid"));

		String domain = String.valueOf(par.get("domain"));
		if (domain == null)
			return SetLog.writeMapError("域名参数丢失"); 
		JMap map = cacheInfo.getMap(HotKey.domainConfigMap);
		if (map == null) {
			map = new JMap(); 
		}
		JMap configInfo = map.getMap(appid!=null? appid:domain); 
		 
		if (configInfo == null) 
		{ 
			List<JMap> lst= sysInvokeMethod.SysAppConfigInfo(appid,domain);
			if(lst.size()==0)
				return SetLog.writeMapError("找不到对应配置信息");  
			configInfo = lst.get(0);
			map.put(appid!=null? appid:domain, configInfo);
			cacheInfo.putJMap(HotKey.domainConfigMap,  map);
		}
		
		
		return par;

	}

}
