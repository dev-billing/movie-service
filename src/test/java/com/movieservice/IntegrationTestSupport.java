package com.movieservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RKeys;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;


@ActiveProfiles("test")
@SpringBootTest
@Import(RedissonTestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationTestSupport {

    protected static final GenericContainer<?> REDIS_CONTAINER;
    protected static final GenericContainer<?> KAFKA_CONTAINER;

    static {
        REDIS_CONTAINER = new GenericContainer<>("redis:7-alpine")
                .withExposedPorts(6379)
                .withReuse(true)
                .waitingFor(Wait.forListeningPort())
                .withStartupTimeout(Duration.ofSeconds(60));

        REDIS_CONTAINER.start();

        DockerImageName kafkaDockerImageName = DockerImageName.parse("confluentinc/cp-kafka:7.4.0")
                .asCompatibleSubstituteFor("apache/kafka");
        KAFKA_CONTAINER = new KafkaContainer(kafkaDockerImageName);

    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port",
                () -> REDIS_CONTAINER.getMappedPort(6379));
    }

    @Autowired
    protected RedissonClient redissonClient;

    @BeforeEach
    void cleanupRedis() {
        cleanupWithRetry(3);
    }

    private void cleanupWithRetry(int maxAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                // 모든 락 강제 해제 및 데이터 삭제
                RKeys keys = redissonClient.getKeys();

                // 락 해제
                keys.getKeys().forEach(key -> {
                    try {
                        RLock lock = redissonClient.getLock(key);
                        if (lock.isLocked()) {
                            lock.forceUnlock();
                        }
                    } catch (Exception ignored) {}
                });

                // 전체 삭제
                keys.flushdb();

                return; // 성공

            } catch (Exception e) {
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Redis 정리 실패 : ", e);
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted", ie);
                }
            }
        }
    }
}
