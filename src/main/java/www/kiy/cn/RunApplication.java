package www.kiy.cn;
  
 

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;


//Mybatis
//@MapperScan("www.kiy.cn.dao.saas")
//Servlet 注解
@ServletComponentScan
@SpringBootApplication
public class RunApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {  
		System.out.println("Start Spring Boot");  
		
		SpringApplication.run(RunApplication.class, args);
		
//		List<JMap> list = new ArrayList<JMap>();
//		JMap m1 = new JMap(); 
//		JMap m2 = new JMap();
//		JMap m3 = new JMap();
//		JMap m4 = new JMap();
//		m1.put("a", "123");
//		m2.put("a", "123");
//		m3.put("a", "123");
//		m4.put("a", "123");
//		
//		m1.put("b", "123");
//		m2.put("b", "123");
//		m3.put("b", "123");
//		m4.put("b", "123");
//		
//		m1.put("c", "123");
//		m2.put("c", "123");
//		m3.put("c", "123");
//		m4.put("c", "123");
//		
//		
//		list.add(m1); 
//		list.add(m2); 
//		list.add(m3);
//		list.add(m4);
//		Iterator<JMap> it = list.iterator();
//		while (it.hasNext()) {
//			JMap map = it.next();
//			Iterator<String> ele = map.keySet().iterator();
//			while (ele.hasNext()) {
//				String str = ele.next();
//				if (str.equals("a")) {
//					ele.remove(); 
//				}
//			}
//		}
//		m1.put("a", "123");
//		System.out.println(list);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RunApplication.class);
	}
}
