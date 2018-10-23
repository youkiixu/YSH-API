package www.kiy.cn.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import www.kiy.cn.HotKey;
import www.kiy.cn.HotKey.eCmdType;
import www.kiy.cn.HotKey.eSqlType; 
import www.kiy.cn.service.SaveService;
import www.kiy.cn.service.SystemService;
import www.kiy.cn.youki.CacheInfo;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.SetLog;

@Service
public class SaveServiceImpl implements SaveService {
	@Autowired
	private SystemService systemService;
	
	@Autowired CacheInfo cacheInfo;
	
	public String PrepareSaveSqlByMybatis(String tblName, final JMap map, eCmdType cmdType) {

		return PrepareSaveSqlByMybatis(tblName, map, cmdType);
	}

	/**
	 * 
	 * @param tblName
	 * @param map
	 * @param cmdType
	 * @param condition
	 *            操作类型为update 时不能为空
	 * @return
	 */
	public String PrepareSaveSqlByMybatis(String tblName, final JMap map, eCmdType cmdType, JMap condition) {

		String strSql = null;
		if (map.containsKey("strSysSqlKey"))
			strSql = map.getWithRemoveKey("strSysSqlKey").toString();
		else {
			strSql = new SQL() {
				{
					String rowState=Convert.ToString( map.getWithRemoveKey("rowState"));
					eCmdType t = cmdType;
					switch (rowState) {
					case "A":
						t = eCmdType.insert;
					case "D":
						t = eCmdType.delete;
						break;
					case "M":
						t = eCmdType.update;
						break;

					}
					Iterator<String> iterator = map.keySet().iterator();
					switch (t) {
					case insert:
						INSERT_INTO(tblName);
						while (iterator.hasNext()) {
							String col = iterator.next();
							INTO_COLUMNS(col);
							INTO_VALUES(String.format("#{%s}", col));
						}
						break;
					case delete:
						DELETE_FROM(tblName);
						while (iterator.hasNext()) {
							String col = iterator.next();
							WHERE(String.format("%s=#{%s}", col, col));
						}
						break;
					case update:
						UPDATE(tblName);
						while (iterator.hasNext()) {
							String col = iterator.next();
							SET(String.format("%s=#{%s}", col, col));
						}
						if (condition == null || condition.size() == 0)
							WHERE("1!=1");
						else {
							Iterator<String> cd = condition.keySet().iterator();
							while (cd.hasNext()) {
								String col = cd.next();
								WHERE(String.format("%s=#{%s}", col, col));
							}
						}
						break;
					default:
						break;
					}
				}
			}.toString();
		}
		return strSql;
	}

	public String PrepareSaveSqlByJdbcTemplate(String tblName, final JMap map, eCmdType cmdType) {

		return PrepareSaveSqlByJdbcTemplate(tblName, map, cmdType);
	}

	/**
	 * 
	 * @param tblName
	 * @param map
	 * @param cmdType
	 * @param condition
	 *            操作类型为update 时不能为空
	 * @return
	 */
	public String PrepareSaveSqlByJdbcTemplate(String tblName, final JMap map, eCmdType cmdType, JMap condition) {

		String strSql = null;
		if (map.containsKey("strSysSqlKey"))
			strSql = map.getWithRemoveKey("strSysSqlKey").toString();
		else {
			strSql = new SQL() {
				{
					String rowState=Convert.ToString( map.getWithRemoveKey("rowState"));
					eCmdType t = cmdType;
					switch (rowState) {
					case "A":
						t = eCmdType.insert;
					case "D":
						t = eCmdType.delete;
						break;
					case "M":
						t = eCmdType.update;
						break;

					}
					Iterator<String> iterator = map.keySet().iterator();
					switch (t) {
					case insert:
						INSERT_INTO(tblName);
						while (iterator.hasNext()) {
							String col = iterator.next();
							INTO_COLUMNS(col);
							INTO_VALUES(String.format(":%s", col));
						}
						break;
					case delete:
						DELETE_FROM(tblName);
						while (iterator.hasNext()) {
							String col = iterator.next();
							WHERE(String.format("%s=:%s", col, col));
						}
						break;
					case update:
						UPDATE(tblName);
						while (iterator.hasNext()) {
							String col = iterator.next();
							SET(String.format("%s=:%s", col, col));
						}
						if (condition == null || condition.size() == 0)
							WHERE("1!=1");
						else {
							Iterator<String> cd = condition.keySet().iterator();
							while (cd.hasNext()) {
								String col = cd.next();
								WHERE(String.format("%s=:%s", col, col));
							}
						}
						break;
					default:
						break;
					}
				}
			}.toString();
		}
		return strSql;
	}
 
	/**
	 * 
	 * @param tblName
	 * @param map
	 * @param cmdType
	 * @param condition
	 *            操作类型为update 时不能为空
	 * @return
	 */
	public String PrepareSaveSqlByJdbc(String tblName, final JMap map, eCmdType cmdType, JMap condition) {

		String strSql = null;
		if (map.containsKey("strSysSqlKey"))
			strSql = map.getWithRemoveKey("strSysSqlKey").toString();
		else {
			strSql = new SQL() {
				{
					String rowState=Convert.ToString( map.getWithRemoveKey("rowState"));
					eCmdType t = cmdType;
					switch (rowState) {
					case "A":
						t = eCmdType.insert;
					case "D":
						t = eCmdType.delete;
						break;
					case "M":
						t = eCmdType.update;
						break;

					}
					Iterator<String> iterator = map.keySet().iterator();
					switch (t) {
					case insert:
						INSERT_INTO(tblName);
						while (iterator.hasNext()) {
							String col = iterator.next();
							INTO_COLUMNS(col);
							INTO_VALUES("?");
						}
						break;
					case delete:
						DELETE_FROM(tblName);
						while (iterator.hasNext()) {
							String col = iterator.next();
							WHERE(String.format("%s=?", col));
						}
						break;
					case update:
						UPDATE(tblName);
						while (iterator.hasNext()) {
							String col = iterator.next();
							SET(String.format("%s=#{%s}", col, col));
						}
						if (condition == null || condition.size() == 0)
							WHERE("1!=1");
						else {
							Iterator<String> cd = condition.keySet().iterator();
							while (cd.hasNext()) {
								String col = cd.next();
								WHERE(String.format("%s=?", col));
							}
						}
						break;
					default:
						break;
					}
				}
			}.toString();
		}
		return strSql;
	}
	@Override
	public JMap tbSave(JMap data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JMap tbSave(JMap config, JMap data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JMap tbSave(JMap config, JMap data, eSqlType type) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * JDBC 形式进行数据保存
	 * 
	 * dicRelation=LogisticsOrder,LogisticsNO_PKey 
	 * @throws Exception 
	 */
	@Override
	public JMap tbSaveByJDBC(JMap config, Map<String, String[]> dicRelation, Map<String, List<JMap>> data) throws Exception {
		// TODO Auto-generated method stub
		String strServerName = Convert.ToString(config.get("strServerName"));
        String strDBName = Convert.ToString(config.get("strDBName"));
        String strUserID = Convert.ToString(config.get("strUserID"));
        String strPassword =Convert.ToString(config.get("strPassword"));  
        String strKey =Convert.ToString(config.get("strKey"));
        //链接数 
        //数据格式-->库-->表-->数据
        
       // int i=0;
        Iterator<String> e=  data.keySet().iterator();
        // key:连接池：value={ key:表名: value={key:sql ,value=List<JMap> }  }
        Map<JMap, Map<String,Map<String,List<JMap>>> >  cns = new HashMap<JMap,Map<String,Map<String,List<JMap>>>>();
       
        
        while(e.hasNext()){
        	String tblName =  e.next();
        	List<JMap> dt= data.get(tblName);
        	//List<JMap> newdt= new ArrayList<JMap>();
        	
        	JMap cn = config;
			if (tblName.contains("..")) {
				String strAppid = null;
				if (cn != null && cn.size() > 0)
					strAppid = Convert.ToString(cn.get("strAppid"));
				String serverName = null;
				if (tblName.contains("[##]")) {
					String[] server = tblName.split("[##]");
					serverName = server[0];
					tblName = server[1]; // YSH_YJ..tblName
				}
				String[] str = tblName.split("..");
				cn = this.systemService.getDBConfig(strAppid, strDBName, (strAppid == null ? true : false));// getDBConfig(strAppid,serverName,
																												// str[0]);
				strServerName = Convert.ToString(cn.get("strServerName"));
				strDBName = Convert.ToString(cn.get("strDBName"));
				strUserID = Convert.ToString(cn.get("strUserID"));
				strPassword = Convert.ToString(cn.get("strPassword"));
				strKey = Convert.ToString(cn.get("strKey"));
				tblName = str[1];
			}
			List<JMap> dtColumns = getTableColumns(cn, tblName);//获取表字段
			//JMap primaryKey = new JMap();
			List<JMap> lstPrimaryKey = new ArrayList<JMap>();
			dtColumns.forEach(tmp->{
				//支持多主键
				if(Convert.ToBoolean(tmp.get("isPrimary"))){
					lstPrimaryKey.add(tmp);
				}
			});
		
			if(lstPrimaryKey.size()==0)
				return SetLog.writeMapError(String.format("表%s不存在主键", tblName));
			 //检验并且删除不必要字段
			Iterator<JMap> iterator= dt.iterator();//数据
			//List<String> lstCols = new ArrayList<String>();
			int j=0;
			Map<String,Map<String,List<JMap>>> tables = null;
			if(cns.containsKey(config)){
				tables= cns.get(tables); 
			}else{
				tables=new HashMap<String,Map<String,List<JMap>>>(); 
			}
			String sqlInsert=null;
			String sqlDelete=null;
			String sqlUpdate=null;
			
			List<JMap> insert= new ArrayList<JMap>();
			List<JMap> update= new ArrayList<JMap>();
			List<JMap> delete= new ArrayList<JMap>();
			
			while (iterator.hasNext()) {
				JMap map = iterator.next();// 单行数据
				j+=1;
				//JMap newmap = new JMap(); // 新单行数据
				
				Iterator<String> cols = map.keySet().iterator();// 数据字段列
				
				while (cols.hasNext()) {
					String colName = cols.next();
//					if(lstCols.contains(colName))
//						continue;
//					lstCols.add(colName);
					
					
					Object val = map.get(colName);
					if (colName.equals("rowState")){
					//	newmap.put(colName,val);
						continue;
					}
						
					if (dicRelation != null && dicRelation.containsKey(tblName)) {
						String strTblColumn = String.format("{0}.{1}", tblName, colName);
						String strTblColumn1 = String.format("{0}.{1}", tblName,
								((String[]) dicRelation.get(tblName))[1]);
						if (strTblColumn.equals(strTblColumn1)){
							//newmap.put(colName, val);
							continue;
						}	
					}
					// 删除不存在字段
					//boolean bRemove = true;
					JMap colInfo = null;
					Iterator<JMap> entityCols = dtColumns.iterator();
					while (entityCols.hasNext()) {
						JMap tmp = entityCols.next();
						if (colName.toUpperCase().equals( tmp.get("colName").toString().toUpperCase())) {
							//bRemove = false;
							colInfo = tmp;
							break;
						}
					}
					//不存在此字段
					if (colInfo==null) {
						cols.remove();
						continue;
					}
					//校验合法性
					JMap check= this.beforeSaveCheck(tblName,j,val ,colInfo);
					if(check.containsKey("errMsg"))
						return check;
					//newmap.putAll(check); //put(colName,check); 
				}

				boolean bExits=this.bExitsData(config, tblName, lstPrimaryKey, map); 
				if(!bExits){
					 if(Convert.ToString( data.get("rowState")).equals("D"))
						 return SetLog.writeMapError(String.format("{%s} the %d  data missing or deleted ", tblName,j)); 
					 if(sqlInsert==null){
						 sqlInsert= this.PrepareSaveSqlByJdbc(tblName, map, eCmdType.insert,null); 
					 }   
					 insert.add(map);
					//新增
				}else{
					// 修改或删除
					JMap cd = new JMap();
					Iterator<String> e1= map.keySet().iterator(); 
					Iterator<JMap> e2=lstPrimaryKey.iterator();
					 while( e2.hasNext()){
						 String col2= e2.next().get("colName").toString();
						 while(e1.hasNext()){
						 String col1= e1.next(); 
							 if(col1.equals(col2)){
								 cd.put(col2, map.get(col2));
								 break;
							 }
						 }
					 } 
					 if(cd.size()<lstPrimaryKey.size())
						 return SetLog.writeMapError(String.format("{%s} 第 {%d} 行,主键信息不完整，请检查数据是否正确 ", tblName,j));
					//根据rowState值是否为D来判断是否删除
					 if(  data.get("rowState").equals("D"))
					 {
						 if(sqlDelete==null){
							 sqlDelete=this.PrepareSaveSqlByJdbc(tblName, map, eCmdType.update,cd);
						 }
						 delete.add(map);
						 
					 }else{
						 if(sqlUpdate==null){
							 sqlUpdate = this.PrepareSaveSqlByJdbc(tblName, map, eCmdType.update,cd); 
						 }
						 update.add(map);
					 }
				}
				
				

//				Map<String, List<JMap>>  sqls=null;
//				if(tables.containsKey(tblName)){
//					sqls=tables.get(tblName); 
//				}else{
//					sqls = new HashMap<String,List<JMap>>(); 
//				}
//				sqls.put(tblName, );
//				
				//newdt.add(newmap);
			 
			}
		//	newdata.put(tblName, newdt);	
        }
        //JMap map = new JMap();
       // map.put("", "");
       // systemService.getDataTableBySystemDao(config,"", map);
        //saas.getDataTableByMethod(map);
        return null;
	}
	
	public JMap beforeSaveCheck(String tblName,int index,Object data,JMap col){
		String colName=col.get("colName").toString();
		boolean bIsNull = Convert.ToBoolean(col.get("bIsNull"));
        String dataType = col.get("dataType").toString();
        if (!bIsNull){
        	 if (Convert.ToInt32(col.get("isIdentity")) != 1 && data==null)
        		  return SetLog.writeMapError(String.format("操作失败，原因：数据存在空值;表：[%s]第(%d)行,字段[%s]不能为空",tblName,index , colName));
        	  if (!dataType.equals("text")  && !dataType.equals("ntext") &&!dataType.equals("bit")){
        		  int maxLength = 0;
        		  
        		   if (dataType.equals("uniqueidentifier")){ 
        			   if( Convert.ToString(data).isEmpty()){
        				   data=null;
        			   }
        			   //maxLength = 36;
        		   }else{
        			    if ((dataType.equals("datetime")  || dataType.equals("date") ) && Convert.isNullOrEmpty(Convert.ToString(data))){
        			    	 data=null;
        			    }
        			    maxLength = Convert.ToInt32(col.get("maxLength"));
        			    //if (val_ != null && val_.GetType().Name != "Boolean" && val_.ToString().Length > maxLength && maxLength != -1  )
        			     if(data!=null && !data.getClass().getSimpleName().equals("Boolean") && data.toString().length()>maxLength&& maxLength!=-1 )
        			    	 return SetLog.writeMapError(String.format("操作失败,原因:字段值过长;表：[%s]第(%d)行,字段[%s]，字段长度最大为%d",tblName,index,colName,maxLength));
        			     
        		   }
        	  }else{
        		  if (dataType.equals("bit"))
        			  if (Convert.ToString( data).equals("√"))
        				  data = true;
        	  }
        }
        JMap map = new JMap();
        map.put(colName,data);
		return map;
	}
	
	 public  List<JMap> getTableColumns(JMap config, String tableName) throws Exception{
		 
		 String uid =tableName;
		 if(config!=null){
			 uid = String.format("%s%s%s", config.get("strServerName"),config.get("strDBName") , tableName);
		 }
		 
		List<JMap> cols= cacheInfo.getListFromMap(HotKey.lstTableEntity, uid);
		if(cols==null){
			StringBuilder str = new StringBuilder();
	         if (tableName.contains("..")){ 
	        	  String[] arr = tableName.split( "..");
	              tableName = arr[1];
	              str.append(" use ").append(arr[0]); 
	         }    
	         str.append(" select distinct col.name colName, case when  col.prec=-1 then 4000 else col.prec end maxLength,col.isnullable bIsNull, \n");
	         str.append("case when exists(SELECT 1 FROM sysobjects where xtype='PK' and parent_obj=col.id and name in ( SELECT name FROM sysindexes WHERE indid in(  SELECT indid FROM sysindexkeys WHERE id = col.id AND colid=col.colid))) then 1 else 0 end isPrimary, \n");
	         str.append("COLUMNPROPERTY(col.id,col.name,'isIdentity') isIdentity, \n");
	         str.append("case when ty.name='sysname' then 'nvarchar' else ty.name end dataType,es.text colDefaultValue/*,p.value colDescript*/ \n");
	         str.append(" from  SysObjects tbl \n");
	         str.append("  left join syscolumns   col on tbl.id = col.id   \n");
	         str.append(" left join systypes ty on  col.xtype = ty.xtype \n");
	         str.append("  left join syscomments  es on col.cdefault =es.id \n");
	         //  str.Append("  left join sys.extended_properties p on p.major_id = col.id and p.minor_id=col.colid \n");
	         str.append(" where tbl.name =? \n");
	         
	         JMap par = new JMap(); 
	         par.put("tblName",tableName );
	         
	        @SuppressWarnings("unchecked")
			List<JMap> lst=(List<JMap>) this.systemService.QuerySql(config, str.toString(), par, eSqlType.Jdbc);
	        return lst; 
		}  
		 return null;
     }
	 
	 public boolean bExitsData(JMap config,String tblName,List<JMap> primaryKey,JMap data) throws Exception{
		 //新增
		 if(Convert.ToString( data.get("rowState")).equals("A"))
			 return false; 
		 
		 StringBuilder condition = new StringBuilder(); 
		 Iterator<String> e1= data.keySet().iterator(); 
		 Iterator<JMap> e2=primaryKey.iterator();
		 while( e2.hasNext()){
			 String col2= e2.next().get("colName").toString();
			 while(e1.hasNext()){
			 String col1= e1.next(); 
				 if(col1.equals(col2)){
					 condition.append(" and ").append(col1).append("=?");
					 break;
				 }
			 }
		 }
		 if(condition.length()==0)
			 return false; 
		 
		 
		 StringBuilder sql = new StringBuilder(); 
		 sql.append(" select top 1 1 from ").append(tblName).append(" where 1=1 "); 
		 sql.append(condition); 
		 @SuppressWarnings("unchecked")
		List<JMap> lst= (List<JMap>) this.systemService.QuerySql(config, sql.toString(), data);
		 if(lst.size()==0)
			 return false;
		 return true;
		// this.systemService.QuerySql(config, sql, par);
	 }
}
