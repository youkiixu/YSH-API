package www.kiy.cn;

public class HotKey {
  /**
   * domain配置缓存
   */
  public static final	String  mDomainConfig="mDomainConfig";
  
  /**
   * 数据库配置缓存
   */
  public static final	String  mDBConfig="mDBConfig"; 
  
  /**
   * 实例缓存key
   */
  public static final String  mSysInvokeMethod= "mSysInvokeMethod";
  
  
  /**
   * 查询方法
   */
  public static final String QgetSysInvokeMethod ="getSysInvokeMethod";
  
  /**
   * 
   * 数据执行类型
   *
   */
  public static enum sqlType{
	  Jdbc,JdbcTemplate,mybatis
  }
  
  /**
   * 
   * 私钥加密、公钥解密的用途是数字签名。数字签名是用于防篡改和防止假冒的
   * 
   * 针对Java 请求C# 时，用公钥进行加密
   * 利用公钥、或私钥进行加解密
   *
   */
  public enum eRSAType{
		PrivateKey, publicKey
	}
}
