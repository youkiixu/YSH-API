package www.kiy.cn.service.impl;

import java.sql.Connection;  
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import www.kiy.cn.service.ConnectPoolService;
import www.kiy.cn.service.JdbcService; 
import www.kiy.cn.youki.YSHException;
@Service
public class JdbcServiceImpl implements JdbcService{
	
	
	private Map<String,ConnectPoolService>  cons = new HashMap<String,ConnectPoolService>();
	
	
	public Connection getConnection (String strServerName,String strDbName,String strUserID,String strPassword) throws  YSHException{
		String key = String.format("%s%s%s%s", strServerName,strDbName,strUserID,strPassword);
		ConnectPoolService connectPoolService;
		if(cons.containsKey(key)){
			connectPoolService = cons.get(key); 
		}else{
			connectPoolService = new ConnectPoolServiceImpl( strServerName, strDbName, strUserID, strPassword); 
			cons.put(key, connectPoolService);
		} 
		
		return 	connectPoolService.getConnection();
	}
	
}
