package www.kiy.cn.service.impl;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
 
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import www.kiy.cn.HotKey;
import www.kiy.cn.HotKey.eSqlType; 
import www.kiy.cn.dao.saas.SaaS;
import www.kiy.cn.service.MssqlService; 
import www.kiy.cn.service.QueryService;
import www.kiy.cn.youki.CacheInfo;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.ListMap; 
import www.kiy.cn.youki.SetLog;

@Service
public class QueryServiceImpl implements QueryService {
	@Resource
	private SaaS systemDao;
	@Autowired
	private CacheInfo cacheInfo;

	@Autowired
	private MssqlService mssql;

	@Value("${spring.datasource.url}")
	private String strDbUrl;
	@Value("${spring.datasource.username}")
	private String strUserId;
	@Value("${spring.datasource.password}")
	private String strPassword;

	@Autowired
	 private ApplicationContext applicationContext;
	
	
	@Override
	public JMap getDBConfig(int DBId) throws Exception {
		List<JMap> lst = getDBConfig(DBId, null, 0, null, true);
		if (lst != null && lst.size() > 0)
			return lst.get(0);
		return null;
	}

	@Override
	public JMap getDBConfig(String strAppid, boolean bDefault) throws Exception {
		return getDBConfig(strAppid, null, bDefault);
	}

	@Override
	public JMap getDBConfig(String strAppid, String strDBName, boolean bDefault) throws Exception {
		List<JMap> lst = getDBConfig(strAppid, 0, strDBName, bDefault);
		if (lst != null && lst.size() > 0)
			return lst.get(0);
		return null;
	}

	@Override
	public List<JMap> getDBConfig(String strAppid, int intSysProject, String strDBName, boolean bDefault)
			throws Exception {
		return getDBConfig(0, strAppid, intSysProject, strDBName, bDefault);
	}

	@Override
	public List<JMap> getDBConfig(int DBId, String strAppid, int intSysProject, String strDBName, boolean bDefault)
			throws Exception {

		JMap map = new JMap();
		// StringBuilder sb = new StringBuilder();
		if (DBId != 0) {
			map.put("@ID", DBId);
			// sb.append(DBId);
		} else {
			if (strAppid != null) {
				map.put("@strAppid", strAppid);
				// sb.append(strAppid);
			}
			if (intSysProject != 0) {
				map.put("@intSysProject", intSysProject);
				// sb.append(intSysProject);
			}
			if (strDBName != null) {
				map.put("@strDBName", strDBName);
				// sb.append(strDBName);
			}

			if (Convert.isNullOrEmpty(strDBName) && intSysProject != 0) {
				map.put("@bDefault", bDefault);
				// sb.append(bDefault);
			}
		}
		List<JMap> lst = QueryCacheListFromMap(HotKey.lstDBConfig, HotKey.QSysDBConfigInfo, map);
		return lst;
	}

	@Override
	public List<JMap> QueryCacheListFromMap(String key, String strMethod, JMap param) throws Exception {
		return QueryCacheListFromMap(key, null, strMethod, param);
	}

	@Override
	public List<JMap> QueryCacheListFromMap(String key, JMap config, String strMethod, JMap param) throws Exception {

		return QueryCacheListFromMap(key, SetLog.GetJSONString(param), config, strMethod, param);
	}

	/**
	 * 缓存+ 数据信息读取
	 * 
	 * @param key
	 *            缓存名
	 * @param uid
	 *            主键
	 * @param strMethod
	 *            执行方法
	 * @param param
	 *            参数
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<JMap> QueryCacheListFromMap(String key, String uid, JMap config, String strMethod, JMap param)
			throws Exception {
		List<JMap> lst = cacheInfo.getListFromMap(key, uid);
		if (lst == null) {
			lst = new ListMap();
		}
		if (lst.size() > 0)
			return lst;
		List<JMap> lst1 = this.getDataTableByMethod(config, strMethod, param);
		lst.addAll(lst1);
		cacheInfo.putListToMap(key, uid, lst);
		return lst;
	}

	@Override
	public List<JMap> getDataTableByMethod(String strName) throws Exception {

		return getDataTableByMethod(strName, null);
	}
	public List<JMap> getDataTableByMethod(String strName, JMap map) throws Exception {
		return getDataTableByMethod(null, strName,  map);
	}
	
	@Override
	public List<JMap> getDataTableByMethod(JMap config, String strName, JMap map) throws Exception {
		// TODO Auto-generated method stub
		return getDataTableByMethod( config, strName,  map,eSqlType.Jdbc);
	}
	 

	@Override
	public List<JMap> getDataTableByMethod(String strName, JMap map, eSqlType type) throws Exception {
		// TODO Auto-generated method stub
		return this.getDataTableByMethod(null,strName,map,type);
	}
	
	@Override
	public List<List<JMap>> getDataSetByMethod(String strName) throws Exception {

		return getDataSetByMethod(strName, null);
	}

	@Override
	public List<List<JMap>> getDataSetByMethod(String strName, JMap map) throws Exception {
		return getDataSetByMethod(null, strName, map);
	}

	public List<List<JMap>> getDataSetByMethod(JMap config, String strMethod, JMap param) throws Exception {
		@SuppressWarnings("unchecked")
		List<List<JMap>> mm = (List<List<JMap>>) getDataTableBySystemDao(config, strMethod, param);
		if (mm != null)
			System.out.println(mm);
		return mm;
	}

	@Override
	public List<JMap> getDataTableByMethod(JMap config, String strName, JMap map,eSqlType type) throws Exception {
		@SuppressWarnings("unchecked")
		List<JMap> mm = (List<JMap>) getDataTableBySystemDao(config, strName, map,type);
		if (mm != null)
			System.out.println(mm);
		return mm;
	}

	public List<?> getDataTableBySystemDao(final JMap config, String strName, final JMap map) throws Exception {
		return getDataTableBySystemDao(config, strName, map, eSqlType.Jdbc);
	}

	/**
	 * 参数统一使用@进行方便操作,为兼容.net 缺陷：strMethod 与strFrom 中对@处理不友好
	 * 
	 * @param config
	 * @param strName
	 * @param map
	 * @param type
	 * @return
	 * @throws YSHException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<?> getDataTableBySystemDao(JMap config, String strName, final JMap map, eSqlType type)
			throws Exception {
		type = eSqlType.Mybatis;

		JMap m = cacheInfo.getMap(HotKey.mSysQueryMethodConfig);
		if (m == null || m.size() == 0) {
			if (m == null)
				m = new JMap();
		}
		List<List<JMap>> lst = null;
		if (m.containsKey(strName))
			lst = (List<List<JMap>>) SetLog.ObjectToObject(m.get(strName), List.class);
		else {
			lst = (List<List<JMap>>) systemDao.sysQueryAndFliter(strName);
			m.put(strName, lst);
			cacheInfo.putJMap(HotKey.mSysQueryMethodConfig, m);
		}

		if (lst == null || lst.get(0).size() == 0)
			throw new Exception("找不到对应方法");
		JMap SysQueryMethod = SetLog.ObjectToListMap(lst.get(0)).get(0);
		
		String strServerName = null;
		// String strInstanceName =null;
		String strDBName = null;
		String strUserID = null;
		String strPassword = null;

		String strKey =null;
		int intDBConfig = Convert.ToInt32(SysQueryMethod.get("intDBConfig"));
		if (!Convert.isNullOrEmpty(Convert.ToString(SysQueryMethod.get("strServerName")))) {
			// 当直接指定时获取直接指定数据库
			strServerName = Convert.ToString(SysQueryMethod.get("strServerName"));
			// strInstanceName =
			// Convert.ToString(SysQueryMethod.get("strInstanceName"));
			strDBName = Convert.ToString(SysQueryMethod.get("strDBName"));
			strUserID = Convert.ToString(SysQueryMethod.get("strUserID"));
			strPassword = Convert.ToString(SysQueryMethod.get("strPassword"));
		} else if (intDBConfig != 0) {
			config = this.getDBConfig(intDBConfig);
			strKey =Convert.ToString(  config.get("strKey"));

		}
		if (config != null && config.size() > 0) {
			strServerName = Convert.ToString(config.get("strServerName"));
			// strInstanceName =
			// Convert.ToString(config.get("strInstanceName"));
			strDBName = Convert.ToString(config.get("strDBName"));
			strUserID = Convert.ToString(config.get("strUserID"));
			strPassword = Convert.ToString(config.get("strPassword"));
			strKey =Convert.ToString(  config.get("strKey"));

		} else {
			// 系统默认级别路径
			// jdbc:sqlserver://101.37.27.55:50156;DatabaseName=SaaS;integratedSecurity=false
			String[] arr = this.strDbUrl.split(";");
			strServerName = arr[0].split("://")[1];
			strDBName = arr[1].split("=")[1];
			strUserID = this.strUserId;
			strPassword = this.strPassword;
		}
		// type = eSqlType.mybatis;
		String strMethod = SysQueryMethod.get("strMethod").toString();
		String strGroupBy = SysQueryMethod.get("strGroupBy").toString();
		String strFrom = Convert.ToString(SysQueryMethod.get("strFrom")); //SysQueryMethod.get("strFrom") == null ? "" : SysQueryMethod.get("strFrom").toString();
		
		
		StringBuilder str = new StringBuilder();

		StringBuilder sql = new StringBuilder(String.format("%s %s", strMethod, strFrom));
		Iterator<?> iterator = lst.get(1).iterator();
		JMap par = new JMap();
		String p ="@";
		int size =0;
		while (iterator.hasNext()) {
			JMap tmp = SetLog.ObjectToMap(iterator.next());
			String strFliterParam = tmp.get("strFliterParam").toString();

			String strColumn = Convert.ToString(tmp.get("strColumn"));
			strColumn = strColumn == null ? "" : strColumn;

			String strValue = Convert.ToString(tmp.get("strValue"));
			strValue = strValue == null ? "" : strValue;

			String strRule = Convert.ToString(tmp.get("strRule"));
			strRule = strRule == null ? "" : strRule;

			if (strFliterParam.toUpperCase().equals("FIX")) {
				// 固定值
				if (strRule.toUpperCase().contains("IN")) {
					str.append(String.format(" %s %s (%s) ", strColumn, strRule, strValue));
				} else {
					str.append(strColumn).append(strRule).append(strValue);
				}
				str.append(" and ");

			} else {

				boolean bIsNotNull = Convert.ToBoolean(SysQueryMethod.get("bIsNotNull"));
				String strFliterParam2 = strFliterParam;
				String strFliterParam1 = strFliterParam.replace("@", "$");
				// if (strFliterParam1.contains("#{")) {
				// strFliterParam1 = strFliterParam1.replace("#{",
				// "@").replace("}", "");
				// }
				// strFliterParam=@userId strFliterParam1=$userId
				if (bIsNotNull) {
					if (map == null || (!map.containsKey(strFliterParam) && !map.containsKey(strFliterParam1)))
						throw new Exception(String.format("关键参数%s必填", strFliterParam));
				}

				if (map != null && (map.containsKey(strFliterParam) || map.containsKey(strFliterParam1))) {

					// 处理值 将 @变成:参数名 或 参数名
					if (/* map.containsKey(strFliterParam) && */ Convert.ToString((map.get(strFliterParam)))
							.equals("undefined")) {
						map.remove(strFliterParam);
						continue;
					} else if (Convert.ToString(map.containsKey(strFliterParam1)).equals("undefined")) {
						map.remove(strFliterParam1);
						continue;
					} else {
						Object objVal;
						if (map.containsKey(strFliterParam))
							objVal = map.getWithRemoveKey(strFliterParam);
						else
							objVal = map.getWithRemoveKey(strFliterParam1);
						switch (type) {
						case Jdbc:
							strFliterParam2 = "?";
							p=String.format("%s", size++);
							
							par.put(p, objVal);
							break;
						case JdbcTemplate:
							strFliterParam2 = strFliterParam.replace("@", ":");
							par.put(strFliterParam.replace("@", ""), objVal);
							break;
						case Mybatis:
							strFliterParam2 = String.format("#{%s}", strFliterParam.replace("@", ""));
							par.put(strFliterParam.replace("@", ""), objVal);
							break;
						}
					}
					if (strColumn.isEmpty())
						str.append(String.format(" %s ", strValue));
					else {
						str.append(String.format(" %s ", strColumn));
						if (strRule.toUpperCase() == "LIKE")
							str.append(String.format(" like '%'+%s+'%'", strFliterParam2));
						else if (strRule.toUpperCase() == "LIKE%")
							str.append(String.format(" like %s+'%'", strFliterParam2));
						else if (strRule.toUpperCase() == "%LIKE")
							str.append(String.format(" '%'+like %s", strFliterParam2));

						else if (strRule.toUpperCase().contains("in")) {
							if (!map.containsKey(strFliterParam))// 必须由内部数据统一过滤
								throw new Exception("过滤参数{%s}缺失");
							str.append(
									String.format(" %s (%s) ", strRule, strValue.replace("or", "").replace("=", "")));

						} else {
							str.append(String.format(" %s %s  ", strRule, strFliterParam2));
						}
					}
					str.append(" and ");
				}

			}
		}
		StringBuilder condition = new StringBuilder();
		if (str.length() > 0) {
			if (!Boolean.valueOf(SysQueryMethod.get("bAppend").toString())) {
				condition.append(" where ");

			} else {
				condition.append(" and ");
			}
			int intlen = str.length();
			condition.append(str.delete(intlen - 4, intlen));
		}
		String sqlCount = "";
		StringBuilder builder = new StringBuilder();
		boolean bPage= Convert.ToBoolean(SysQueryMethod.get("bPage"));
		
		if (!bPage) {
			builder.append(sql).append(condition).append(" ").append(strGroupBy);
		} else {
			Object rowIndex = 1;
			Object pageRecord = 15;
			if (map.containsKey("$rowIndex")) {
				rowIndex = map.getWithRemoveKey("$rowIndex");
			} else {
				if (map.containsKey("@rowIndex"))
					rowIndex = map.getWithRemoveKey("@rowIndex");
			}
			if (map.containsKey("$pageRecord")) {
				pageRecord = map.getWithRemoveKey("$pageRecord");
			} else {
				if (map.containsKey("@pageRecord"))
					pageRecord = map.getWithRemoveKey("@pageRecord");
			}
			String sRowIndex = "";
			String sPageRecord = "";
			switch (type) {
			case Jdbc:
				sRowIndex = "?";
				sPageRecord = "?";  
				p=String.format("%s", size++);
				par.put(p, rowIndex);
				p=String.format("%s", size++);
				par.put(p, pageRecord);
				p=String.format("%s", size++);
				par.put(p, rowIndex);
				p=String.format("%s", size++);
				par.put(p, pageRecord);
				break;
			case Mybatis:
			case JdbcTemplate:
				if(eSqlType.JdbcTemplate==type){
					sRowIndex = ":rowIndex";
					sPageRecord = ":pageRecord";
				}else{
					sRowIndex = "#{rowIndex}";
					sPageRecord = "#{pageRecord}";
				}
				par.put("rowIndex", rowIndex);
				par.put("pageRecord", pageRecord);
				break;
			}
			// par.put(sRowIndex, rowIndex);
			// par.put(sPageRecord, pageRecord);
			

			if (Convert.isNullOrEmpty(strFrom)) {
				int len = sql.toString().toUpperCase().lastIndexOf("FORM");
				sqlCount = String.format("  select count(1) intByPageIndex/*intEmptyByPageIndex*/ %s \n %s  \n",
						sql.substring(len, sql.length() - len), condition);
			} else if (strGroupBy.toUpperCase().contains("GROUP")) {
				sqlCount = String.format(
						" select count(intCount) intByPageIndex/*intGroupByBPageIndex */ from ( select count(1) intCount  %s \n %s \n %s ) t ",
						strFrom, condition, strGroupBy);
			} else {
				sqlCount = String.format("  select count(1) intByPageIndex/*intFromByPageIndex*/ %s \n %s \n", strFrom,
						condition);
			}
			int roNumIndx = sql.toString().toUpperCase().indexOf("ROW_NUMBER()");
			if (roNumIndx > 0)
				sql = sql.insert(roNumIndx,
						String.format("  top %s   ", Convert.ToInt32(rowIndex) * Convert.ToInt32(pageRecord)));

			builder.append("select * from ( \r\n");
			builder.append(sql);
			builder.append(condition);
			builder.append(strGroupBy);
			builder.append(" ) t  \r\n where ");

			builder.append(" t.rowNum>= (").append(sRowIndex).append(" -1 ) *Convert(int, ").append(sPageRecord)
					.append(")+1 ");
			builder.append(" and ");
			builder.append(" t.rowNum<= ").append(sRowIndex).append(" *Convert(int, ").append(sPageRecord)
					.append(") \r\n");
 
		}
		return QuerySql( strKey,  strServerName,  strDBName,  strUserID, strPassword,builder.toString(),sqlCount,bPage , par,type);
	}
	
	@Override
	public List<?> QuerySql(JMap config,String sql,JMap par) throws Exception{
		return QuerySql(config,sql,par,eSqlType.Mybatis);
	}
	@Override
	public List<?> QuerySql(JMap config,String sql,JMap par,eSqlType type) throws Exception{
		return QuerySql(config,sql,null,false,par,type);
	}
	@Override
	public List<?> QuerySql(JMap config, String sql, String sqlCount, boolean bPage, JMap par, eSqlType type)
			throws Exception {

		return QuerySql(Convert.ToString(config.get("strKey")), Convert.ToString(config.get("strServerName")),
				Convert.ToString(config.get("strDBName")), Convert.ToString(config.get("strUserID")),
				Convert.ToString(config.get("strPassword")), sql, sqlCount, bPage, par, type);
	}
	@Override
	public List<?> QuerySql(String strKey, String strServerName, String strDBName, String strUserID,
			String strPassword, String sql, String sqlCount, boolean bPage, JMap par, eSqlType type) throws Exception {

		if ( type==eSqlType.Mybatis &&( !Convert.isNullOrEmpty(strKey) || ( Convert.isNullOrEmpty(strServerName) &&Convert.isNullOrEmpty(strDBName)) )) {
			if(Convert.isNullOrEmpty(strKey))
				strKey="default";
			SqlSessionFactory yshSqlSessionFactory = (SqlSessionFactory) applicationContext.getBean(String.format("%sSqlSessionFactory", strKey));
			par.put("strSysSqlKey", sql);
			List<?> ll = yshSqlSessionFactory.openSession().selectList("www.kiy.cn.dao.saas.SaaS.getDataByMethod", par);
			return ll;
		} else {

			// 业务级别
			List<?> l = null;
			switch (type) {
			case JdbcTemplate:
				l = mssql.getDataByJdbcTemplate(strServerName, strDBName, strUserID, strPassword, sql, sqlCount, par);
				break;
			case Mybatis:
				par.put("strSysSqlKey", sql);
				l = systemDao.getDataByMethod(par);

				break;
			case Jdbc:
				if (bPage)
					sqlCount += " select ?, ?, ?, ? ";
				l = mssql.getDataByJDBC(strServerName, strDBName, strUserID, strPassword, sql,sqlCount,
						par);
				break;
			} 
			return l;
		}

	}
}
