package www.kiy.cn.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import www.kiy.cn.HotKey;
import www.kiy.cn.service.SaaSService;
import www.kiy.cn.youki.CacheInfo;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.HttpRequestHelp;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.Pub;
import www.kiy.cn.youki.RSA;
import www.kiy.cn.youki.SetLog; 
@RestController
@RequestMapping("/YSH")
public class YSHController {
	
	@Autowired
	private SaaSService saas;

	@Autowired
	private CacheInfo cacheInfo;
	private final RSA rsa = new RSA();
	@Autowired
	private BaseRequest baseRequest;

	String strPrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKRZyzdQQ/Z8OxILFsJcDIvlkwG4ftRRwasW1ODRjcYz8AC987kyZF0Vqh/CxdbfnYVg2kwwA/8Styoxu3ZtZ+dyuw4PQl/liP/tSWONAD9lvdQ+yU87Tciwm/TLUHIAERbdVgCJr6PTaHAG17FrwhCZ3vtU+yJrT5QYU7ku7utzAgEDAoGAG2RMiTgLU79fLayDyw9XbKZDKvQVI2L1nIPOJXhCS7NSqspTSYhmD4OcWqB2TnqaQOV5t11V/9hz3F2fPmeRUPlqWFKhHFlyGsiMMqWcWgbLwsU+royhTiPJ7jHtwVMALKE/Zyl5bmauX4fuGaRGyz57mpcrr/1PujlAtvcxYNMCQQDNWwVUM+mgKjTLMVgi9bwAUyO5opXMd9RtscclSAVAVSVBKDuVoEwDahIrwUXNNqvLo2mdwPF/V+4epmHEBfmZAkEAzOH2y0fMLLIqbs7biGBnPKQf5a6iIw4fbC6YenXkzbrl1LFZ+zbBZ+ggq4HRxOKhV1XyLDQQ/BlIoikNn8Cs6wJBAIjnWOLNRmrGzdzLkBdOfVWMwnvBuTL6jZ52hMOFWNWOGNYa0mPAMqzxYXKA2TN5x90Xm76AoP+P9BRu69gD+7sCQQCIlqSHhTLIdsb0ieewQETTGBVDycFstBTyybr8TpiJJ0PjIOanedZFRWsdATaDQcDk4/bIIrX9ZjBsG15qgHNHAkAjk8Y1kCNRjixxKwPJzh8xaWMsAQQvmGzEKGrw7m8xLkxDLMlWs1dkG3NIhFW1cdJHaZMURkbwdtkVPsY6hnh8";
	String publicKey = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQCkWcs3UEP2fDsSCxbCXAyL5ZMBuH7UUcGrFtTg0Y3GM/AAvfO5MmRdFaofwsXW352FYNpMMAP/ErcqMbt2bWfncrsOD0Jf5Yj/7UljjQA/Zb3UPslPO03IsJv0y1ByABEW3VYAia+j02hwBtexa8IQmd77VPsia0+UGFO5Lu7rcwIBAw==";

	@RequestMapping("/Query")
	public Object Query(HttpServletRequest req, HttpServletResponse rep) {
		
		try{
		JMap par = baseRequest.getRequestDataMap(req);

		if (par == null || par.size() == 0) {
			SetLog.Info("BaseRequest", "空数据");
			return SetLog.writeMapError("空数据");
		}
		String appid =Convert.ToString(par.get("appid"));

		String domain = Convert.ToString(par.get("domain"));
		if (domain == null)
			return SetLog.writeMapError("域名参数丢失");

		// req.getRemoteAddr(); 影响整体性能禁用
		String strSysMac = par.containsKey("strSysMac") ? par.get("strSysMac").toString() : req.getRemoteAddr();
		// 测试使用
		//String sign1 = rsa.signData(SetLog.GetJSONString(par), strPrivateKey);
		//par.put("sign", sign1);

		JMap configInfo = saas.getAppConfig(appid, domain);
		if (configInfo.containsKey("errMsg"))
			return configInfo;
		if (!String.valueOf(configInfo.get("strMac")).equals(strSysMac))
			return SetLog.writeMapError("无效Mac地址");
		String strDomain = String.valueOf(configInfo.get("strDomain"));// yj.kiy.cn
		String clientToken =Convert.ToString(configInfo.get("clientToken"));

		if (!strDomain.equals(domain))
			return SetLog.writeMapError("无效域名");
		// 通过域名下的文件获取strText
		if (clientToken == null && !Convert.ToBoolean(configInfo.get("bDebug"))) {
			String strScheme = String.valueOf(configInfo.get("strScheme"));
			String strTxt = String.valueOf(configInfo.get("strTxt"));
			String path = String.format("%s://%s/%s",strScheme, strDomain, strTxt);// .txt应放在主站下
			if (Convert.isNullOrEmpty(strScheme)) {
				strScheme = "http";
				clientToken = HttpRequestHelp.doGetRequest(path, null);
				if (!clientToken.equals(strTxt)) {
					strScheme = "https";
					// .txt应放在主站下
					clientToken = HttpRequestHelp.doGetRequest(String.format("%s://%s/%s",strScheme, strDomain, strTxt), null);
					if (!clientToken.equalsIgnoreCase(rsa.EncoderByMd5(Convert.ToString( configInfo.get("strPublicKey")))))
						return SetLog.writeMapError("服务器URL请求无效");
				}
			}

			configInfo.put("clientToken", clientToken);

			cacheInfo.putJMap(HotKey.mDomainConfig, configInfo);

		}
		String sign = par.getWithRemoveKey("sign").toString();
		String json= SetLog.GetJSONString(par);
		System.out.println(json);
		boolean bol = rsa.verifyData(json, sign, configInfo.get("strPublicKey").toString());

		if (!bol)
			return SetLog.writeMapError("Sign 无效");

		// strSysMethod 代表执行方法名
		String strSysMethod = Convert.ToString(par.getWithRemoveKey("strSysMethod"));
		// uid 代表InvokeMethod中的
		String uid = Convert.ToString(par.getWithRemoveKey("uid"));

		// 主键 key 代表InvokeMethod 中的strName
		String key = Convert.ToString(par.getWithRemoveKey("key"));

		if (strSysMethod == null && uid == null && key == null)
			return par;

		JMap m = new JMap();
		if (uid != null)
			m.put("uid", uid);
		else if (key != null)
			m.put("key", key);
		else
			m.put("strMethod", strSysMethod);
		m.put("param", par);
		
		Object obj;
		try {
			obj = saas.getSaaSData(Convert.ToString(configInfo.get("strAppid")),m);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			obj = SetLog.writeMapError("内部错误",e.toString());
		}
		
		if (obj.getClass().getName() == "JMap") {
			String strPrivateKey = Convert.ToString(configInfo.get("strPrivateKey")); 
			((JMap) obj).put("sign", rsa.signData(SetLog.GetJSONString(obj), strPrivateKey));
		}
		return obj;
		}catch(Exception ex){
			ex.printStackTrace();
			//return SetLog.writeMapError("内部运行错误",);
			return SetLog.writeMapError("找不到对应类型信息",  Pub.getInstance().getStackTrace(ex.fillInStackTrace()));
		}
	}

}
