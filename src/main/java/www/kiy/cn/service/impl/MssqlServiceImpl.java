package www.kiy.cn.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
import www.kiy.cn.youki.SetLog;

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
	public void releaseCN(JMap config,Connection cn){
		if(config!=null){
			MssqlPoolService poolService = getConnectPoolService(config);
			if(poolService!=null){
				poolService.release(cn);
				return;
			}
		}
		try {
			cn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		cn =null;
		
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
		return getDataByJDBC(strServerName, strDBName, strUserID, strPassword, strSql, null, param);
	}

	@Override
	public List<?> getDataByJdbcTemplate(String strServerName, String strDBName, String strUserID, String strPassword,
			String strSql, String strSqlCount, JMap param) throws Exception {
		MssqlPoolService poolService = getConnectPoolService(strServerName, strDBName, strUserID, strPassword);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(poolService.getDriverManagerDataSource());
		NamedParameterJdbcTemplate PJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		 
		List<Map<String, Object>> lst1 = PJdbcTemplate.queryForList(strSql, param);
		System.out.println("============PJdbcTemplate Query Start ============ ");
		System.out.println(strSql);
		System.out.println(param); 
		System.out.println(String.format("count=%d",lst1.size()));
		System.out.println("============PJdbcTemplate Query end ============ ");
		if (Convert.isNullOrEmpty(strSqlCount))
			return lst1;
		
		List<List<Map<String, Object>>> lst = new ArrayList<List<Map<String, Object>>>();
		lst.add(lst1);
		int pageRecord = Convert.ToInt32(param.getWithRemoveKey("pageRecord"));
		param.remove("rowIndex");
		System.out.println("============PJdbcTemplate Query Start ============ ");
		List<Map<String, Object>> lst2 = PJdbcTemplate.queryForList(strSqlCount, param);
		
		
		System.out.println(strSqlCount);
		System.out.println(param); 
		System.out.println(String.format("count=%d",lst2.size()));
		System.out.println("============PJdbcTemplate Query end ============ ");
		
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
	@Override
	public MssqlPoolService getConnectPoolService(JMap config) {
		return getConnectPoolService(Convert.ToString(config.get("strServerName")),
				Convert.ToString(config.get("strDBName")), Convert.ToString(config.get("strUserID")),
				Convert.ToString(config.get("strPassword")));
	}
	@Override
	public MssqlPoolService getConnectPoolService(String strServerName, String strDBName, String strUserID,
			String strPassword) {

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

	@Override
	public List<?> getDataByJDBC(String strServerName, String strDBName, String strUserID, String strPassword,
			String strSql, String strSqlCount, JMap par) throws Exception {
		
		MssqlPoolService poolService = getConnectPoolService(strServerName, strDBName, strUserID, strPassword);
		Connection cn = poolService.getConnection();
		PreparedStatement cmd = null;
		ResultSet res = null;  
		try {
			
			System.out.println("============JDBC  Query Start ============ ");
			System.out.println(strSql);
			System.out.println(par); 
			cmd = cn.prepareStatement(strSql);
			Iterator<String> e = par.keySet().iterator();
			int j = 1;
			while (e.hasNext()) {
				String key = e.next();
				cmd.setObject(j, par.get(key));
				j++;
			}
 
			res = cmd.executeQuery();
			System.out.println("============JDBC  Query end ============ "); 
			
			List<JMap> lst = getResultSet(res);
			
			if (!Convert.isNullOrEmpty(strSqlCount)) {
				res.close();
				cmd.close();
				
				System.out.println("============JDBC  Query Start ============ ");
				System.out.println(strSqlCount);
				System.out.println(par);  
				cmd = cn.prepareStatement(strSqlCount);
				
				e = par.keySet().iterator();
				j = 1;
				while (e.hasNext()) {
					String key = e.next();
					cmd.setObject(j, par.get(key));
					j++;
				} 
				res= cmd.executeQuery();
				System.out.println("============JDBC  Query end ============ "); 
				List<JMap> lst1 = getResultSet(res);
				List<List<JMap>> lst2 = new ArrayList<List<JMap>>();
				lst2.add(lst);
				lst2.add(lst1);
				return lst2;
			}
			return lst;
		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			if (res != null) {
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					SetLog.Info("JDBCQuery", e.toString());
					e.printStackTrace();

				}
				res = null;
			}
			if (cmd != null) {
				try {
					cmd.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					SetLog.Info("JDBCQuery", e.toString());
					e.printStackTrace();
				}
				cmd = null;
			}
			poolService.release(cn);
		}
	}
	
	private List<JMap> getResultSet(ResultSet res) throws Exception {
		List<JMap> lst = new ArrayList<JMap>();
		//try {
			//int intRowCount = res.getRow();
			//if (intRowCount > 0) 
			{ 
				ResultSetMetaData resultSetMetaData = res.getMetaData();
				int intCount = resultSetMetaData.getColumnCount();
				String[] columns = new String[intCount];
				for (int i = 0; i < intCount; i++) {
					String name = resultSetMetaData.getColumnName(i+1);
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

//		} catch (Exception ex) {
//
//		} finally {
//			try {
//				res.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		res = null;
		return lst;
	} 

}
