package com.movieservice.infra.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class DistributedLockHelper {

    private final RedissonClient redissonClient;

    /**
     * Multi Lock 실행 (ALL 전략)
     */
    public <T> T executeWithMultiLock(Collection<String> lockKeys, long waitTime, long leaseTime, TimeUnit timeUnit, Supplier<T> action) {

        RLock[] locks = lockKeys.stream()
                .map(redissonClient::getLock)
                .toArray(RLock[]::new);

        RLock multiLock = redissonClient.getMultiLock(locks);

        try {
            boolean acquired = multiLock.tryLock(waitTime, leaseTime, timeUnit);

            if (!acquired) {
                throw new RuntimeException("Lock 획득 중입니다.");
            }

            log.info("Multi Lock 획득: {}", lockKeys);
            return action.get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Multi Lock 대기 중단: " + lockKeys);
        } finally {
            multiLock.unlock();
        }
    }
}
