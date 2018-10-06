package www.kiy.cn.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
 
import org.springframework.core.annotation.Order;

import www.kiy.cn.youki.SetLog;

//@Order注解表示执行过滤顺序，值越小，越先执行
@Order(2) 
@WebFilter(urlPatterns="/YSH/*",filterName="yshFilter")
public class YSHFilter implements Filter {
  
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
		SetLog.logInfo("destroy-YSHFilter");  
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rep, FilterChain arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		//getRemoteHost() 影响性能
		SetLog.logInfo("doFilter-YSHFilter;RemoteAddr="+req.getRemoteAddr()+"");
		
		//String ip= req.getRemoteAddr();
		//int port= req.getRemotePort();
		
		arg2.doFilter(req,rep);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		SetLog.logInfo("init-YSHFilter");
		
	}

}
