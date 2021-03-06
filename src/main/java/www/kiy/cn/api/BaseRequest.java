package www.kiy.cn.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
 
import org.springframework.stereotype.Component;
 
import www.kiy.cn.youki.JMap; 
import www.kiy.cn.youki.SetLog;

@Component
public class BaseRequest {

	/**
	 * 通过POST形式获取
	 * 
	 * @param req
	 * @return
	 */
	public JMap getRequestDataMap(HttpServletRequest request) {
		ServletInputStream stream = null;
		JMap map = null;
		try {

			stream = request.getInputStream();
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			byte[] b = new byte[1024];

			int count = -1;
			while ((count = stream.read(b, 0, 1024)) > 0)
				bStream.write(b, 0, count);
			b = null;
			String strJson = new String(bStream.toByteArray(), "utf-8");
			if (!strJson.isEmpty()) {
				map = SetLog.ObjectToMap(strJson);
				JMap par = getRequestParameterMap(request);
				if (par.size() > 0) {
					map.putAll(par);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 通过GET形式获取
	 * 
	 * @param req
	 * @return
	 */
	public JMap getRequestParameterMap(HttpServletRequest req) {

		JMap map = new JMap();
		Enumeration<String> e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String str = e.nextElement();
			String paramValue = "";
			try {
				paramValue = URLDecoder.decode(req.getParameter(str), "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			if (str.equals("strSysJson")) {
				map.putAll(SetLog.ObjectToMap(paramValue));
			} else {
				map.put(str, paramValue);
			}

		}
		return map;
	}

	 
}
