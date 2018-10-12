package www.kiy.cn.youki;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.microsoft.sqlserver.jdbc.StringUtils;

@Component
public class CacheInfo {
	@Resource
	private StringRedisTemplate strRedisTemplate;
	
	
	@Resource(name = "stringRedisTemplate")
	ValueOperations<String, String> valOpsStr;

	@Resource
	RedisTemplate<String, JMap> redisTemplate;
	@Resource(name = "redisTemplate")
	ValueOperations<String, JMap> valOpsMap;

	public boolean exits(String key) {
		boolean bol = redisTemplate.hasKey(key);
		// strRedisTemplate.hasKey(key);
		return bol;
	}
	
	public String getString(String key) {
		if (!exits(key))
			return null;
		String str = strRedisTemplate.opsForValue().get(key);
		System.out.println(String.format("读取Redis  String 缓存: key=%s,value=%s", key, str)); 
		return str; 
	}

	public JMap getMap(String key) {
		if (!exits(key))
			return null;
		//JMap map = this.valOpsMap.get(key);  
		String json=  getString(key); 
		JMap map = SetLog.ObjectToMap(json); 
		return map;
	}

	public void putString(String key, Object value) {
		//7天超时
		putString(key, value,  60*24*7);
	}
	
	/**
	 * 
	 * @param key 
	 * @param value
	 * @param expireTime
	 */
	public void putString(String key, Object value, int expireMinute) {
		if (exits(key))
			strRedisTemplate.delete(key);  
		 strRedisTemplate.opsForValue().set(key, SetLog.GetJSONString(value) ,expireMinute,TimeUnit.MINUTES);	  
	}

	public void putJMap(String key, JMap value) {
		putJMap(key, value,  60*24*7);
	}

	public void putJMap(String key, JMap value, int expireMinute) {
		if (exits(key)){
			//this.redisTemplate.delete(key);
			this.del(key);
		} 
		if(expireMinute==-1){
			//	this.valOpsMap.set(key, value);			
			this.putString(key,  value);
		}
		else {
			this.putString(key, value,expireMinute);
			//this.valOpsMap.set(key, value, expireMinute,TimeUnit.MINUTES); 
		} 
	}
	public void putListToMap(String key, String uid, List<JMap> value) {
		  putListToMap(key, uid,value,60 * 24 * 7);
	}
	public void putListToMap(String key, String uid, List<JMap> value, int expireMinute) {
		JMap map= null;
		if(this.exits(key))
			map=this.getMap(key);
		else 
			map =new JMap(); 
		map.put(uid,value);
		this.putJMap(key, map,expireMinute);
	}
	
	public void putMapToList(String key, String uid, JMap value) {
		value.put("cachePrimaryKey", uid);
		List<JMap> lst = new ArrayList<JMap>();
		lst.add(value);
		putMapToList(key, lst);
	}

	public void putMapToList(String key, List<JMap> value) {
		putMapToList(key, value, 60 * 24 * 7);
	}

	public void putMapToList(String key, List<JMap> value, int expireMinute) {

		if (exits(key)) {
			this.del(key);
		}
		if (expireMinute == -1) {
			this.putString(key, value);
		} else {
			this.putString(key, value, expireMinute);
		}
	}
	
	public List<JMap> getList(String key) {
		String json = getString(key); 
		List<JMap> lst = SetLog.ObjectToListMap(json);
		return lst;
	} 
	public List<JMap> getListFromMap(String key,String uid) {
		if (!exits(key))
			return null;
		//String json = getString(key); 
		JMap map=this.getMap(key);
		if(!map.containsKey(uid))
			return null; 
		
		List<JMap> lst = SetLog.ObjectToListMap(map.get(uid));
		return lst;
	}
	public JMap getMapFromList(String key,String uid){
//		if (!exits(key))
//			return null;
//		String json = getString(key);
//		List<JMap> lst = SetLog.ObjectToListMap(json); 
//		lst.forEach((tmp)->{
//			if(tmp.get("cachePrimaryKey").getClass().toString().equals(uid)){
//				res.putAll(tmp); 
//				return;
//			} 
//		});
		List<JMap> lst=getListFromMap(key, uid);
		
		for (int i = 0; i < lst.size(); i++) {
			JMap tmp = lst.get(i);
			if (tmp.get("cachePrimaryKey").getClass().toString().equals(uid)) {
				return tmp;
			}
		}
		return null;
	}
	
	
	public void del(String key) {
		if (exits(key)) {
			redisTemplate.delete(key);
		}
	}

	public void del(String... keys) {
		for (String key : keys)
			redisTemplate.delete(key);
	}
	
}
