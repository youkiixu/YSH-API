package www.kiy.cn.youki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequestHelp {

	public static JMap doGet(String url) {
		return (JMap) doGet(url, JMap.class);
	}

	public static JMap doGet(String url, Object param) {

		String str = doGetRequest(url, param);
		return SetLog.ObjectToMap(str);
	}

	public static String doGetRequest(String url, Object param) {
		String json = SetLog.GetJSONString(param);
		if (json != null)
			url = String.format("%s?%s", url, json);
		String str = null;
		try {
			URLConnection connection = CreateURLConnection(url);

			connection.connect();
			str = GetResponse(connection, null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			str = SetLog.GetJSONString(SetLog.writeMapError("请求异常", e.toString()));

		}
		return str;

	}

	public static JMap doPost(String url) {
		return doPost(url, null);
	}

	public static JMap doPost(String url, Object param) {
		String str = doPost(url, param, null);
		return SetLog.ObjectToMap(str);
	}

	public static Object doPost(String strUrl, Object param, Class<?> cls, String ContentType) {
		String str = doPost(strUrl, param, ContentType);

		return SetLog.ObjectToObject(str, cls);
	}

	public static String doPost(String strUrl, Object param, String ContentType) {
		String res = null;
		try {
			if (Convert.isNullOrEmpty(ContentType))
				ContentType = "application/json";
			URLConnection connection = CreateURLConnection(strUrl);
			connection.setRequestProperty("Content-Type", ContentType);
			// 发送POST请求必须设置如下两行
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			String json = SetLog.GetJSONString(param);

			if (json == null)
				res = GetResponse(connection, null);
			else
				res = GetResponse(connection, json.getBytes("utf-8"));
			// if (res != null && cls != null)
			// return SetLog.ObjectToObject(res, cls);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = SetLog.GetJSONString(SetLog.writeMapError("请求异常", e.toString()));

		} 
		return res;
	}

	public static URLConnection CreateURLConnection(String strUrl) throws Exception {

		URL url = new URL(strUrl);
		URLConnection con = url.openConnection();
		con.setConnectTimeout(10000);
		// 设置通用的请求属性
		con.setRequestProperty("accept", "*/*");
		con.setRequestProperty("connection", "Keep-Alive");
		con.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		return con;
	}

	public static String GetResponse(URLConnection connection, byte[] data) {
		BufferedReader reader = null;
		OutputStream output = null;
		try {
			if (data != null && data.length > 0) {
				output = connection.getOutputStream();
				output.write(data, 0, data.length);
				output.flush();
			}
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			StringBuilder stringBuilder = new StringBuilder();
			String str = null;
			while ((str = reader.readLine()) != null)
				stringBuilder.append(str);
			return stringBuilder.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				reader = null;
			}
		}
		return null;

	}

}
