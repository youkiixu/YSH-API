package www.kiy.cn.youki;

public class Convert {

	public static String ToString(Object obj) {
		return String.valueOf(obj);
	}

	public static int ToInt32(Object obj) {
		if (obj == null)
			return 0;
		
		return Integer.valueOf(obj.toString());
	}

	public static boolean ToBoolean(Object obj) {
		String str = String.valueOf(obj);
		if (str == null)
			return false;
		return Boolean.valueOf(str);
	}

	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.isEmpty())
			return true;
		return false;
	}
	
	public static String ToMssqlJdbcUrl(String strServerName,String strDBName){
		return ToJdbcUrl(strServerName,"MSSQLSERVER",strDBName);  
	}
	public static String ToJdbcUrl(String strServerName,String strInstanceName ,String strDBName){
		return String.format("jdbc:sqlserver://%s;instanceName=%s;DatabaseName=%s;integratedSecurity=false", strServerName, strInstanceName, strDBName);
	}

}
