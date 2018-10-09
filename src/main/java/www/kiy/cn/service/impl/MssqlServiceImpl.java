package www.kiy.cn.service.impl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import www.kiy.cn.service.MssqlPoolService;
import www.kiy.cn.service.MssqlService;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.YSHException;

@Service
public class MssqlServiceImpl implements MssqlService {
//	@Autowired
//	MssqlPoolService mssqlPool;
	
	private Map<String, MssqlPoolService> cons = new HashMap<String, MssqlPoolService>();
	
	@Override
	public Connection getMssqlConnection(JMap config) throws YSHException {
		// 暂时没有使用mysql 先稳定一版后再优化使用mysql
		// strInstanceName

		return getMssqlConnection(Convert.ToString(config.get("strServerName")),
				Convert.ToString(config.get("strDBName")), Convert.ToString(config.get("strUserID")),
				Convert.ToString(config.get("strPassword")));
	}

	@Override
	public Connection getMssqlConnection(String strServerName, String strDBName, String strUserID, String strPassword)
			throws YSHException {

		if (strUserID == null) {
			strUserID = "";
			strPassword = "";
		}
		// String key = String.format("%s%s%s%s%s", strServerName,
		// strInstanceName, strDbName, strUserID, strPassword);
		MssqlPoolService poolService = getConnectPoolService(strServerName, strDBName, strUserID, strPassword);

		return poolService.getConnection();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<List<JMap>> getDataSetByMethod(JMap config, String strSql, JMap param) throws YSHException {
		return (List<List<JMap>>) getDataByMethod(config, strSql, param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JMap> getDataTableByMethod(JMap config, String strSql, JMap param) throws YSHException {
		return (List<JMap>) getDataByMethod(config, strSql, param);
	}

	@Override
	public List<?> getDataByMethod(JMap config, String strSql, JMap param) throws YSHException {
		return getDataByMethod(Convert.ToString(config.get("strServerName")), Convert.ToString(config.get("strDBName")),
				Convert.ToString(config.get("strUserID")), Convert.ToString(config.get("strPassword")), strSql, param);
	}

	@Override
	public List<?> getDataByMethod(String strServerName, String strDBName, String strUserID, String strPassword,
			String strSql, JMap param) throws YSHException {

		MssqlPoolService poolService = getConnectPoolService(strServerName, strDBName, strUserID, strPassword);
		 poolService.getDriverManagerDataSource();
		
		//JdbcTemplate jdbcTemplate = new JdbcTemplate();
		// jdbcTemplate.query(sql, args, rse);

		return null;

	}

	private MssqlPoolService getConnectPoolService(String strServerName, String strDBName, String strUserID,
			String strPassword) throws YSHException {

		String key = String.format("%s%s%s%s", strServerName, strDBName, strUserID, strPassword);
		MssqlPoolService poolService;

		if (cons.containsKey(key)) {
			poolService = cons.get(key);
		} else {
			poolService = new MssqlPoolServiceImpl(strServerName, strDBName, strUserID, strPassword);

			cons.put(key, poolService);
		}
		
		return poolService;
	}

}
