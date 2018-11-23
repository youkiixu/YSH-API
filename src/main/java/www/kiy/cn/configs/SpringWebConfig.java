//package www.kiy.cn.configs;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//@Configuration
////@EnableWebMvc
//public class SpringWebConfig extends WebMvcConfigurerAdapter {
//	/**
//	 * 配置一个对静态文件访问的支持	
//	 */
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		System.out.println("Start Spring-Web-Config");
//		  //将templates目录下的CSS、JS文件映射为静态资源，防止Spring把这些资源识别成thymeleaf模版
//        registry.addResourceHandler("/templates/**.js");
//        registry.addResourceHandler("/templates/**.css");
//        //其他静态资源
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");  
//	}  
//}
