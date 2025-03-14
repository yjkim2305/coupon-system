package com.example.couponcore.coupon.application.service;

import com.example.couponcore.coupon.application.repository.CouponRepository;
import com.example.couponcore.coupon.domain.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public Coupon getById(long couponId) {
        return couponRepository.findById(couponId);
    }

    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public Coupon getByIdWithLock(long couponId) {
        return couponRepository.findByIdWithLock(couponId);
    }

}
