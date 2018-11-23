package www.kiy.cn.web;

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

import www.kiy.cn.service.SaaSService;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap; 
import www.kiy.cn.youki.SetLog;

@RestController
@RequestMapping("/Login")
public class LoginController {
	@Autowired
	private SaaSService saas;
	/**
	 * 开发页面登陆
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/DevelopLogin")
	public ModelAndView DevelopLogin(HttpServletRequest request, Model model) {
		HttpSession obj = request.getSession();
		Object u = obj.getAttribute("u");
		Cookie[] cook = request.getCookies();

		if (u == null) {
			JMap user = new JMap();
			String strUserName = null;
			String strPassword = null;
			if (cook.length > 0) {
				strUserName = cook[0].getValue();
				strPassword = cook[1].getValue();
			}

			user.put("strUserName", strUserName);
			user.put("strPassword", strPassword);
			model.addAttribute("user", user);
			model.addAttribute("Title", "SaaS 开发登陆界面");
			return new ModelAndView("Develop/Login", "TempData", model);
		} else {

			System.out.println(cook[0].getValue());
			System.out.println(cook[1].getValue());
			model.addAttribute("Title", "SaaS 首页");
			System.out.println(u.toString());
			model.addAttribute("userInfo", u);
			return new ModelAndView("Develop/Index", "TempData", model);
		}
	}
	
	// @RequestMapping(value="/DevelopLogin",method=RequestMethod.POST)
	@PostMapping(value = "/DevelopLogin")
	public JMap DevelopLogin(HttpServletRequest req, HttpServletResponse rep,
			@RequestParam(name = "strUserName", value = "strUserName", required = true) String strUserName,
			@RequestParam(name = "strPassword", value = "strPassword", required = true) String strPassword) {

		System.out.println(strUserName);
		System.out.println(strPassword);

		if (Convert.isNullOrEmpty(strUserName) || Convert.isNullOrEmpty(strPassword)) {
			return SetLog.writeMapError("用户名或密码不正确");
		}

		Cookie u = new Cookie("strUserName", strUserName);
		Cookie p = new Cookie("strPassword", strPassword);
		// Sets the maximum age of the cookie in seconds
		// 30天清空cookie
		u.setMaxAge(1 * 60 * 60 * 24 * 30);
		p.setMaxAge(1 * 60 * 60 * 24 * 30);
		rep.addCookie(u);
		rep.addCookie(p);
		HttpSession session = req.getSession();
		session.setAttribute("u", strUserName);
		JMap par= new JMap();
		par.put("@strUserName",strUserName);
		//saas.getDataTableByMethod("", strMethod, param);
		// new
		// ModelAndView("Develop/Index");
		return SetLog.writeMapSuccess("登陆成功");
	}
}
