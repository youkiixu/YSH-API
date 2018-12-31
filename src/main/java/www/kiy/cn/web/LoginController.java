package www.kiy.cn.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import www.kiy.cn.models.MNotices;
import www.kiy.cn.models.MString;
import www.kiy.cn.models.MUserInfo;
import www.kiy.cn.service.SaaSService;
import www.kiy.cn.models.MDevelop; 
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap; 
import www.kiy.cn.youki.SetLog;
import www.kiy.cn.youki.YSHEncrypt;

@RestController
@RequestMapping(HotKey.Mapping.Login)
public class LoginController {
	@Autowired
	private SaaSService saas;
	@Autowired
	BaseRequest baseRequest;
	
	/**
	 * 开发页面登陆
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(MDevelop.Mapping.Login)
	public ModelAndView DevelopLogin(HttpServletRequest request, Model model) {
		HttpSession obj = request.getSession();
		Object u = obj.getAttribute(HotKey.strUidSession);
		if(baseRequest==null)
			baseRequest = new BaseRequest();
		baseRequest.setCookies(request);
		String strUserName = Convert.ToString(  baseRequest.getCookies(MUserInfo.strUserName));
		String strPassword =  Convert.ToString(  baseRequest.getCookies(MUserInfo.strPassword));  
		if (u == null) {
			JMap user = new JMap();
			user.put(MUserInfo.strUserName, strUserName);
			user.put(MUserInfo.strPassword, strPassword);
			model.addAttribute(MString.mDevelopUser, user);
			model.addAttribute(MString.strPageTitle, MNotices.Login.LoginTitle); 
			return new ModelAndView(MDevelop.Page.Login, HotKey.TempData, model);
		} else { 
			
			model.addAttribute(MString.strPageTitle, MNotices.Index.IndexTitle); 
			model.addAttribute(MString.mDevelopUser, u);
			
			return new ModelAndView(MDevelop.Page.Index,HotKey.TempData, model);
		}
	}
	
	// @RequestMapping(value="/DevelopLogin",method=RequestMethod.POST)
	@PostMapping(value = MDevelop.Mapping.Login)
	public JMap DevelopLogin(HttpServletRequest req, HttpServletResponse rep,
			@RequestParam(name = MUserInfo.strUserName, value = MUserInfo.strUserName, required = true) String strUserName,
			@RequestParam(name =MUserInfo.strPassword, value =MUserInfo.strPassword, required = true) String strPassword) throws Exception {

		System.out.println(strUserName);
		System.out.println(strPassword); 
		if (Convert.isNullOrEmpty(strUserName) || Convert.isNullOrEmpty(strPassword)) {
			return SetLog.writeMapError(MNotices.Login.errorPW);
		}

		Cookie u = new Cookie(MUserInfo.strPassword, strUserName);
		Cookie p = new Cookie(MUserInfo.strPassword, strPassword);
		// Sets the maximum age of the cookie in seconds
		// 30天清空cookie
		u.setMaxAge(1 * 60 * 60 * 24 * 30);
		p.setMaxAge(1 * 60 * 60 * 24 * 30);
		rep.addCookie(u);
		rep.addCookie(p);  
		
		JMap par= new JMap();
		par.put(MUserInfo.$strCode,strUserName); 
		List<JMap> list=  saas.getDataTableByMethod(HotKey.eSaaSAppKey.YSH00000006.name(),HotKey.QgetAdminUserInfo , par);
		if(list.size()==0)
			return SetLog.writeMapError(MNotices.Login.errorUser);
		JMap userInfo = list.get(0);
		String Password= Convert.ToString(userInfo.get(MUserInfo.Password));
		
		String Md5= YSHEncrypt.getEncryptInstance().EncoderByMd5(strPassword).toLowerCase();
		if(!Password.equals(Md5))
			return SetLog.writeMapError(MNotices.Login.errorPW); 
		HttpSession session = req.getSession();
		session.setAttribute(HotKey.strUidSession, strUserName); 
		session.setAttribute(HotKey.mUserSession, userInfo);   
		return SetLog.writeMapSuccess(MNotices.Login.successLogin);
	}
}
