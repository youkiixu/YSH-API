package www.kiy.cn.api;
 
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import www.kiy.cn.service.SaaSService;
import www.kiy.cn.service.SystemService;
import www.kiy.cn.service.TestService;
import www.kiy.cn.youki.CacheInfo;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.Pub;
import www.kiy.cn.youki.SetLog;

@RestController
@RequestMapping("/tt")
public class TestController {
	@Autowired
	private SaaSService saas;
	
	
	
	@Autowired
	 private ApplicationContext applicationContext;
	
	@Resource(name="YSHSqlSessionTemplate")
	SqlSessionTemplate tmp;
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
			 
 		 try {
 			 JMap map = new JMap();
 			map.put("a", "123");
 			map.put("b", "123");
 			map.put("c", "123");
 			saas.tbSaaSSave("YSH00000006", "tblA", map);
 			
				 //	Object obj= Pub.getInstance().InvokeMethod(applicationContext,null,"www.kiy.cn.service.impl", "TestServiceImpl", "tbSaveTest", new JMap());
			// 	System.out.println(obj);
 				
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 					e.printStackTrace();
 			}
//			 
			   JMap map = new JMap();  
			 //  map.put("@IsHidden",0);
			 System.out.println("data="+saas.getDataSetByMethod("YSH00000007", "GetProductComments",map));
			   
		} catch (Exception e) {
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
