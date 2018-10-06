package www.kiy.cn.youki;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Pub {
	private final JMap InstanceMap = new JMap();
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

	public Object InvokeMethod(String strPackage, String strClassName, String strMethod, Object json) throws Exception {
		return InvokeMethod(null, strPackage, strClassName, strMethod, json);
	}

	public Object InvokeMethod(String strFilePath, String strPackage, String strClassName, String strMethod,
			Object json) throws Exception {
		// if(strPackage.isEmpty())
		// return setLog.writeMapError("包名不能为空");
		// if(strClassName.isEmpty())
		// return setLog.writeMapError("类名不能为空");
		// if(strMethod.isEmpty())
		// return setLog.writeMapError("方法名不能为空");

		String key = String.format("%s.%s.%sInstanceMethod", strPackage, strClassName, strMethod);
		Class<?> cls = GetClass(strFilePath, strPackage, strClassName);

		if (cls == null) {
			return SetLog.writeMapError("找不到对应类型信息");
		}
		try {
			Method method = null;
			if (InstanceMethodMap.containsKey(key))
				method = (Method) InstanceMethodMap.get(key);
			 else {
				 method = cls.getMethod(strMethod, json.getClass());
				 InstanceMethodMap.put(key, method);
			}
			return method.invoke(null, json);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return SetLog.writeMapError("找不到对应类型信息", e.toString());
		}
	}

	public Class<?> GetClass(String strFilePath, String strPackage, String strClassName) throws Exception {
		if (strPackage.isEmpty() || strClassName.isEmpty())
			return null;
		String strClass = String.format("%s.%s", strPackage, strClassName);

		// try {
		Class<?> cls = null;
		if (strFilePath.isEmpty()) {
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
}
