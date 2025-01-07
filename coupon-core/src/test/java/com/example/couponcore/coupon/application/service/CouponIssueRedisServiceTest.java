package com.example.couponcore.coupon.application.service;

import com.example.couponcore.coupon.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.stream.IntStream;

import static com.example.couponcore.common.util.CouponRedisUtils.getIssueRequestKey;
import static org.assertj.core.api.Assertions.assertThat;

class CouponIssueRedisServiceTest extends TestConfig {

    @Autowired CouponIssueRedisService couponIssueRedisService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void clear() {
        Collection<String> redisKeys = redisTemplate.keys("*");
        redisTemplate.delete(redisKeys);
    }

    @Test
    @DisplayName("쿠폰 수량 검증 - 발급 가능 수량이 존재하면 true를 반환한다")
    void test1() {

        int totalIssueQuantity = 10;
        long couponId = 1;

        boolean result = couponIssueRedisService.availableTotalIssueQuantity(totalIssueQuantity, couponId);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("쿠폰 수량 검증 - 발급 가능 수량이 모두 소진되면 false를 반환한다")
    void test2() {

        int totalIssueQuantity = 10;
        long couponId = 1;

        IntStream.range(0, totalIssueQuantity).forEach(userId -> {
            redisTemplate.opsForSet().add(getIssueRequestKey(couponId), String.valueOf(userId));
        });

        boolean result = couponIssueRedisService.availableTotalIssueQuantity(totalIssueQuantity, couponId);

        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("쿠폰 중복 발급 검증 - 발급된 내역에 유저가 존재하지 않으면 true를 반환한다")
    void test3() {
        int couponId = 1;
        long userId = 1;

        boolean result = couponIssueRedisService.availableUserIssueQuantity(couponId, userId);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("쿠폰 중복 발급 검증 - 발급된 내역에 유저가 존재하면 false를 반환한다")
    void test4() {
        int couponId = 1;
        long userId = 1;
        redisTemplate.opsForSet().add(getIssueRequestKey(couponId), String.valueOf(userId));

        boolean result = couponIssueRedisService.availableUserIssueQuantity(couponId, userId);

        assertThat(result).isFalse();
    }


}