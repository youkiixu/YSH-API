package www.kiy.cn.web;

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
import www.kiy.cn.service.web.MenuAuthorService;
import www.kiy.cn.youki.JMap;

@RestController
@RequestMapping(HotKey.Mapping.Index)
public class IndexController {
	@Autowired
	BaseRequest baseRequest; 
	@Autowired
	MenuAuthorService  menuAuthor;
	@GetMapping(MDevelop.Mapping.Index)
	public ModelAndView DevelopIndex(HttpServletRequest req, Model model) throws Exception {
		HttpSession session = req.getSession();
		Object mUser = session.getAttribute(HotKey.mUserSession);
		if (mUser == null) {   
			
			return new LoginController().DevelopLogin(req, model);
		}
		//JMap data= baseRequest.getRequestDataMap(req);
		 
		//加载权限菜单 
		JMap map= menuAuthor.HomeMenuAuthor(mUser); 
		model.addAttribute(MString.data, map);
		model.addAttribute(MString.strPageTitle, MNotices.Index.IndexTitle);
		model.addAttribute(MString.mDevelopUser, session.getAttribute(HotKey.strUidSession));
		return new ModelAndView(MDevelop.Page.Index, HotKey.TempData, model);
	} 
	
	
	
}
