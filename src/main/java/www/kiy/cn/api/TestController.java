package www.kiy.cn.api;
 
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import www.kiy.cn.service.SaaSService;
import www.kiy.cn.service.SystemService;
import www.kiy.cn.service.TestService;
import www.kiy.cn.youki.CacheInfo;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.SetLog;
import www.kiy.cn.youki.YSHException;

@RestController
@RequestMapping("/tt")
public class TestController {
	@Autowired
	private SaaSService saas;
	
	@Autowired
	TestService testService;
	@Autowired
	CacheInfo cacheInfo;
	public static final Logger log = LoggerFactory.getLogger(TestController.class);
	@RequestMapping("/test")
	 public String Index() {
		log.info("msg");
		SetLog.logInfo("testAAAAAAAAAA");  
		 try {
			//systemService.getDataTableByMethod("GetLogin");
			 JMap map = new JMap();
			 
			 saas.getDataSetByMethod("YSH00000007", "GetLogin",map );
		} catch (YSHException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		 cacheInfo.getString("a");  
//		 
//		 
//		cacheInfo.putString("kk", "222");
//		
//		cacheInfo.getString("kk");
//		
//		JMap m = new JMap();
//		m.put("1231", "111");
//		m.put("a", 222);
//		
//		JMap m1 = new JMap();
//	    m1.put("7", "77");
//		
//		m.put("m1", m1);
//		
//		cacheInfo.putJMap("m",m);
//		cacheInfo.getMap("m"); 
		
		return "xxx";
	}
	
	
	

}
