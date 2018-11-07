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
	
	// 不建议使用
	// @Autowired
	// CacheInfo cacheInfo;
	private Map<String, DriverManagerDataSource> cacheInfo = new HashMap<String, DriverManagerDataSource>(); 
	
	//private  Map<String, SqlSessionFactoryBean> sessionFactory= new  HashMap<String,SqlSessionFactoryBean>();

	/*
	 * public DBDataSourceConfig(){
	 * 
	 * }
	 */
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
		return getDataSource(strDriverClass, strServerName,strDBName, strUser, strPassword);
	}

	public DriverManagerDataSource getDataSource(String strDriverClass, String strServerName,String strDBName, String strUser,
			String strPassword) throws Exception {
		String strJdbcUrl = Convert.ToMssqlJdbcUrl(strServerName.toString().replace(',', ':'), strDBName);
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
		 
		
		SqlSessionFactoryBean factory=new SqlSessionFactoryBean(); //this.getSqlSessionFactory( strServerName,strDBName);  
		factory.setDataSource(dataSource);
//		factory.setTypeHandlersPackage("www.kiy.cn.test"); 
//		
//		//添加XML目录
//        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        //jdbc:sqlserver://192.168.0.91;instanceName=MSSQLSERVER;DatabaseName=Cyt_Mall_Dev;integratedSecurity=false
//        factory.setMapperLocations(resolver.getResources(String.format("classpath:mybatis/%s/*.xml",strServerName)));
//        
//        VendorDatabaseIdProvider  databaseIdProvider  =new VendorDatabaseIdProvider ();
//        Properties properties = new Properties();
//        String str= String.format("%s%s", strServerName,strDBName);
//        properties.put(str, str);
//        databaseIdProvider.setProperties(properties);    
//        factory.setDatabaseIdProvider(databaseIdProvider); 
//		System.out.println("databaseId="+factory.getDatabaseIdProvider().getDatabaseId(dataSource));
//		sessionFactory.put(str, factory);
	    cacheInfo.put(key, dataSource); 
		return dataSource;
	}
  
//	public SqlSessionFactoryBean getSqlSessionFactory( String strServerName,String strDBName) throws Exception {
//		String key = String.format("%s%s", strServerName, strDBName);
//		if(sessionFactory.containsKey(key))
//			return sessionFactory.get(key);  
//        return null;
//         
//	}

}
