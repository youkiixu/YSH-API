package www.kiy.cn.configs;
 
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import www.kiy.cn.youki.YSHException; 


public class DBDataSourceConfig {
	
	@Autowired
	private  SqlSessionFactoryBean sessionFactory;// = new SqlSessionFactoryBean(); 
	
	/*public DBDataSourceConfig(){
		
	}*/
	public DriverManagerDataSource getMssQLDataSource(String strServerName, String strDBName, String strUser,
			String strPassword) throws YSHException {

		return getMssQLDataSource(strServerName, "MSSQLSERVER", strDBName, strUser, strPassword);
	}

	public DriverManagerDataSource getMssQLDataSource(String strServerName, String strInstanceName, String strDBName,
			String strUser, String strPassword) throws YSHException {

		if (strInstanceName == null || strInstanceName.isEmpty())
			strInstanceName = "MSSQLSERVER";

		/*
		 * spring.datasource.url=jdbc:sqlserver://192.168.0.91;instanceName=
		 * MSSQLSERVER;DatabaseName=SaaS;integratedSecurity=false
		 * spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.
		 * SQLServerDriver spring.datasource.username=dev
		 * spring.datasource.password=dev
		 */

		String strDriverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String strJdbcUrl = String.format(
				"jdbc:sqlserver://%s;instanceName=%s;DatabaseName=%s;integratedSecurity=false", strServerName,
				strInstanceName, strDBName);

		return getDataSource(strDriverClass, strJdbcUrl, strUser, strPassword);

	}

	public DriverManagerDataSource getDataSource(String strDriverClass, String strJdbcUrl, String strUser,
			String strPassword) throws YSHException {
		if (strDriverClass == null)
			strDriverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		if (strJdbcUrl == null || strJdbcUrl.isEmpty())
			throw new YSHException("JdbcUrl 不正确");
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(strDriverClass);
		dataSource.setUrl(strJdbcUrl);
		if (strUser != null && !strUser.isEmpty())
			dataSource.setUsername(strUser);
		if (strPassword != null && !strPassword.isEmpty())
			dataSource.setPassword(strPassword);
		
		 sessionFactory.setDataSource(dataSource);
		
		sessionFactory.setDataSource(dataSource);
		
		return dataSource;
	}

	/*public SqlSessionFactory sqlSessionFactory() throws Exception {
		//SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean(); 
		return sessionFactory.getObject();
	}*/

}
