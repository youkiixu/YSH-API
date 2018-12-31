package www.kiy.cn.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import www.kiy.cn.HotKey;
import www.kiy.cn.HotKey.eSqlType;
import www.kiy.cn.dao.saas.SaaS;
import www.kiy.cn.service.MssqlService;
import www.kiy.cn.service.SaaSService;
import www.kiy.cn.service.SaveService;
import www.kiy.cn.service.QueryService;
import www.kiy.cn.youki.CacheInfo;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.Pub;
import www.kiy.cn.youki.SetLog;

@Service
public class SaaSServiceImpl implements SaaSService {
	@Autowired
	 private SaveService saveService;
	@Autowired
	private MssqlService mssql;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private CacheInfo cacheInfo;
	@Resource
	SaaS systemDao;
	@Autowired
	private QueryService systemService;

	public JMap getAppConfig(String appid) {
		return getAppConfig(appid, null);
	}

	public JMap getAppConfig(String strAppid, String strDomain) {

		JMap map = cacheInfo.getMap(HotKey.mDomainConfig);
		if (map == null) {
			map = new JMap();
		}

		JMap configInfo = map.getMap(strAppid != null ? strAppid : strDomain);

		 if (configInfo == null)
		{
			JMap m = new JMap();
			m.put("appid", strAppid);
			m.put("domain", strDomain);

			List<JMap> lst = systemDao.SysAppConfigInfo(m);
			if (lst.size() == 0)
				return SetLog.writeMapError("找不到对应配置信息");
			configInfo = lst.get(0);
			map.put(strAppid != null ? strAppid : strDomain, configInfo);
			cacheInfo.putJMap(HotKey.mDomainConfig, map);
		}
		return configInfo;
	}

	@Override
	public Object getSaaSData(String strAppid, JMap map) throws Exception {
		return getSaaSData(strAppid, map, eSqlType.Jdbc);
	}

	@Override
	public Object getSaaSData(String strAppid, JMap map, eSqlType type) throws Exception {

		// TODO Auto-generated method stub
		JMap param = null;

		if (map.get("strMethod") != null) {
			String strMethod = map.get("strMethod").toString();
			if (map.containsKey("param"))
				param = SetLog.ObjectToMap(map.get("param")); 
			Object obj = systemService.getDataTableBySystemDao(this.getAppConfig(strAppid), strMethod, param, type);

			if (obj.getClass().getName().equals("JMap")) {
				return obj;
			}
			return SetLog.writeMapSuccess("Success", obj);
		} else if (map.get("uid") != null || map.get("key") != null) {
			param = new JMap();
			JMap mp = new JMap();
			if (map.containsKey("param")) {

				mp = SetLog.ObjectToMap(map.get("param"));
				if (!mp.containsKey("strAppid"))
					mp.put("strAppid", strAppid);
				// param.put("intSysProject", intSysProject);
			} else {
				mp.put("strAppid", strAppid);
			}
			param.put("param", mp);

			String uid = Convert.ToString(map.get("uid"));
			String key = Convert.ToString(map.get("key"));
			String cKey = uid == null ? String.format("%s%s", strAppid, key) : uid;
			JMap dt = SetLog.ObjectToMap(HotKey.mSysInvokeMethod);
			if (dt == null) {
				dt = new JMap();
			}
			JMap InfoMethod;
			if (dt.containsKey(cKey)) {
				InfoMethod = SetLog.ObjectToMap(dt.get(cKey)); // xxxxxx
			} else {
				List<JMap> lst = systemService.getDataTableByMethod(HotKey.QgetSysInvokeMethod, map, type);

				if (lst.size() == 0)
					return SetLog.writeMapError("方法主键不存在");

				InfoMethod = lst.get(0);
				dt.put(cKey, InfoMethod);
				cacheInfo.putJMap(HotKey.mSysInvokeMethod, dt);
			}

			Object oo = Pub.getInstance().InvokeMethod(applicationContext,
					Convert.ToString(InfoMethod.get("strFilePath")), InfoMethod.get("strPackage").toString(),
					InfoMethod.get("strClassName").toString(), InfoMethod.get("strMethod").toString(), param);
			return oo;
		}
		return null;
	}

	@Override
	public List<List<JMap>> getDataSetByMethod(String strAppid, String strMethod, JMap param) throws Exception {
		// TODO Auto-generated method stub
		JMap config = systemService.getDBConfig(strAppid, true);
		List<List<JMap>> lst = systemService.getDataSetByMethod(config, strMethod, param);
		return lst;
	}

	@Override
	public List<JMap> getDataTableByMethod(String strAppid, String strMethod, JMap param) throws Exception {
		JMap config = this.getAppConfig(strAppid);
		List<JMap> lst = systemService.getDataTableByMethod(config, strMethod, param);
		return lst;
	}
	/**
	 * 不推荐使用
	 */
	@Override
	public List<JMap> getDataTableByMethodByCache(String uid,String strAppid, String strMethod, JMap param) throws Exception {
		//String key = SetLog.GetJSONString(param);
		return getDataTableByMethodByCache( uid, strMethod, strAppid,  strMethod,  param);
	} 
	@Override
	public List<JMap> getDataTableByMethodByCache(String uid,String key,String strAppid, String strMethod, JMap param) throws Exception {
		// TODO Auto-generated method stub
		List<JMap> lst = cacheInfo.getListFromMap(key, uid);
		if(lst==null){
			lst = new ArrayList<JMap>(); 
		}
		
		if (lst.size() > 0)
			return lst;
		
		List<JMap> lst1 = this.getDataTableByMethod(strAppid, strMethod, param);
		if(lst1.size()>0){
			lst.addAll(lst1);
			cacheInfo.putListToMap(key, uid, lst);
		}
		return lst1;
	}
	

	/**
	 * 
	 * @param strAppid
	 * @param tblName
	 *            表名
	 * @param data
	 *            数据
	 * @return
	 * @throws Exception 
	 */
	@Override
	public JMap tbSaaSSave(String strAppid, String tblName, JMap data) throws Exception {
		List<JMap> lst = new ArrayList<JMap>();
		lst.add(data);
		return tbSaaSSave(strAppid, tblName, lst);
	}
	@Override
	public JMap tbSaaSSave(String strAppid, String tblName, List<JMap> data) throws Exception {
		JMap map = new JMap();
		map.put(tblName, data);
		return tbSaaSSaveForList(strAppid, map);
	}

	/**
	 * 不做委托特殊业务保存自行解决
	 * 
	 * @param strAppid
	 * @param data
	 *            Map<String,List<JMap>> String=tblName,List<JMap>=data
	 * @return
	 * @throws Exception 
	 */
	@Override
	public JMap tbSaaSSaveForList(String strAppid, JMap data) throws Exception {
		return tbSaaSSaveForList(strAppid, data, eSqlType.Jdbc);
	}
	@Override
	public JMap tbSaaSSaveForList(String strAppid, JMap data, eSqlType type) throws Exception {

	 
		return tbSaaSSave( strAppid,null, data,null,null);
	}
	@Override
	public JMap tbSaaSSave(String strAppid, String returnID_tblName, JMap data, String tblHeadName, String tblColumns)
			throws Exception {
		JMap relation = new JMap();
		if(tblHeadName!=null){
			relation.put(tblHeadName, tblColumns);
		} 
		return tbSaaSSave(strAppid, null, returnID_tblName, data, relation);
	}
	@Override
	public JMap tbSaaSSave(String strAppid, String strDBName, String returnID_tblName, JMap data, JMap relation)
			throws Exception {
		// TODO Auto-generated method stub

		JMap config = systemService.getDBConfig(strAppid, strDBName, true);

		return tbSaaSSave( config,  returnID_tblName,  data,  relation);
	}
	
	public JMap tbSaaSSave(JMap config,String returnID_tblName, JMap data, JMap relation)
			throws Exception {
		  return tbSaaSSave( config, returnID_tblName,  data,  relation,eSqlType.Jdbc);
	}
	
	@Override
	public JMap tbSaaSSave(JMap config,String returnID_tblName, JMap data, JMap relation,eSqlType type)
			throws Exception {
		// TODO Auto-generated method stub
		
		if (data == null || data.size() == 0)
			return SetLog.writeMapError("无效数据");
		
		Map<String,List<JMap>> dt = new HashMap<String,List<JMap>>();
		Map<String,String[]> dicRelation= new HashMap<String,String[]>();
		int i = 0;
		if (relation != null && relation.size() > 0) {
		
			Iterator<String> e = relation.keySet().iterator();

			while (e.hasNext()) { 
				String tHName = e.next(); // LogisticsOrder
				String tBName = relation.get(tHName).toString();
				if (Convert.isNullOrEmpty(tHName))
					return SetLog.writeMapError(String.format("保存失败; 第[%s]表,未赋予名称", i + 1));
				List<JMap> lst = SetLog.ObjectToListMap(data.get(tHName));
				if (lst == null || lst.size() == 0)
					return SetLog.writeMapError(String.format("表%s无数据", tHName));
				dt.put(tHName, lst);
				data.remove(tHName);
				String[] ts = tBName.split("-");
				for (int j = 0; j < ts.length; j++) {
					String[] ss = ts[j].split(":");//ts[j]=LogisticsOrder_List.LogisticsNO_PKey
					i++;// i=1
					List<JMap> l = SetLog.ObjectToListMap(data.get(ss[0]));
					if (l == null || l.size() == 0)
						return SetLog.writeMapError(String.format("表%s无数据", ss[0]));
					if (!l.get(0).containsKey(ss[1])) {
						l.forEach(tmp -> {
							if (!tmp.containsKey(ss[1]))
								tmp.put(ss[1], null); //增加LogisticsNO_PKey字段 
						}); 
					}
					dt.put(ss[0], l);
					data.remove(ss[0]); 
					dicRelation.put(ss[0], new String[]{tHName,ss[1]} );
					//表 LogisticsOrder_List:LogisticsOrder, LogisticsNO_PKey :
				}
			}
			i++;
		}
		Iterator<String> e2= data.keySet().iterator();
		while(e2.hasNext()){
			String  tName= e2.next();
			if(Convert.isNullOrEmpty(tName)){
				return SetLog.writeMapError(String.format("保存失败; 第[%s]表,未赋予名称",i+1));
			}
			List<JMap> lst = SetLog.ObjectToListMap(data.get(tName));
			if(lst==null || lst.size()==0)
				return SetLog.writeMapError(String.format("表%s无数据", tName));
			
			dt.put(tName,  lst);
			i++;
		}
		//  //表排序成 , LogisticsOrder , LogisticsOrder_List,LogisticsRecords
		Map<JMap, Map<String, Map<String, List<JMap>>>> cns=null;
		if( config!=null  ) {
			
			cns=saveService.tbSaaSSaveByJdbc(config, dicRelation, dt);	
		}
		
		return JDBCNonQuery(returnID_tblName,dicRelation,cns);
	}
	
	private  JMap JDBCNonQuery(String returnID_tblName,Map<String,String[]> dicRelation,Map<JMap, Map<String, Map<String, List<JMap>>>> cns) {
		Iterator<JMap> iterator = cns.keySet().iterator();
		Map<JMap, Connection> c = new HashMap<JMap, Connection>();
		//List<Integer> lst = new ArrayList<Integer>();
		JMap res = new JMap();
		JMap dd= new JMap();
		PreparedStatement  pst=null;
		
		boolean check = true;
		int intCount=0;
		try {
			while (iterator.hasNext()) {
				JMap config = iterator.next();// 链接配置信息
				Map<String, Map<String, List<JMap>>> tables = cns.get(config); // 表-->Sqls-->数据
																				// s
				Iterator<String> iTables = tables.keySet().iterator();
				Connection cn = this.mssql.getMssqlConnection(config);// 获取链接
				c.put(config, cn);
				cn.setAutoCommit(false);
				while (iTables.hasNext()) {
					StringBuilder str = new StringBuilder();
					String tblName = iTables.next();// 表名
					Map<String, List<JMap>> sqlAndData = tables.get(tblName);// Sqls-->数据s
					Iterator<String> sqls = sqlAndData.keySet().iterator();
					while (sqls.hasNext()) {
						String sql = sqls.next();/// Sql
						List<JMap> data = sqlAndData.get(sql);// 数据s
						if (tblName.equals(returnID_tblName) && sql.startsWith("INSERT")) {
							pst = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
						} else {
							if (sql.startsWith("INSERT") && dicRelation != null && dicRelation.containsKey(tblName)) {
								String[] s = dicRelation.get(tblName);
								Object v=dd.get(s[0]);
								data.forEach(tmp->{
									tmp.put(s[1], v);
								});
							}
							pst = cn.prepareStatement(sql);
						}
						
					
						
						pst.setQueryTimeout(data.size()*30);//每条数据最大执行时间为三十秒
						
						Iterator<JMap> ds = data.iterator();
						while (ds.hasNext()) {
							pst.clearParameters();
							JMap d = ds.next();// 单行数据
							Iterator<String> elements = d.keySet().iterator();
							int i = 0;
							while (elements.hasNext()) {
								i++;
								String element = elements.next();
								pst.setObject(i, d.get(element));
								
							}
							 
							intCount += pst.executeUpdate(); 
							if (tblName.equals(returnID_tblName) && sql.startsWith("INSERT")) {
								// pst.addBatch();

								ResultSet rs = pst.getGeneratedKeys();// .executeQuery();
								if (rs.next()) {
									str.append(rs.getInt(1)).append(" ");
								}
							}
						}
						pst.clearParameters();
						pst.close();
						pst = null;
						if (str.length() > 0)
							dd.put(tblName, str.toString().trim());
					}
				}
			}
		} catch (Exception ex) {
			check = false;
			SetLog.Info("JDBC","数据保存失败");
			SetLog.Info("JDBC", ex.toString());
			res = SetLog.writeMapError("操作失败",ex.toString());
		}
		try {
			if (pst != null && !pst.isClosed()) {
				pst.clearParameters();
				pst.clearBatch();
				pst.close();

			}
		} catch (Exception ex) {
			SetLog.Info("JDBC", "prepareStatement关闭失败");
			SetLog.Info("JDBC", ex.toString());
		}
		pst=null;
		Iterator<JMap> iterator1 = c.keySet().iterator();
		while (iterator1.hasNext()) {
			JMap config = iterator1.next();
			Connection cn = c.get(config);

			try {

				if (!check) {
					cn.rollback();
				} else {
					cn.commit();
					res = SetLog.writeMapSuccess("操作成功", dd);
					res.put("intEffect", intCount);
				}
			} catch (Exception ex) {
				SetLog.Info("JDBC", "资源提交或释放失败");
				SetLog.Info("JDBC", Pub.getInstance().getStackTrace(ex));
				SetLog.Info("JDBC", SetLog.GetJSONString(cns));
				res = SetLog.writeMapError("资源提交或释放失败", Pub.getInstance().getStackTrace(ex));

			}
			this.mssql.releaseCN(config, cn);
		}  
		return res;
	}  
}
