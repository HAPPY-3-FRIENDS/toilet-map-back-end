package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.request.PaymentRequest;
import com.happy3friends.toiletmapbackend.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPaymentByAccountId(PaymentRequest paymentRequest);
    List<PaymentResponse> getPaymentHistoriesByAccountId(int accountId, BasePaginationRequest paginationRequest);
}
