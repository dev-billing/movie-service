package com.movieservice.infra.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String host;

    @Value("${spring.data.redis.port:6379}")
    private int port;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        String address = String.format("redis://%s:%d", host, port);

        config.useSingleServer()
                .setAddress(address)
                .setConnectionPoolSize(64)       // 연결 풀 크기
                .setConnectionMinimumIdleSize(10) // 최소 유휴 연결
                .setIdleConnectionTimeout(10000)  // 유휴 연결 타임아웃 (10초)
                .setConnectTimeout(10000)         // 연결 타임아웃 (10초)
                .setTimeout(3000)                 // 응답 타임아웃 (3초)
                .setRetryAttempts(3)              // 재시도 횟수
                .setRetryInterval(1500)           // 재시도 간격 (1.5초)
                .setDatabase(0);                  // DB 번호

        // Codec 설정 (Jackson 사용)
        config.setCodec(new JsonJacksonCodec());

        // 스레드 풀 설정
        config.setThreads(16);
        config.setNettyThreads(32);

        return Redisson.create(config);
    }
}
