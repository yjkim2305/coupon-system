package com.example.couponcore.coupon.domain;

import com.example.couponcore.coupon.domain.exception.CouponIssueException;
import com.example.couponcore.coupon.domain.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CouponTest {


    @Test
    @DisplayName("발급 수량이 남아 있다면 true를 반환한다")
    void couponIssueQuantityTrueTest() {
        //given
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .build();
        
        //when
        boolean result = coupon.availableIssueQuantity();

        //then
        assertThat(result).isEqualTo(true);

    }

    @Test
    @DisplayName("발급 수량이 남아 있지 않으면 false를 반환한다")
    void couponIssueQuantityFalseTest() {
        //given
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .build();

        //when
        boolean result = coupon.availableIssueQuantity();

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("발급 수량이 설정되지 않으면 true를 반환한다")
    void couponIssueQuantityNullTest() {
        //given
        Coupon coupon = Coupon.builder()
                .totalQuantity(null)
                .issuedQuantity(100)
                .build();
        //when
        boolean result = coupon.availableIssueQuantity();

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("쿠폰 발급 기한이 시작되지 않았다면 false를 반환한다")
    void couponIssueDateFalseTest() {
        //given
        Coupon coupon = Coupon.builder()
                .issueStartDate(LocalDateTime.now().plusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(2))
                .build();

        //when
        boolean result = coupon.availableIssueDate();

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("쿠폰 발급 기한이 시작되지 않았다면 false를 반환한다")
    void couponIssueDateFalseTest2() {
        //given
        Coupon coupon = Coupon.builder()
                .issueStartDate(LocalDateTime.now().minusDays(2))
                .issueEndDate(LocalDateTime.now().minusDays(1))
                .build();

        //when
        boolean result = coupon.availableIssueDate();

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("쿠폰 발급 기한이 해당하면 true를 반환한다")
    void couponIssueDateTrueTest() {
        //given
        Coupon coupon = Coupon.builder()
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(2))
                .build();

        //when
        boolean result = coupon.availableIssueDate();

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("발급 기한과 수량이 유효하면 쿠폰 발급 성공")
    void issue1() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(2))
                .build();

        coupon.issue();

        assertThat(coupon.getIssuedQuantity()).isEqualTo(100);
    }

    @Test
    @DisplayName("수량이 유효하지 않을 때 발급이 되지 않음")
    void issue2() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .issueStartDate(LocalDateTime.now().minusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(2))
                .build();

        CouponIssueException exception = assertThrows(CouponIssueException.class, coupon::issue);
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY);

    }

    @Test
    @DisplayName("기한이 유효하지 않을 때 발급이 되지 않음")
    void issue3() {
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .issueStartDate(LocalDateTime.now().plusDays(1))
                .issueEndDate(LocalDateTime.now().plusDays(2))
                .build();

        CouponIssueException exception = assertThrows(CouponIssueException.class, coupon::issue);
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_COUPON_ISSUE_DATE);

    }
}