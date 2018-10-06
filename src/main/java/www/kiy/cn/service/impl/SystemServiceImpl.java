package www.kiy.cn.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import www.kiy.cn.dao.SystemDao;
import www.kiy.cn.service.JdbcService;
import www.kiy.cn.service.SystemService;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.YSHException;
@Service
public class SystemServiceImpl implements SystemService {
	@Resource
	private SystemDao systemDao;
	
	@Autowired
	private JdbcService jdbcService;
	
	@Override
	public List<JMap> getDataTableByMethod(String strName) throws YSHException {
 
		return getDataTableByMethod(strName, null);
	}

	@Override
	public List<JMap> getDataTableByMethod(String strName, JMap map) throws YSHException {

		return getDataTableByMethod(null, strName, map);

	}

	@Override
	public  List<List<JMap>> getDataSetByMethod(String strName) throws YSHException {
 
		return getDataSetByMethod(strName, null);
	}

	@Override
	public  List<List<JMap>> getDataSetByMethod(String strName, JMap map) throws YSHException {

		return getDataSetByMethod(null, strName, map);

	}
	public List<List<JMap>> getDataSetByMethod(JMap config, String strMethod, JMap param) throws YSHException{
		
		@SuppressWarnings("unchecked")
		 List<List<JMap>> mm =( List<List<JMap>>) getDataTableBySystemDao( config,strMethod, param);
		if (mm != null)
			System.out.println(mm);
		return mm;
	}
	
	@Override
	public List<JMap> getDataTableByMethod(JMap config, String strName, JMap map) throws YSHException {
	 //	List<List<JMap>> lst = systemDao.getSystemDataSetByName(strName);
		
//		DriverManagerDataSource dc= db.getMssQLDataSource("192.168.0.91", "SaaS", "dev", "dev");
//		try {
//			Connection cn= dc.getConnection();
//			PreparedStatement par=cn.prepareStatement("select * from SysQueryMethod with(nolock) where strName=:strName "
//					+ "select f.* from SysSqlFliter f with(nolock) inner join SysQueryMethod where s.strName=:strName ");
//			
//			par.close();
//			
//			cn.close();
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		@SuppressWarnings("unchecked")
		List<JMap> mm =(List<JMap>) getDataTableBySystemDao( config,strName, map);
		if (mm != null)
			System.out.println(mm);
		return mm;

	}

	public List<?> getDataTableBySystemDao(final JMap config,String strName, final JMap map) throws YSHException {
		
		List<List<JMap>> lst= systemDao.SysQueryAndFliter(strName);
		System.out.println(lst);
		if (lst == null || lst.get(0).size()==0)
			throw new YSHException("找不到对应方法"); 
		JMap SysQueryMethod =  lst.get(0).get(0);
		
		String strServerName=Convert.ToString( SysQueryMethod.get("strServerName"));
		String strInstanceName = Convert.ToString(SysQueryMethod.get("strInstanceName"));
		String strDBName = Convert.ToString(SysQueryMethod.get("strDBName"));
		String strUserID = Convert.ToString(SysQueryMethod.get("strUserID"));
		String strPassword = Convert.ToString(SysQueryMethod.get("strPassword"));
		if(config!=null){
			 strServerName=Convert.ToString( config.get("strServerName"));
			 strInstanceName = Convert.ToString(config.get("strInstanceName"));
			 strDBName = Convert.ToString(config.get("strDBName"));
			 strUserID = Convert.ToString(config.get("strUserID"));
			 strPassword = Convert.ToString(config.get("strPassword"));
		}
		
		String strMethod = SysQueryMethod.get("strMethod").toString();
		String strGroupBy = SysQueryMethod.get("strGroupBy").toString();
		String strFrom = SysQueryMethod.get("strFrom") == null ? "" : SysQueryMethod.get("strFrom").toString();

		// if( !strCon.isEmpty()){

		// }
		StringBuilder str = new StringBuilder();

		StringBuilder sql = new StringBuilder(String.format("%s %s", strMethod, strFrom));
		Iterator<JMap> iterator = lst.get(1).iterator();
		JMap par = new JMap();

		while (iterator.hasNext()) {
			JMap tmp = iterator.next();
			String strFliterParam = tmp.get("strFliterParam").toString();

			String strColumn = String.valueOf(tmp.get("strColumn"));
			strColumn = strColumn == null ? "" : strColumn;

			String strValue = String.valueOf(tmp.get("strValue"));
			strValue = strValue == null ? "" : strValue;

			String strRule = String.valueOf(tmp.get("strRule"));
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
				boolean bIsNotNull = SysQueryMethod.get("bIsNotNull") == null ? false
						: Boolean.valueOf(SysQueryMethod.get("bIsNotNull").toString());
				String strFliterParam1 = strFliterParam;
				if (strFliterParam1.contains("#{")) {
					strFliterParam1 = strFliterParam1.replace("#{", "@").replace("}", "");
				}

				if (bIsNotNull) {

					if (map == null
							|| (!map.containsKey(strFliterParam) && !map.containsKey(strFliterParam.replace("@", "$"))))
						throw new YSHException(String.format("关键参数%s必填", strFliterParam));
				}
				if (map != null && (map.containsKey(strFliterParam) || map.containsKey(strFliterParam.replace("@", "$"))
						|| map.containsKey(strFliterParam.replace("#{", "$").replace("}", "")))) {
					if (map.containsKey(strFliterParam)
							&& String.valueOf(map.get(strFliterParam)).equals("undefined")) {
						map.remove(strFliterParam);
						continue;
					} else { 
						if (map.containsKey(strFliterParam))
							par.put(strFliterParam.replace("@", ""), map.get(strFliterParam));
						else {
							Object objVal_ = map.get(strFliterParam1);// ==
																		// "undefined"
							map.remove(strFliterParam1);
							if (String.valueOf(objVal_) == "undefined") {
								continue;
							}
							String key1 = strFliterParam.replace("$", ""); 
							if (strFliterParam.contains("#")) {
								key1 = strFliterParam.replace("#{", "").replace("}", "");
							}
							par.put(key1, map.get(strFliterParam));
						}

					}
					if (strFliterParam.startsWith("@"))
						strFliterParam = String.format("%s}", strFliterParam.replace("@", "#{"));
					else if (!strFliterParam.startsWith("#{"))
						strFliterParam = String.format("#{%s}", strFliterParam);
					if (strColumn == "")
						str.append(String.format(" %s ", strValue));
					else {
						str.append(String.format(" %s ", strColumn));
						if (strRule.toUpperCase() == "LIKE")
							str.append(String.format(" like '%'+%s+'%'", strFliterParam));
						else if (strRule.toUpperCase() == "LIKE%")
							str.append(String.format(" like %s+'%'", strFliterParam));
						else if (strRule.toUpperCase() == "%LIKE")
							str.append(String.format(" '%'+like %s", strFliterParam));

						else if (strRule.toUpperCase().contains("in")) {
							if (!map.containsKey(strFliterParam))// 必须由内部数据统一过滤
								throw new YSHException("过滤参数{%s}缺失");
							str.append(
									String.format(" %s (%s) ", strRule, strValue.replace("or", "").replace("=", "")));

						} else {
							str.append(String.format(" %s %s  ", strRule, strFliterParam));
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
		String strSysSql = "";
		if (!Boolean.valueOf(SysQueryMethod.get("bPage").toString())) {
			sql.append(condition).append(" ").append(strGroupBy);
			strSysSql = sql.toString();
		} else {
			// if (map.containsKey("$rowIndex") && map.containsKey("@rowIndex"))
			// map.remove("$rowIndex");

			// if (map.containsKey("$pageRecord") &&
			// map.containsKey("@pageRecord"))
			// map.remove("$pageRecord");
			// if (map.containsKey("$rowIndex") &&
			// map.get("$rowIndex").toString().equals("undefined") )
			// map.remove("$rowIndex");
			// if (map.containsKey("@rowIndex") &&
			// map.get("@rowIndex").toString().equals("undefined") )
			// map.remove("@rowIndex");

			// if (map.containsKey("$$pageRecord") &&
			// map.get("$$pageRecord").toString().equals("undefined") )
			// map.remove("$$pageRecord");
			// if (map.containsKey("@$pageRecord") &&
			// map.get("@$pageRecord").toString().equals("undefined") )
			// map.remove("@$pageRecord");
			Object rowIndex = 1;
			Object pageRecord = 15;
			if (map.containsKey("$rowIndex")) {
				rowIndex = map.getWithRemoveKey("$rowIndex");
			} else {
				if (!map.containsKey("@rowIndex"))
					rowIndex = map.getWithRemoveKey("@rowIndex");
			}
			if (map.containsKey("$pageRecord")) {
				pageRecord = map.getWithRemoveKey("$pageRecord");
			} else {
				if (!map.containsKey("@pageRecord"))
					pageRecord = map.getWithRemoveKey("@pageRecord");
			}
			par.put("rowIndex", rowIndex);
			par.put("pageRecord", pageRecord);
			String sqlCount = "";
			if (strFrom == null || strFrom.isEmpty()) {
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
				sql = sql.insert(roNumIndx, String.format("  top %s   ",  Convert.ToInt32( rowIndex) *  Convert.ToInt32(pageRecord) ));
			strSysSql = String.format(
					" select * from ( %s  %s %s) t where t.rowNum>=(#{rowIndex}-1)*convert(int,#{pageRecord})+1 and t.rowNum<=#{rowIndex}*convert(int,#{pageRecord}) \n %s",
					sql.toString(), condition.toString(), strGroupBy, sqlCount);
					// if (Boolean.valueOf(
					// SysQueryMethod.get("bNonQuery").toString())){

			// }else{

			// }
		}
		if (Convert.isNullOrEmpty(strServerName) ) {
			par.put("strSysSqlKey", strSysSql);
		  return systemDao.getDataByMethod(par);
		} else { 
			//  return jdbcService.exeReader(strCon, strSysSql, par);
			return null;
		}
	}

}
