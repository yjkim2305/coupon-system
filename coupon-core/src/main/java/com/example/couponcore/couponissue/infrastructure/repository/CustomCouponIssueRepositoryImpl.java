package com.example.couponcore.couponissue.infrastructure.repository;

import com.example.couponcore.couponissue.domain.CouponIssue;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.couponcore.couponissue.infrastructure.entity.QCouponIssueEntity.couponIssueEntity;

@Repository
@RequiredArgsConstructor
public class CustomCouponIssueRepositoryImpl implements CustomCouponIssueRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public CouponIssue findFirstCouponIssue(Long couponId, Long userId) {
        return queryFactory.select(Projections.constructor(CouponIssue.class,
                        couponIssueEntity.id,
                        couponIssueEntity.couponId,
                        couponIssueEntity.userId,
                        couponIssueEntity.issuedDate,
                        couponIssueEntity.usedDate,
                        couponIssueEntity.createdDate,
                        couponIssueEntity.updatedDate
                        ))
                .from(couponIssueEntity)
                .where(couponIssueEntity.couponId.eq(couponId))
                .where(couponIssueEntity.userId.eq(userId))
                .fetchFirst();
    }
}
