package www.kiy.cn;

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
	/*******************************************缓存  start********************************************************/
	/**
	 * domain配置缓存
	 */
	public static final String mDomainConfig = "mDomainConfig";
	
	public static final String mSysQueryMethodConfig= "mSysQueryMethodConfig";

	/**
	 * 数据库配置缓存
	 */
	 public static final String lstDBConfig = "lstDBConfig";

	/**
	 * 实例缓存key
	 */
	public static final String mSysInvokeMethod = "mSysInvokeMethod";
	
	/*******************************************缓存  start********************************************************/

	
	/*******************************************查询方法  start********************************************************/
	/**
	 * 查询方法
	 */
	public static final String QgetSysInvokeMethod = "getSysInvokeMethod";

	public static final String QSysDBConfigInfo = "SysDBConfigInfo";
	
	//public static final String 

	/**
	 * 
	 * 数据执行类型
	 *
	 */
	public static enum eSqlType {
		Jdbc, JdbcTemplate, mybatis
	}

	/**
	 * 
	 * 私钥加密、公钥解密的用途是数字签名。数字签名是用于防篡改和防止假冒的
	 * 
	 * 针对Java 请求C# 时，用公钥进行加密 利用公钥、或私钥进行加解密
	 *
	 */
	public enum eRSAType {
		privateKey, publicKey
	}
}
