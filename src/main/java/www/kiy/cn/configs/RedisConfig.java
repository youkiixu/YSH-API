package www.kiy.cn.configs;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager; 
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer; 
import org.springframework.data.redis.serializer.StringRedisSerializer;
  
/**
 * .Redis缓存配置类提供redisTemplate（获得配置文件中连接参数后的）
 * 
 * @author Administrator
 *
 */
@Configuration
@EnableCaching
public class RedisConfig { 
	
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private Integer port;
	@Value("${spring.redis.password}")
	private String password; 
	
	@Bean
	public CacheManager cacheManager(RedisTemplate<String, Serializable> redisTemplate) {
		System.out.println("Start-Redis-Create CacheManager");
		CacheManager cacheManager = new RedisCacheManager(redisTemplate); 
		return cacheManager;
	}

	@Bean
	public RedisTemplate<String, Serializable> redisTemplate(JedisConnectionFactory redisConnectionFactory) {
		System.out.println("Redis-Config-redisTemplate");
		RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<String, Serializable>(); 
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
		// 以上4条配置可以不用
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		
		return redisTemplate;
	}
	 
	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		System.out.println("Redis-Config-Connection");
		JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
		redisConnectionFactory.setHostName(host);
		redisConnectionFactory.setPort(port);
//		JedisPoolConfig jd = new JedisPoolConfig(); 
//		redisConnectionFactory.setPoolConfig(jd);
		redisConnectionFactory.setPassword(password); 
		return redisConnectionFactory;
	}  

}
