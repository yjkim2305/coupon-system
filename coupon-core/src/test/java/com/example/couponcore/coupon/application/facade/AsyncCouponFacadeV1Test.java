package com.example.couponcore.coupon.application.facade;

import com.example.couponcore.common.domain.enums.CouponType;
import com.example.couponcore.coupon.TestConfig;
import com.example.couponcore.coupon.domain.Coupon;
import com.example.couponcore.coupon.domain.exception.CouponIssueException;
import com.example.couponcore.coupon.domain.exception.ErrorCode;
import com.example.couponcore.coupon.infrastructure.entity.CouponEntity;
import com.example.couponcore.coupon.infrastructure.repository.CouponJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.IntStream;

import static com.example.couponcore.common.util.CouponRedisUtils.getIssueRequestKey;
import static org.junit.jupiter.api.Assertions.*;

class AsyncCouponFacadeV1Test extends TestConfig {

    @Autowired AsyncCouponFacadeV1 facade;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    CouponJpaRepository couponJpaRepository;

    @BeforeEach
    void clear() {
        Collection<String> keys = redisTemplate.keys("coupon*");
        redisTemplate.delete(keys);
    }


    @Test
    @DisplayName("쿠폰 발급 - 쿠폰이 존재하지 않는다면 예외를 반환한다.")
    void test() {
        long couponId = 1;
        long userId = 1;

        CouponIssueException exception = assertThrows(
                CouponIssueException.class,
                () -> facade.issue(couponId, userId)
        );

        assertEquals(exception.getErrorCode(), ErrorCode.COUPON_NOT_EXIST);
    }

    @Test
    @DisplayName("쿠폰 발급 - 발급 가능 수량이 존재하지 않는다면 예외를 반환한다.")
    void test2() {
        long userId = 1000;

        Coupon coupon = Coupon.builder()
                .title("선착순 테스트 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(100)
                .issuedQuantity(0)
                .discountAmount(1000)
                .minAvailableAmount(1000)
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(1))
                .build();

        CouponEntity couponEntity = couponJpaRepository.save(CouponEntity.toEntity(coupon));

        IntStream.range(0, couponEntity.getTotalQuantity()).forEach(idx -> {
            redisTemplate.opsForSet().add(getIssueRequestKey(couponEntity.getId()), String.valueOf(idx));
        });


        CouponIssueException exception = assertThrows(
                CouponIssueException.class,
                () -> facade.issue(couponEntity.getId(), userId)
        );

        assertEquals(exception.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_QUANTITY);
    }

    @Test
    @DisplayName("쿠폰 발급 - 이미 발급된 유저라면 예외를 반환한다.")
    void test3() {
        long userId = 1;

        Coupon coupon = Coupon.builder()
                .title("선착순 테스트 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(100)
                .issuedQuantity(0)
                .discountAmount(1000)
                .minAvailableAmount(1000)
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(1))
                .build();

        CouponEntity couponEntity = couponJpaRepository.save(CouponEntity.toEntity(coupon));
        redisTemplate.opsForSet().add(getIssueRequestKey(couponEntity.getId()), String.valueOf(userId));


        CouponIssueException exception = assertThrows(
                CouponIssueException.class,
                () -> facade.issue(couponEntity.getId(), userId)
        );

        assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATE_COUPON_ISSUE);
    }

    @Test
    @DisplayName("쿠폰 발급 - 발급 기한이 유효하지 않다면 예외를 반환한다.")
    void test4() {
        long userId = 1;

        Coupon coupon = Coupon.builder()
                .title("선착순 테스트 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(100)
                .issuedQuantity(0)
                .discountAmount(1000)
                .minAvailableAmount(1000)
                .issueStartDate(LocalDateTime.now().minusDays(2))
                .issueEndDate(LocalDateTime.now().minusDays(1))
                .build();

        CouponEntity couponEntity = couponJpaRepository.save(CouponEntity.toEntity(coupon));
        redisTemplate.opsForSet().add(getIssueRequestKey(couponEntity.getId()), String.valueOf(userId));


        CouponIssueException exception = assertThrows(
                CouponIssueException.class,
                () -> facade.issue(couponEntity.getId(), userId)
        );

        assertEquals(exception.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_DATE);
    }

    @Test
    @DisplayName("쿠폰 발급을 기록한다")
    void test5() {
        long userId = 1;

        Coupon coupon = Coupon.builder()
                .title("선착순 테스트 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(100)
                .issuedQuantity(0)
                .discountAmount(1000)
                .minAvailableAmount(1000)
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(2))
                .build();

        CouponEntity couponEntity = couponJpaRepository.save(CouponEntity.toEntity(coupon));
        facade.issue(couponEntity.getId(), userId);

        Boolean isSaved = redisTemplate.opsForSet().isMember(getIssueRequestKey(couponEntity.getId()), String.valueOf(userId));

        Assertions.assertThat(isSaved).isTrue();
    }
}