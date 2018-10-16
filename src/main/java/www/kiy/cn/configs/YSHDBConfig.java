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
//@MapperScan(basePackages = YSHDBConfig.PACKAGE, sqlSessionFactoryRef = "YSHSqlSessionFactory")
@MapperScan(basePackageClasses = {SaaS.class}, sqlSessionFactoryRef = "YSHSqlSessionFactory")

public class YSHDBConfig {

	// 精确到 目录，以便跟其他数据源隔离
	//static final String PACKAGE = "www.kiy.cn.dao.saas";
	 final String MAPPER_LOCATION = "classpath:mybatis/YSH/*.xml";

	@Value("${YSH.datasource.url}")
	private String strJdbcUrl;

	@Value("${YSH.datasource.username}")
	private String strUser;

	@Value("${YSH.datasource.password}")
	private String strPassword;

	@Value("${spring.datasource.driver-class-name}")
	private String strDriverClass;

	@Bean(name = "YSHDataSource")
	@Description("商城连接池,YSH")
	public DataSource YSHDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(strDriverClass);
		dataSource.setUrl(strJdbcUrl);
		if (strUser != null && !strUser.isEmpty())
			dataSource.setUsername(strUser);
		if (strPassword != null && !strPassword.isEmpty())
			dataSource.setPassword(strPassword);
		System.out.println("YSH DataSourceTransactionManager");
		return dataSource;
	}

	@Bean(name = "YSHTransactionManager")
	@Description("商城事务连接池")
	public DataSourceTransactionManager YSHTransactionManager() {
		System.out.println("YSH DataSourceTransactionManager");
		return new DataSourceTransactionManager(YSHDataSource());
	}

	@Bean(name = "YSHSqlSessionFactory")
	@Description("商城sqlSesstionFactory")
	public SqlSessionFactory YSHSqlSessionFactory(@Qualifier("YSHDataSource") DataSource YSHDataSource)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(YSHDataSource);
		sessionFactory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
		System.out.println("YSH sessionFactory=" + sessionFactory);
		return sessionFactory.getObject();
	}
	
	@Bean(name="YSHSqlSessionTemplate") 
	 public SqlSessionTemplate YSHSqlSessionTemplate(SqlSessionFactory YSHSqlSessionFactory) {
		System.out.println("YSH YSHSqlSessionTemplate=");
       return new SqlSessionTemplate(YSHSqlSessionFactory);
   } 
}