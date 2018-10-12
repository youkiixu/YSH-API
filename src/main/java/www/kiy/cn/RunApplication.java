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
		 
		
//		JMap m = new JMap();
//		m.put("aa", "123");
//		
//		JMap m1 = new JMap();
//		m1.put("aa", "123");
//		lst.add(m1);
//		
		System.out.println("Start Spring Boot"); 
		SpringApplication.run(RunApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RunApplication.class);
	}
}
