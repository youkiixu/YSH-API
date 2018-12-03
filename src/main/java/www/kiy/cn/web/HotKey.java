package www.kiy.cn.web;

/**
 * 热词常量
 * 
 * @author Administrator
 * 
 * 
 *         1、数据缓存类型：JMap 必须m 开头,String 类型必须s开头,List<JMap> 数据必须以lst开头
 *
 *         2、数据库查询语句以:Q开头
 *
 *         3、枚举必须以e开头
 */

public class HotKey {
	
	
	
	public static final String QgetAdminUserInfo="getAdminUserInfo";
	public static final String QCRMMenuAuthor="CRMMenuAuthor";
	
	public static final String TempData="TempData";
	/**
	 * 用户登陆Session
	 */
	public static final String strUidSession = "uidSession";
	public static final String mUserSession = "mUserSession";
	
	public static final String mDomainConfig = "mDomainConfig";
	
	///页面
	public class Mapping{ 
		public static final String Index = "/Index"; 
		public static final String Login = "/Login";
		
	}
	
	public enum eSaaSAppKey {
		YSH00000001, // 印生活平台
		YSH00000002, // 彩印通平台1
		YSH00000003, // 印捷运输
		YSH00000004, // 印讯
		YSH00000005, // 印刷同城
		YSH00000006, // SaaS
		YSH00000007, // 印生活平台小程序 
	}

}
