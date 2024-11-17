package co.kr.common.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class CacheConfigurer {

	private final RedisProperties redisProperties;

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		final Codec codec = new StringCodec(); // redis-cli에서 보기 위해
		config.setCodec(codec);
		config.useSingleServer()
			.setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
			.setConnectionPoolSize(20)
			.setConnectionMinimumIdleSize(5);

		return Redisson.create(config);
	}
}
