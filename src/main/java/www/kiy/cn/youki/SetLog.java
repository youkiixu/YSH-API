package www.kiy.cn.youki;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetLog {

	public static Object lockObject = new Object();

	public static final Logger log = LoggerFactory.getLogger(SetLog.class);

	public static synchronized void logInfo(String strMsg) {
		log.info(strMsg);
	}

	public static JMap writeMapError(String msg) {
		return writeMapError(msg, null, 0);
	}
	public static JMap writeMapError(String msg,String strSysError) {
		return writeMapError(msg,strSysError, 0);
		
	}
	public static JMap writeMapError(String str, String sysError, int errCode) {
		JMap m = new JMap();
		m.put("result", "err");
		m.put("sysError", sysError);
		m.put("errMsg", str);
		if (errCode != 0)
			m.put("errCode", errCode); // 错误代码
		return m;
	}

	public static JMap writeMapSuccess(String msg) {
		return writeMapSuccess(msg, null);
	}

	public static JMap writeMapSuccess(String msg, Object data) {
		JMap m = new JMap();
		m.put("result", "ok");
		m.put("msg", msg);
		if (data != null)
			m.put("data", data);

		return m;
	}

	public static String GetJSONString(Object json) {

		try {
			if (json != null)
				return MapJson.getMapper().writeValueAsString(json);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JMap ObjectToMap(Object json) {

		return (JMap) ObjectToObject(json, JMap.class);
	}

	@SuppressWarnings("unchecked")
	public static List<JMap> ObjectToListMap(Object json) {
		return (List<JMap>) ObjectToObject(json, List.class);

	}

	public static Object ObjectToObject(Object json, Class<?> cls) {
		try {
			if(json==null)
				return json;
			if (cls.getSimpleName().equals("String")) {
				if (json.getClass().getSimpleName().equals("String"))
					return String.valueOf(json);
			}
			if (json.getClass().getSimpleName().equals("String"))
				return MapJson.getMapper().readValue(String.valueOf(json), cls);
			else if (json.getClass()==cls)
				return json;
			else{
				String strJSON = GetJSONString(json);
				return MapJson.getMapper().readValue(strJSON, cls);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void Info(String strFileName, String msg) {

		synchronized (lockObject) {
			FileOutputStream fo = null;

			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				
				// String strFilePath = String.format("%s%s%s.log",
				// Environment.getDataDirectory() ,strFileName,format.format(new
				// Date())); 
				Date curDate= new Date();
				
				String strFilePath = String.format("%s\\%s%s.log",System.getProperty("user.dir") , strFileName, format.format(curDate));
				fo = new FileOutputStream(strFilePath, true);
				byte[] data = String.format("%s  %s\r\n",format1.format(curDate) , msg).getBytes("utf-8");
				fo.write(data, 0, data.length);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fo != null) {
					try {
						fo.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					fo = null;
				}
			}
		}
	}

}
