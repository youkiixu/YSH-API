package www.kiy.cn.service;

import java.sql.Connection; 
import java.util.List;

import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.YSHException;

public interface MssqlService {

	Connection getMssqlConnection(JMap config) throws YSHException;

	Connection getMssqlConnection(String strServerName, String strDBName, String strUserID, String strPassword)
			throws YSHException;

//	Connection getMssqlConnection(String strServerName, String strInstanceName, String strDBName, String strUserID,
//			String strPassword) throws YSHException;

	List<List<JMap>> getDataSetByMethod(JMap config, String strSql, JMap param) throws YSHException;

	List<JMap> getDataTableByMethod(JMap config, String strSql, JMap param) throws YSHException;

	List<?> getDataByMethod(JMap config, String strSql, JMap param) throws YSHException;

	List<?> getDataByMethod(String strServerName, String strDBName, String strUserID, String strPassword,
			String strSql, JMap param) throws YSHException;

//	List<?> getDataByMethod(String strServerName, String strInstanceName, String strDBName, String strUserID,
//			String strPassword, String strSql, JMap param) throws YSHException; 

}
