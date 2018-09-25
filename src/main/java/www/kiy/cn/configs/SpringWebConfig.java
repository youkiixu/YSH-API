package www.kiy.cn.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class SpringWebConfig extends WebMvcConfigurerAdapter {
	/**
	 * 配置一个对静态文件访问的支持	
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		System.out.println("Start Spring-Web-Config");
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		registry.addResourceHandler("*.html").addResourceLocations("/");
		
	}
	
	
}
