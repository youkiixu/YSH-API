package www.kiy.cn.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import www.kiy.cn.HotKey.sqlType;
import www.kiy.cn.dao.SystemDao;
import www.kiy.cn.service.MssqlService;
import www.kiy.cn.service.SystemService;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.YSHException;
@Service
public class SystemServiceImpl implements SystemService {
	@Resource
	private SystemDao systemDao;
	
	@Autowired
	private MssqlService mssql;
	
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
		@SuppressWarnings("unchecked")
		List<JMap> mm =(List<JMap>) getDataTableBySystemDao( config,strName, map);
		if (mm != null)
			System.out.println(mm);
		return mm;

	}

	public List<?> getDataTableBySystemDao(final JMap config,String strName, final JMap map) throws YSHException {
		return getDataTableBySystemDao(config,strName,map,sqlType.Jdbc);
		
	}
	/**
	 * 参数统一使用@进行方便操作,为兼容.net 
	 * 缺陷：strMethod 与strFrom 中对@处理不友好
	 * 
	 * @param config
	 * @param strName
	 * @param map
	 * @param type
	 * @return
	 * @throws YSHException
	 */
	public List<?> getDataTableBySystemDao( JMap config,String strName, final JMap map,sqlType type) throws YSHException {
	 
		List<List<JMap>> lst= systemDao.sysQueryAndFliter(strName); 
		if (lst == null || lst.get(0).size()==0)
			throw new YSHException("找不到对应方法"); 
		JMap SysQueryMethod =  lst.get(0).get(0);
		
		String strServerName=Convert.ToString( SysQueryMethod.get("strServerName"));
	//	String strInstanceName = Convert.ToString(SysQueryMethod.get("strInstanceName"));
		String strDBName = Convert.ToString(SysQueryMethod.get("strDBName"));
		String strUserID = Convert.ToString(SysQueryMethod.get("strUserID"));
		String strPassword = Convert.ToString(SysQueryMethod.get("strPassword"));
		if(config!=null){
			 strServerName=Convert.ToString( config.get("strServerName"));
			// strInstanceName = Convert.ToString(config.get("strInstanceName"));
			 strDBName = Convert.ToString(config.get("strDBName"));
			 strUserID = Convert.ToString(config.get("strUserID"));
			 strPassword = Convert.ToString(config.get("strPassword"));
		}
		
		String strMethod = SysQueryMethod.get("strMethod").toString();
		String strGroupBy = SysQueryMethod.get("strGroupBy").toString();
		String strFrom = SysQueryMethod.get("strFrom") == null ? "" : SysQueryMethod.get("strFrom").toString();
 
		StringBuilder str = new StringBuilder();

		StringBuilder sql = new StringBuilder(String.format("%s %s", strMethod, strFrom));
		Iterator<JMap> iterator = lst.get(1).iterator();
		JMap par = new JMap();

		while (iterator.hasNext()) {
			JMap tmp = iterator.next();
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
				
				boolean bIsNotNull = Convert.ToBoolean( SysQueryMethod.get("bIsNotNull")); 
				String strFliterParam2=strFliterParam;
				String strFliterParam1 = strFliterParam.replace("@", "$");
//				if (strFliterParam1.contains("#{")) {
//					strFliterParam1 = strFliterParam1.replace("#{", "@").replace("}", "");
//				}
				//strFliterParam=@userId  strFliterParam1=$userId
				if (bIsNotNull) {  
					if (map == null || (!map.containsKey(strFliterParam) && !map.containsKey(strFliterParam1)))
						throw new YSHException(String.format("关键参数%s必填", strFliterParam));
				}
				
				if (map != null && (map.containsKey(strFliterParam) || map.containsKey(strFliterParam1) )) {
					
					//处理值 将 @变成:参数名 或 参数名
					if (/*map.containsKey(strFliterParam) &&*/ Convert.ToString((map.get(strFliterParam))).equals("undefined")) {
						map.remove(strFliterParam);
						continue;
					}else if ( Convert.ToString(map.containsKey(strFliterParam1)).equals("undefined")) {
						map.remove(strFliterParam1);
						continue;
					}
					else {
						Object objVal; 
						if(map.containsKey(strFliterParam))
							objVal=map.getWithRemoveKey(strFliterParam);
						else
							objVal=map.getWithRemoveKey(strFliterParam1);
						switch(type){ 
						case Jdbc:
							strFliterParam2 ="?"; 
							break;
						case JdbcTemplate: 
							strFliterParam2=strFliterParam.replace("@",":");
							par.put(strFliterParam.replace('@',':' ) , objVal);
							break;
						case mybatis:
							strFliterParam2=String.format("#{%s}", strFliterParam.replace("@",""));
							par.put( strFliterParam.replace("@",""), objVal);
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
								throw new YSHException("过滤参数{%s}缺失");
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
				if (map.containsKey("@rowIndex"))
					rowIndex = map.getWithRemoveKey("@rowIndex");
			}
			if (map.containsKey("$pageRecord")) {
				pageRecord = map.getWithRemoveKey("$pageRecord");
			} else {
				if (map.containsKey("@pageRecord"))
					pageRecord = map.getWithRemoveKey("@pageRecord");
			}
			String sRowIndex="";
			String sPageRecord="";
			switch(type){ 
			case Jdbc:
				sRowIndex ="?"; 
				sPageRecord="?";
				break;
			case JdbcTemplate:  
				sRowIndex =":rowIndex"; 
				sPageRecord=":pageRecord";
				break;
			case mybatis:
				sRowIndex ="#{rowIndex}"; 
				sPageRecord="#{pageRecord}";
				break;
			}
			
			
			//par.put("rowIndex", rowIndex);
			//par.put("pageRecord", pageRecord);
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
			if(sqlType.mybatis!=type){
				strSysSql = strSysSql.replaceAll("#{rowIndex}", sRowIndex);
				strSysSql = strSysSql.replaceAll("#{pageRecord}", sPageRecord);
			}
			
		}
		System.out.println(strSysSql);
		if (Convert.isNullOrEmpty(strServerName) ) {	
			//系统级别
			par.put("strSysSqlKey", strSysSql); 
			return systemDao.getDataByMethod(par); 
		} else {
			//业务级别 
			 //mssql.getDataByMethod(config, strSysSql, par);
			return mssql.getDataByMethod(strServerName, strDBName, strUserID, strPassword, strSysSql, par);
		}
	}

}
