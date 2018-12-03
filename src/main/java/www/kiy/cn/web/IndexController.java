package www.kiy.cn.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import www.kiy.cn.models.MDevelop;
import www.kiy.cn.models.MNotices;
import www.kiy.cn.models.MString;
import www.kiy.cn.service.SaaSService;
import www.kiy.cn.youki.JMap;

@RestController
@RequestMapping(HotKey.Mapping.Index)
public class IndexController {
	@Autowired
	BaseRequest baseRequest;
	@Autowired
	SaaSService saas; 
	@GetMapping(MDevelop.Mapping.Index)
	public ModelAndView DevelopIndex(HttpServletRequest req, Model model) throws Exception {
		HttpSession session = req.getSession();
		Object uid = session.getAttribute(HotKey.strUidSession);
		if (uid == null) {   
			
			return new LoginController().DevelopLogin(req, model);
		}
		//加载权限菜单 
		initAuthorMenu(uid.toString()); 
		
		model.addAttribute(MString.strPageTitle, MNotices.Index.IndexTitle);
		model.addAttribute(MString.mDevelopUser, session.getAttribute(HotKey.strUidSession));
		return new ModelAndView(MDevelop.Page.Index, HotKey.TempData, model);
	} 
	
	private JMap  initAuthorMenu(String userId) throws Exception{ 
		JMap par = new JMap();
		par.put("bShow", true);
		par.put("userId", userId);
		List<JMap> lst=saas.getDataTableByMethod(HotKey.eSaaSAppKey.YSH00000006.name(), "CRMMenuAuthor",  par);
		
		return null;
	}
}
