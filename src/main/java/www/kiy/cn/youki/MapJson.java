package www.kiy.cn.youki;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.ser.std.SerializerBase;

public class MapJson {
	private static ObjectMapper maper = null;

	private MapJson() {
	}

	public static ObjectMapper getMapper() {
		if (maper == null) {
			maper = new ObjectMapper();
			SimpleModule model = new SimpleModule("model", new Version(1, 0, 0, null));
			model.addSerializer(new utilDateSerializer());

		}
		return maper;
	}

	/**
	 *
	 * @author Administrator
	 *
	 */
	private static class utilDateSerializer extends SerializerBase<java.util.Date> {
		public utilDateSerializer() {
			super(java.util.Date.class);
		}

		public void serialize(java.util.Date value, JsonGenerator jen, SerializerProvider provider)
				throws JsonGenerationException, java.io.IOException {
			java.text.SimpleDateFormat sFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String str = sFormat.format(value);
			jen.writeString(str);

		}
	}

	@SuppressWarnings("unused")
	private static class sqlDateSerializer extends SerializerBase<java.sql.Date> {
		public sqlDateSerializer() {
			super(java.sql.Date.class);
		}

		public void serialize(java.sql.Date value, JsonGenerator jen, SerializerProvider provider)
				throws JsonGenerationException, java.io.IOException {

			java.text.SimpleDateFormat sFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String str = sFormat.format(value);
			jen.writeString(str);
		}
	}
}
