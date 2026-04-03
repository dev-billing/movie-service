package com.movieservice;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class RedissonTestConfig {

    private static final Object LOCK = new Object();
    private static volatile RedissonClient SHARED_INSTANCE;
    private static volatile String CURRENT_ADDRESS;
    private static volatile boolean IS_SHUTTING_DOWN = false;

    @Bean(destroyMethod = "")  // Spring이 자동으로 shutdown 호출하지 않도록
    @Primary
    public RedissonClient redissonClient(
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") int port) {

        String address = "redis://" + host + ":" + port;

        synchronized (LOCK) {
            // 기존 인스턴스가 있고 같은 주소면 재사용
            if (SHARED_INSTANCE != null &&
                    !SHARED_INSTANCE.isShutdown() &&
                    !IS_SHUTTING_DOWN &&
                    address.equals(CURRENT_ADDRESS)) {

                System.out.println("Redis 재사용 : " + address);
                return SHARED_INSTANCE;
            }

            // 이전 인스턴스 정리
            if (SHARED_INSTANCE != null && !SHARED_INSTANCE.isShutdown()) {
                System.out.println("이전 RedissonClient 종료");
                IS_SHUTTING_DOWN = true;

                try {
                    SHARED_INSTANCE.shutdown();
                    Thread.sleep(1000); // 완전 종료 대기
                } catch (Exception e) {
                    System.err.println("RedissonClient 종료 중 에러 발생 : " + e.getMessage());
                } finally {
                    IS_SHUTTING_DOWN = false;
                }
            }

            Config config = new Config();
            config.useSingleServer()
                    .setAddress(address)
                    // 연결 풀 설정 - 충분히 크게
                    .setConnectionPoolSize(64)
                    .setConnectionMinimumIdleSize(16)
                    // 타임아웃 설정
                    .setConnectTimeout(10000)
                    .setTimeout(5000)
                    // 재시도 설정 - 더 관대하게
                    .setRetryAttempts(5)
                    .setRetryInterval(2000)
                    // 연결 유지
                    .setKeepAlive(true)
                    .setTcpNoDelay(true)
                    .setPingConnectionInterval(30000);

            // Netty 스레드 증가
            config.setNettyThreads(8);
            config.setThreads(8);

            // Lockwatchdog 타임아웃
            config.setLockWatchdogTimeout(30000);

            SHARED_INSTANCE = Redisson.create(config);
            CURRENT_ADDRESS = address;

            System.out.println("RedissonClient 생성 완료");

            return SHARED_INSTANCE;
        }
    }

    // JVM 종료 시 정리
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronized (LOCK) {
                if (SHARED_INSTANCE != null && !SHARED_INSTANCE.isShutdown()) {
                    System.out.println("RedissonClient Shutdown");
                    SHARED_INSTANCE.shutdown();
                }
            }
        }));
    }
}
