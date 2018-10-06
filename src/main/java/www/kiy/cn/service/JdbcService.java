package www.kiy.cn.service;

import java.sql.Connection;
import java.sql.SQLException;

import www.kiy.cn.youki.YSHException;

public interface JdbcService {
	  Connection getConnection (String strServerName,String strDbName,String strUserName,String strPassword) throws  YSHException, SQLException;
}
