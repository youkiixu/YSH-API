package www.kiy.cn.service;

import java.sql.Connection; 
import java.util.List;

import www.kiy.cn.youki.JMap; 

public interface MssqlService {

	Connection getMssqlConnection(JMap config) throws Exception;

	Connection getMssqlConnection(String strServerName, String strDBName, String strUserID, String strPassword)
			throws Exception;

//	Connection getMssqlConnection(String strServerName, String strInstanceName, String strDBName, String strUserID,
//			String strPassword) throws YSHException;

	List<List<JMap>> getDataSetByMethod(JMap config, String strSql, JMap param) throws Exception;

	List<JMap> getDataTableByMethod(JMap config, String strSql, JMap param) throws Exception;

	List<?> getDataByMethod(JMap config, String strSql, JMap param) throws Exception;

	List<?> getDataByMethod(String strServerName, String strDBName, String strUserID, String strPassword,
			String strSql, JMap param) throws Exception;

//	List<?> getDataByMethod(String strServerName, String strInstanceName, String strDBName, String strUserID,
//			String strPassword, String strSql, JMap param) throws YSHException; 

}
