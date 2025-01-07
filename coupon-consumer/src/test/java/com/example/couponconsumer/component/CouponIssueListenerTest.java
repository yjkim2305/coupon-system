package com.example.couponconsumer.component;

import com.example.couponconsumer.TestConfig;
import com.example.couponcore.coupon.application.facade.CouponFacade;
import com.example.couponcore.coupon.infrastructure.repository.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collection;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Import(CouponIssueListener.class)
class CouponIssueListenerTest extends TestConfig {
    @Autowired CouponIssueListener couponIssueListener;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    RedisRepository redisRepository;

    @MockitoBean
    CouponFacade couponFacade;

    @BeforeEach
    void clear() {
        Collection<String> redisKeys = redisTemplate.keys("coupon:*");
        redisTemplate.delete(redisKeys);
    }

    @Test
    @DisplayName("쿠폰 발급 큐에 처리 대상이 없다면 발급을 하지 않는다.")
    void test1() throws JsonProcessingException {
        couponIssueListener.issue();

        verify(couponFacade, never()).issue(anyLong(), anyLong());
    }

    @Test
    @DisplayName("쿠폰 발급 큐에 처리 대상이 있다면 발급한다.")
    void test2() throws JsonProcessingException {
        long couponId = 1;
        long userId = 1;
        int totalQuantity = Integer.MAX_VALUE;
        redisRepository.issueRequest(couponId, userId, totalQuantity);

        couponIssueListener.issue();

        verify(couponFacade, times(1)).issue(anyLong(), anyLong());
    }

    @Test
    @DisplayName("쿠폰 발급 요청 순서에 맞게 처리된다.")
    void test3() throws JsonProcessingException {
        long couponId = 1;
        long userId1 = 4;
        long userId2 = 5;
        long userId3 = 6;
        int totalQuantity = Integer.MAX_VALUE;
        redisRepository.issueRequest(couponId, userId1, totalQuantity);
        redisRepository.issueRequest(couponId, userId2, totalQuantity);
        redisRepository.issueRequest(couponId, userId3, totalQuantity);

        couponIssueListener.issue();
        InOrder inOrder = Mockito.inOrder(couponFacade);
        inOrder.verify(couponFacade, times(1)).issue(couponId, userId1);
        inOrder.verify(couponFacade, times(1)).issue(couponId, userId2);
        inOrder.verify(couponFacade, times(1)).issue(couponId, userId3);

    }


}