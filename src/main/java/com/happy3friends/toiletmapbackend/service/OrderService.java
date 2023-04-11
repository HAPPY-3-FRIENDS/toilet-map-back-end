package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.request.OrderRequest;
import com.happy3friends.toiletmapbackend.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrderByAccountId(OrderRequest orderRequest);
    List<OrderResponse> getOrderHistoriesByAccountId(int accountId, BasePaginationRequest paginationRequest);
    int count(Integer accountId);
}
