package www.kiy.cn.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import www.kiy.cn.youki.JMap;

@RestController
@RequestMapping("/YSH")
public class YSHController {
	
	@RequestMapping("/Query")
	public JMap Query(HttpServletRequest request, HttpServletResponse response){
		
		return null;
	}
	
	
}
