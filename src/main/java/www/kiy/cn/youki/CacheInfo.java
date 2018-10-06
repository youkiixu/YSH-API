package www.kiy.cn.youki;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

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
		System.out.println(String.format("key=%s,value=%s", key, str)); 
		return str; 
	}

	public JMap getMap(String key) {
		if (!exits(key))
			return null;
		JMap map = this.valOpsMap.get(key);
		//ValueOperations<String, JMap> operations = redisTemplate.opsForValue();
		//JMap map = SetLog.ObjectToMap( operations.get(key));

		return map;
	}

	public void putString(String key, String value) {
		//7天超时
		putString(key, value,  60*24*7);
	}
	
	/**
	 * 
	 * @param key 
	 * @param value
	 * @param expireTime
	 */
	public void putString(String key, String value, int expireMinute) {
		if (exits(key))
			strRedisTemplate.delete(key);  
		 strRedisTemplate.opsForValue().set(key, value,expireMinute,TimeUnit.MINUTES);	  
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
			this.putString(key, SetLog.GetJSONString(  value));
		}
		else {
			this.putString(key, SetLog.GetJSONString(  value),expireMinute);
			//this.valOpsMap.set(key, value, expireMinute,TimeUnit.MINUTES);
			
		}
			
		
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
