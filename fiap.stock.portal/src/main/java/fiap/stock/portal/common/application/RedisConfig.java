package fiap.stock.portal.common.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(@Value("${redis.host}") String host,
                                                           @Value("${redis.port}") Integer port,
                                                           @Value("${redis.password}")String password) {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();

        redisConf.setHostName(host);
        redisConf.setPort(port);
        redisConf.setPassword(
                RedisPassword.of(password)
        );

        return new LettuceConnectionFactory(redisConf);
    }

    @Bean
    public RedisCacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisCacheManager redisCacheManager = RedisCacheManager.create(lettuceConnectionFactory);
        redisCacheManager.setTransactionAware(true);

        return redisCacheManager;
    }
}
