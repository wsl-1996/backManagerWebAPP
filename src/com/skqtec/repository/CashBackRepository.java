package com.skqtec.repository;

import com.skqtec.entity.CashbackEntity;

public interface CashBackRepository extends DomainRepository<CashbackEntity,String> {
    public int statisticCashback(String userId);
}
