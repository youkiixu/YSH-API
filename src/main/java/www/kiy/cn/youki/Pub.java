package www.kiy.cn.youki;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.context.ApplicationContext;

import www.kiy.cn.HotKey.eCmdType;

public class Pub {
	private static final Pub pub = new Pub();
	
	public  static Pub getInstance(){
		return pub;
	}
	
	private final JMap InstanceMap = new JMap();
	private final JMap InstanceObjMap = new JMap();
	private final JMap InstanceMethodMap = new JMap();
	
	public List<String> getClassName(String packageName) {

		String filePath = ClassLoader.getSystemResource("").getPath() + packageName.replace(".", "\\");
		List<String> fileNames = getClassName(filePath, null);
		return fileNames;
	}

	public List<String> getClassName(String filePath, List<String> className) {
		List<String> myClassName = new ArrayList<String>();
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				myClassName.addAll(getClassName(childFile.getPath(), myClassName));
			} else {
				String childFilePath = childFile.getPath();
				childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9,
						childFilePath.lastIndexOf("."));
				childFilePath = childFilePath.replace("\\", ".");
				myClassName.add(childFilePath);
			}
		}
		return myClassName;
	}

	public Object GetInstance(String strPackage, String strClassName) throws Exception {
		return GetInstance(null, strPackage, strClassName);
	}

	public Object GetInstance(String strFilePath, String strPackage, String strClassName) throws Exception {
		String key = String.format("%s.%sInstance", strPackage, strClassName);

		 if (InstanceMap.containsKey(key))
			 return InstanceMap.get(key);

		Class<?> cls = GetClass(strFilePath, strPackage, strClassName);
		if (cls == null)
			return cls;
		try {
			Object obj = cls.newInstance();
			InstanceMap.put(key, obj);

			return obj;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Object InvokeMethod(String strPackage, String strClassName, String strMethod, Object json) {
		return InvokeMethod(null, strPackage, strClassName, strMethod, json);
	}
	public Object InvokeMethod( String strFilePath, String strPackage, String strClassName, String strMethod, Object json)  {
		return InvokeMethod(null,strFilePath, strPackage, strClassName, strMethod, json);
	}
	public Object InvokeMethod(ApplicationContext applicationContext,String strFilePath, String strPackage, String strClassName, String strMethod, Object json)  {
		// if(strPackage.isEmpty())
		// return setLog.writeMapError("包名不能为空");
		// if(strClassName.isEmpty())
		// return setLog.writeMapError("类名不能为空");
		// if(strMethod.isEmpty())
		// return setLog.writeMapError("方法名不能为空");

		try {
			String key = String.format("%s.%s.%sInstanceMethod", strPackage, strClassName, strMethod);
			Class<?> cls = GetClass(strFilePath, strPackage, strClassName);
			Object obj = null;
			if (InstanceObjMap.containsKey(key))
				obj = InstanceObjMap.get(key);
			else {

				if (applicationContext != null)
					obj = applicationContext.getBean(cls);// 单例
				else {
					obj = cls.newInstance();// 非单例
				}
				InstanceObjMap.put(key, obj);
			}
			if (cls == null) {
				return SetLog.writeMapError("找不到对应类型信息");
			}
			Method method = null;
			if (InstanceMethodMap.containsKey(key))
				method = (Method) InstanceMethodMap.get(key);
			else {
				method = cls.getMethod(strMethod, json.getClass());
				InstanceMethodMap.put(key, method);
			}
			return method.invoke(obj, json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return SetLog.writeMapError("找不到对应类型信息", getStackTrace(e.fillInStackTrace()));
		}
	}
	
	public  String getStackTrace(Throwable aThrowable) {        
		 Writer result = new StringWriter();
		 PrintWriter printWriter = new PrintWriter(result);  
		 aThrowable.printStackTrace(printWriter);  
			 
		 
		printWriter.close(); 
		try {
			result.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.toString();  
		
	}
 

	public Class<?> GetClass(String strFilePath, String strPackage, String strClassName) throws Exception {
		if (strPackage.isEmpty() || strClassName.isEmpty())
			return null;
		String strClass = String.format("%s.%s", strPackage, strClassName);

		// try {
		Class<?> cls = null;
		if (Convert.isNullOrEmpty(strFilePath) ) {
			cls = Class.forName(strClass);
		} else {
			System.out.println(String.format("寻找%s文件", strFilePath));
			File file = new File(strFilePath);
			if (file.exists())
				throw new Exception(String.format("找不到对应文件%s", strFilePath));
			URL url = file.toURI().toURL();
			ClassLoader loader = new URLClassLoader(new URL[] { url });// 创建类载入器
			cls = Class.forName(strClass, true, loader);

		}
		return cls;
	}

	public Properties getPropertiesInfo(String strResourcePath) {
		Properties props = new Properties();
		InputStream inStream = null;
		try {
			inStream = this.getClass().getResourceAsStream(strResourcePath);
			props.load(inStream);
			props.clear();
		} catch (Exception ex) {

		}
		try {
			if (inStream != null) {
				inStream.close();
				inStream = null;
			}
		} catch (Exception ex) {
		}
		return props;
	}
	
	
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
					if (!Convert.isNullOrEmpty(rowState)) {

						switch (rowState) {
						case "A":
							t = eCmdType.insert;
							break;
						case "D":
							t = eCmdType.delete;
							break;
						case "M":
							t = eCmdType.update;
							break;

						}
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
					if (!Convert.isNullOrEmpty(rowState)) {

						switch (rowState) {
						case "A":
							t = eCmdType.insert;
							break;
						case "D":
							t = eCmdType.delete;
							break;
						case "M":
							t = eCmdType.update;
							break;

						}
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
					if (!Convert.isNullOrEmpty(rowState)) {

						switch (rowState) {
						case "A":
							t = eCmdType.insert;
							break;
						case "D":
							t = eCmdType.delete;
							break;
						case "M":
							t = eCmdType.update;
							break;

						}
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
							SET(String.format("%s=?", col));
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
}
