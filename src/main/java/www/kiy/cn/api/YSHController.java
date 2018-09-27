package www.kiy.cn.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.SetLog;

@RestController
@RequestMapping("/YSH")
public class YSHController {
	
	@Autowired
	private BaseRequest baseRequest;
	
	@RequestMapping("/Query")
	public JMap Query(HttpServletRequest req, HttpServletResponse rep){
		JMap par= baseRequest.getRequestDataMap(req); 
		baseRequest.CheckSaaSLegal(par);
		
		SetLog.Info("AA", SetLog.GetJSONString(par));
		return null;
	}
	
	
	
	
	
	
	
}
