package www.kiy.cn.youki;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tomcat.util.codec.binary.Base64;

public class Convert {

	public static String ToString(Object obj) {
		if(obj==null)
			return null;
		return String.valueOf(obj);
	}

	public static int ToInt32(Object obj) {
		if (obj == null)
			return 0; 
		return Integer.valueOf(obj.toString());
	}

	public static boolean ToBoolean(Object obj) {
		String str = String.valueOf(obj);
		if (str == null || str.equals("0"))
			return false;
		return Boolean.valueOf(str);
	}

	public static boolean isNullOrEmpty(String str) {
		if (str==null || str.isEmpty())
			return true;
		return false;
	}
	
	public static String ToMssqlJdbcUrl(String strServerName,String strDBName){
		return ToMssqlJdbcUrl(strServerName,"MSSQLSERVER",strDBName);  
	}
	public static String ToMssqlJdbcUrl(String strServerName,String strInstanceName ,String strDBName){
		return String.format("jdbc:sqlserver://%s;instanceName=%s;DatabaseName=%s;integratedSecurity=false", strServerName, strInstanceName, strDBName);
	}
	
	public static String  ToBase64String(byte [] data ){
		if(data==null)
			return null;
		return Base64.encodeBase64String(data);
	}
	
	public static byte[]  ToBase64Byte(String data ){
		if(data==null)
			return null;
		return Base64.decodeBase64(data);
	}
	
	public String getDateString() {

		return getDate(new Date(), "yyyy-MM-dd");
	}

	public String getFullDateString() {
		return getDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
	}

	public String getDate(Date curDate, String strFormat) {
		SimpleDateFormat format = new SimpleDateFormat(strFormat);

		return format.format(curDate);

	}

}
