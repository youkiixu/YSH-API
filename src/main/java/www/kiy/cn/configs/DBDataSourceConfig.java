package www.kiy.cn.configs;
 
import java.util.HashMap;
import java.util.Map;

//import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import www.kiy.cn.youki.Convert; 
@Service 
public class DBDataSourceConfig  {
	
	//不建议使用
//	@Autowired 
//	CacheInfo cacheInfo;
	private Map<String,DriverManagerDataSource> cacheInfo = new HashMap<String,DriverManagerDataSource>();
	private  SqlSessionFactoryBean sessionFactory =new SqlSessionFactoryBean(); 
	
	/*public DBDataSourceConfig(){
		
	}*/
	public DriverManagerDataSource getMssQLDataSource(String strServerName, String strDBName, String strUserName,
			String strPassword) throws Exception {

		return getMssQLDataSource(strServerName, "MSSQLSERVER", strDBName, strUserName, strPassword);
	}

	public DriverManagerDataSource getMssQLDataSource(String strServerName, String strInstanceName, String strDBName,
			String strUser, String strPassword) throws Exception {

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
		String strJdbcUrl = Convert.ToMssqlJdbcUrl(strServerName, strDBName);
		return getDataSource(strDriverClass, strJdbcUrl, strUser, strPassword);
	}

	public DriverManagerDataSource getDataSource(String strDriverClass, String strJdbcUrl, String strUser,
			String strPassword) throws Exception {
		if (strDriverClass == null)
			strDriverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		if (strJdbcUrl == null || strJdbcUrl.isEmpty())
			throw new Exception("JdbcUrl 不正确");

		String key = String.format("%s%s%s", strJdbcUrl, strUser, strPassword);
		if (cacheInfo.containsKey(key)) {
			// sessionFactory.
			return (DriverManagerDataSource) cacheInfo.get(key);
		}
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(strDriverClass);
		dataSource.setUrl(strJdbcUrl);
		if (strUser != null && !strUser.isEmpty())
			dataSource.setUsername(strUser);
		if (strPassword != null && !strPassword.isEmpty())
			dataSource.setPassword(strPassword);

		sessionFactory.setDataSource(dataSource);

		sessionFactory.setDataSource(dataSource);
		cacheInfo.put(key, dataSource);
		return dataSource;
	}

	public String getMssqlDatabaseId(String strServerName, String strDBName, String strUser, String strPassword)
			throws Exception {

		String strJdbcUrl = Convert.ToMssqlJdbcUrl(strServerName, strDBName);
		DriverManagerDataSource dataSource = getDataSource(null, strJdbcUrl, strUser, strPassword);
		String dbId = sessionFactory.getDatabaseIdProvider().getDatabaseId(dataSource);
		System.out.println("getDBId=" + dbId);
		return dbId;
	}
	/*public SqlSessionFactory sqlSessionFactory() throws Exception {
		//SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean(); 
		return sessionFactory.getObject();
	}*/

}
