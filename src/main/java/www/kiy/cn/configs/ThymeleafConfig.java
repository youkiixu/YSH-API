package www.kiy.cn.configs;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebMvc
@ComponentScan({ "www.kiy.cn.web" })
public class ThymeleafConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}

	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}

	@Bean
	public ISpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver());
		return engine;
	}

	private ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setTemplateMode(TemplateMode.TEXT);
		resolver.setPrefix("classpath:/WEB-INF/templates/");
		 resolver.setSuffix(".html");
		//resolver.setCacheTTLMs(3600000L);//页面缓存
		 resolver.setCacheable(false);//不进行页面缓存
		return resolver;
	}
	//
	// private ITemplateResolver templateResolver() {
	// // ServletContextTemplateResolver resolver = new
	// ServletContextTemplateResolver((ServletContext) applicationContext);
	// SpringResourceTemplateResolver resolver = new
	// SpringResourceTemplateResolver();
	//
	// resolver.setTemplateMode("LEGACYHTML5");
	// ///StandardTemplateModeHandlers.LEGACYHTML5.getTemplateModeName()
	// resolver.setPrefix("/WEB-INF/templates/");
	// resolver.setSuffix(".html");
	// resolver.setCacheTTLMs(3600000L);
	// return resolver;
	// }
}
