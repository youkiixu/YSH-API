package www.kiy.cn.api;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import www.kiy.cn.service.TestService;
import www.kiy.cn.youki.CacheInfo;
import www.kiy.cn.youki.JMap;

@RestController
@RequestMapping("/tt")
public class TestController {

	@Autowired
	TestService testService;
	@Autowired
	CacheInfo cacheInfo;
	
	@RequestMapping("/test")
	 public String Index() {
		
		String d =cacheInfo.getString("a");  
		
		cacheInfo.putString("kk", "222");
		
		cacheInfo.getString("kk");
		
		JMap m = new JMap();
		m.put("1231", "111");
		m.put("a", 222);
		
		JMap m1 = new JMap();
	    m1.put("7", "77");
		
		m.put("m1", m1);
		
		cacheInfo.putJMap("m",m);
		cacheInfo.getMap("m");
		
		return "xxx";
	}

}
