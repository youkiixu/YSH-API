package www.kiy.cn.configs;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description; 
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import www.kiy.cn.dao.saas.SaaS;

@Configuration
// 扫描 Mapper 接口并容器管理 
@MapperScan(basePackageClasses = {SaaS.class}, sqlSessionFactoryRef = "YJSqlSessionFactory")

public class YJDBConfig {

	// 精确到 目录，以便跟其他数据源隔离
	//static final String PACKAGE = "www.kiy.cn.dao.saas";
	 final String MAPPER_LOCATION = "classpath:mybatis/YJ/*.xml";

	@Value("${YJ.datasource.url}")
	private String strJdbcUrl;

	@Value("${YJ.datasource.username}")
	private String strUser;

	@Value("${YJ.datasource.password}")
	private String strPassword;

	@Value("${spring.datasource.driver-class-name}")
	private String strDriverClass;

	@Bean(name = "YJDataSource")
	@Description("印捷连接池,YJ")
	public DataSource YJDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(strDriverClass);
		dataSource.setUrl(strJdbcUrl);
		if (strUser != null && !strUser.isEmpty())
			dataSource.setUsername(strUser);
		if (strPassword != null && !strPassword.isEmpty())
			dataSource.setPassword(strPassword);
		System.out.println("YJ DataSourceTransactionManager");
		return dataSource;
	}

	@Bean(name = "YJTransactionManager")
	@Description("印捷事务连接池")
	public DataSourceTransactionManager YJTransactionManager() {
		System.out.println("YJ DataSourceTransactionManager");
		return new DataSourceTransactionManager(YJDataSource());
	}

	@Bean(name = "YJSqlSessionFactory")
	@Description("印捷sqlSesstionFactory")
	public SqlSessionFactory YJSqlSessionFactory(@Qualifier("YJDataSource") DataSource YJDataSource)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(YJDataSource);
		sessionFactory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
		System.out.println("YJ sessionFactory=" + sessionFactory);
		return sessionFactory.getObject();
	}
	
	@Bean(name="YJSqlSessionTemplate") 
	 public SqlSessionTemplate YJSqlSessionTemplate(SqlSessionFactory YJSqlSessionFactory) {
		System.out.println("YJ YJSqlSessionTemplate=");
       return new SqlSessionTemplate(YJSqlSessionFactory);
   } 
}