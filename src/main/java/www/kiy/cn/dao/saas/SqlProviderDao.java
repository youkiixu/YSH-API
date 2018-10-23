package www.kiy.cn.dao.saas;

import java.util.Iterator;
import org.apache.ibatis.jdbc.SQL;

import www.kiy.cn.HotKey.eCmdType;
import www.kiy.cn.youki.JMap;

public class SqlProviderDao {

	public String getDataTableBySystemDao(final JMap map) {

		String strSql = map.getWithRemoveKey("strSysSqlKey").toString();
		return strSql;
	}

	
}
