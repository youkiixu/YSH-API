package www.kiy.cn;
  

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
 

//Mybatis
@MapperScan("www.kiy.cn.dao")
//Servlet 注解
@ServletComponentScan
@SpringBootApplication
public class RunApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.out.println("Start Spring Boot");
		SpringApplication.run(RunApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RunApplication.class);
	}
}
