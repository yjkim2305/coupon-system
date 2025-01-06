package com.example.couponcore.coupon.infrastructure.repository;

import com.example.couponcore.coupon.application.repository.CouponRepository;
import com.example.couponcore.coupon.domain.Coupon;
import com.example.couponcore.coupon.domain.exception.CouponIssueException;
import com.example.couponcore.coupon.domain.exception.ErrorCode;
import com.example.couponcore.coupon.infrastructure.entity.CouponEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Coupon findById(Long id) {
        return couponJpaRepository.findById(id)
                .map(Coupon::from)
                .orElseThrow(() -> new CouponIssueException(ErrorCode.COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. %s".formatted(id)));
    }


    public Coupon save(Coupon coupon) {
        CouponEntity couponEntity = couponJpaRepository.save(CouponEntity.toEntity(coupon));
        return Coupon.from(couponEntity);
    }

    @Override
    public Coupon findByIdWithLock(Long id) {
        return couponJpaRepository.findCouponWithLock(id)
                .map(Coupon::from)
                .orElseThrow(() -> new CouponIssueException(ErrorCode.COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. %s".formatted(id)));
    }
}
