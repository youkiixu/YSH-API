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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import www.kiy.cn.dao.saas.SaaS;

@Configuration
// 扫描 Mapper 接口并容器管理
//@MapperScan(  basePackages = SaaSDBConfig.PACKAGE, sqlSessionFactoryRef = "defaultSqlSessionFactory")
@MapperScan(  basePackageClasses ={SaaS.class } , sqlSessionFactoryRef = "defaultSqlSessionFactory")
public class SaaSDBConfig {

	// 精确到 cluster 目录，以便跟其他数据源隔离
	//static final String PACKAGE = "www.kiy.cn.dao.saas.SAAS";
	final String MAPPER_LOCATION = "classpath:mybatis/System/*.xml";

	@Value("${spring.datasource.url}")
	private String strJdbcUrl;

	@Value("${spring.datasource.username}")
	private String strUser;

	@Value("${spring.datasource.password}")
	private String strPassword;

	@Value("${spring.datasource.driver-class-name}")
	private String strDriverClass;

	@Bean(name = "defaultDataSource")
	@Primary //必须指定默认，否则报Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
	@Description("默认连接池,SaaS")
	public DataSource defaultDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(strDriverClass);
		dataSource.setUrl(strJdbcUrl);
		if (strUser != null && !strUser.isEmpty())
			dataSource.setUsername(strUser);
		if (strPassword != null && !strPassword.isEmpty())
			dataSource.setPassword(strPassword);
		return dataSource;
	}

	@Bean(name = "defaultTransactionManager")
	@Description("默认事务连接池")
	public DataSourceTransactionManager defaultTransactionManager() {
		return new DataSourceTransactionManager(defaultDataSource());
	}

	@Bean(name = "defaultSqlSessionFactory")
	@Description("默认sqlSesstionFactory")
	public SqlSessionFactory defaultSqlSessionFactory(@Qualifier("defaultDataSource") DataSource defaultDataSource)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(defaultDataSource);
		sessionFactory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
		System.out.println("sessionFactory=" + sessionFactory);
		return sessionFactory.getObject();
	}
	@Bean(name="defaultSqlSessionTemplate")
	@Primary
	 public SqlSessionTemplate SaaSSqlSessionTemplate(SqlSessionFactory defaultSqlSessionFactory) {
       return new SqlSessionTemplate(defaultSqlSessionFactory);
   }  
}