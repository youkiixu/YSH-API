package www.kiy.cn.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import www.kiy.cn.service.TestService;

@RestController
@RequestMapping("/tt")
public class TestController {
	
	@Autowired
	private  TestService testService; 
	
	@RequestMapping("/test")
	public String Index(){
		
		testService.Test();
		return "xxx";
	}
}
