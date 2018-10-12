package www.kiy.cn.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import www.kiy.cn.service.MssqlPoolService;
import www.kiy.cn.service.MssqlService;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap;

@Service
public class MssqlServiceImpl implements MssqlService {

	private Map<String, MssqlPoolService> cons = new HashMap<String, MssqlPoolService>();

	@Override
	public Connection getMssqlConnection(JMap config) throws Exception {
		// 暂时没有使用mysql 先稳定一版后再优化使用mysql
		// strInstanceName

		return getMssqlConnection(Convert.ToString(config.get("strServerName")),
				Convert.ToString(config.get("strDBName")), Convert.ToString(config.get("strUserID")),
				Convert.ToString(config.get("strPassword")));
	}

	@Override
	public Connection getMssqlConnection(String strServerName, String strDBName, String strUserID, String strPassword)
			throws Exception {

		if (strUserID == null) {
			strUserID = "";
			strPassword = "";
		}
		MssqlPoolService poolService = getConnectPoolService(strServerName, strDBName, strUserID, strPassword);

		return poolService.getConnection();
	}

	@Override
	public List<?> getDataTableByMethod(JMap config, String strSql, JMap param) throws Exception {
		return getDataTableByMethod(Convert.ToString(config.get("strServerName")),
				Convert.ToString(config.get("strDBName")), Convert.ToString(config.get("strUserID")),
				Convert.ToString(config.get("strPassword")), strSql, param);
	}

	@Override
	public List<?> getDataTableByMethod(String strServerName, String strDBName, String strUserID, String strPassword,
			String strSql, JMap param) throws Exception {
		return getDataByMethod(strServerName, strDBName, strUserID, strPassword, strSql, null, param);
	}

	@Override
	public List<?> getDataByMethod(String strServerName, String strDBName, String strUserID, String strPassword,
			String strSql, String strSqlCount, JMap param) throws Exception {
		MssqlPoolService poolService = getConnectPoolService(strServerName, strDBName, strUserID, strPassword);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(poolService.getDriverManagerDataSource());
		NamedParameterJdbcTemplate PJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		List<Map<String, Object>> lst1 = PJdbcTemplate.queryForList(strSql, param);

		if (Convert.isNullOrEmpty(strSqlCount))
			return lst1;

		List<List<Map<String, Object>>> lst = new ArrayList<List<Map<String, Object>>>();
		lst.add(lst1);
		int pageRecord = Convert.ToInt32(param.getWithRemoveKey("pageRecord"));
		param.remove("rowIndex");
		List<Map<String, Object>> lst2 = PJdbcTemplate.queryForList(strSqlCount, param);
		if (lst2.size() > 0) {
			Map<String, Object> map = lst2.get(0);
			int intByPageIndex = Convert.ToInt32(map.get("intByPageIndex")); 
			int intTotalPage = intByPageIndex / pageRecord; 
			if (intByPageIndex % pageRecord != 0)
				intTotalPage += 1;
			map.put("intTotalPage", intTotalPage);
		}
		lst.add(lst2);
		return lst;
	}

	private MssqlPoolService getConnectPoolService(String strServerName, String strDBName, String strUserID,
			String strPassword) throws Exception {

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

	public List<?> JDBCQuery(String strServerName, String strDBName, String strUserID, String strPassword,
			String strSql, String strSqlCount, JMap par) throws Exception {
		MssqlPoolService poolService = getConnectPoolService(strServerName, strDBName, strUserID, strPassword);
		Connection cn = poolService.getConnection();
		PreparedStatement cmd = cn.prepareStatement(strSql);

		Iterator<String> e = par.keySet().iterator();
		int j = 0;
		while (e.hasNext()) {
			String key = e.next();
			cmd.setObject(j, par.get(key));
			j++;
		}
		ResultSet res = cmd.getResultSet();
		List<JMap> lst = new ArrayList<JMap>();
		int intRowCount = res.getRow();
		if (intRowCount > 0) {
			ResultSetMetaData resultSetMetaData = res.getMetaData();
			int intCount = resultSetMetaData.getColumnCount();
			String[] columns = new String[intCount];
			for (int i = 0; i < intCount; i++) {
				String name = resultSetMetaData.getColumnName(i);
				columns[i] = name;
			}
			while (res.next()) {
				JMap map = new JMap();
				for (String str : columns) {
					map.put(str, res.getObject(str));
				}
				lst.add(map);
			}
		}
		cmd.close();
		return null;
	}
	
}
