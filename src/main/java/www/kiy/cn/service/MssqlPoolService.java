package www.kiy.cn.service;

import java.sql.Connection;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import www.kiy.cn.youki.YSHException;

public interface MssqlPoolService {

	void isClose(Connection cn);

	void destory();

	/***
	 * 释放资源
	 */
	void release(Connection cn);
	Connection getConnection() throws YSHException;
	 
	DriverManagerDataSource getDriverManagerDataSource() throws YSHException;

	// 定时检查连接池情况
	void CheckPool();

	String getStrServerName();

	void setStrServerName(String strServerName);

	String getStrDBName();

	void setStrDBName(String strDBName);

	String getStrUserID();

	void setStrUserID(String strUserName);

	String getStrPassword();

	void setStrPassword(String strPassword);

	void setDriverManager(String strServerName, String strDBName, String strUserName, String strPassword);

}