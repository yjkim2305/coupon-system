package com.example.couponcore.coupon.application.facade;

import com.example.couponcore.common.domain.enums.CouponType;
import com.example.couponcore.coupon.TestConfig;
import com.example.couponcore.coupon.domain.Coupon;
import com.example.couponcore.coupon.domain.exception.CouponIssueException;
import com.example.couponcore.coupon.domain.exception.ErrorCode;
import com.example.couponcore.coupon.infrastructure.entity.CouponEntity;
import com.example.couponcore.coupon.infrastructure.repository.CouponJpaRepository;
import com.example.couponcore.couponissue.domain.CouponIssue;
import com.example.couponcore.couponissue.infrastructure.entity.CouponIssueEntity;
import com.example.couponcore.couponissue.infrastructure.repository.CouponIssueJpaRepository;
import com.example.couponcore.couponissue.infrastructure.repository.CustomCouponIssueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CouponFacadeTest extends TestConfig {


    @Autowired CouponFacade couponFacade;

    @Autowired
    CouponJpaRepository couponJpaRepository;

    @Autowired
    CouponIssueJpaRepository couponIssueJpaRepository;

    @Autowired
    CustomCouponIssueRepository customCouponIssueRepository;

    @BeforeEach
    void clean() {
        couponJpaRepository.deleteAllInBatch();
        couponIssueJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("쿠폰 발급 내역이 존재하면 예외를 반환한다.")
    void test1() {
        //given
        Coupon coupon = Coupon.builder()
                .title("선착순 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(100)
                .issuedQuantity(0)
                .discountAmount(1000)
                .minAvailableAmount(1000)
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(2))
                .build();

        couponJpaRepository.save(CouponEntity.toEntity(coupon));

        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(1L)
                .userId(1L)
                .build();

        couponIssueJpaRepository.save(CouponIssueEntity.toEntity(couponIssue));

        //when
        CouponIssueException exception = assertThrows(
                CouponIssueException.class,
                () -> couponFacade.issue(couponIssue.getCouponId(), couponIssue.getUserId())
        );

        //then
        assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATE_COUPON_ISSUE);
    }

    @Test
    @DisplayName("쿠폰 발급 내역이 존재하지 않는다면 쿠폰을 발급한다")
    void test2() {
        //given
        Coupon coupon = Coupon.builder()
                .title("선착순 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(100)
                .issuedQuantity(0)
                .discountAmount(1000)
                .minAvailableAmount(1000)
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(2))
                .build();

        couponJpaRepository.save(CouponEntity.toEntity(coupon));

        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(1L)
                .userId(1L)
                .build();


        //when
        couponFacade.issue(couponIssue.getCouponId(), couponIssue.getUserId());

        //then
        assertTrue(couponIssueJpaRepository.findById(couponIssue.getCouponId()).isPresent());
    }

    @Test
    @DisplayName("발급 수량, 기한, 중복 발급 문제가 없다면 쿠폰을 발급한다")
    void issue_1() {
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

        couponFacade.issue(couponEntity.getId(), userId);

        CouponEntity couponResult = couponJpaRepository.findById(couponEntity.getId()).get();

        assertEquals(couponResult.getIssuedQuantity(), 1);

        CouponIssue issue = customCouponIssueRepository.findFirstCouponIssue(couponEntity.getId(), userId);
        assertNotNull(issue);
    }

    @Test
    @DisplayName("발급 수량에 문제가 있다면 예외를 발생한다")
    void issue_2() {
        long userId = 1;
        Coupon coupon = Coupon.builder()
                .title("선착순 테스트 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(100)
                .issuedQuantity(100)
                .discountAmount(1000)
                .minAvailableAmount(1000)
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(1))
                .build();

        CouponEntity couponEntity = couponJpaRepository.save(CouponEntity.toEntity(coupon));



        CouponIssueException exception = assertThrows(CouponIssueException.class, () -> {
            couponFacade.issue(couponEntity.getId(), userId);
        });
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY);
    }

    @Test
    @DisplayName("발급 기한에 문제가 있다면 예외를 발생한다")
    void issue_3() {
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



        CouponIssueException exception = assertThrows(CouponIssueException.class, () -> {
            couponFacade.issue(couponEntity.getId(), userId);
        });
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_COUPON_ISSUE_DATE);
    }

    @Test
    @DisplayName("쿠폰이 존재하지 않는다면 예외를 반환한다")
    void issue_4() {
        long userId = 1;
        long couponId = 1;

        CouponIssueException exception = assertThrows(CouponIssueException.class, () -> {
            couponFacade.issue(couponId, userId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.COUPON_NOT_EXIST);
    }

}