package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.PaymentRequest;
import com.happy3friends.toiletmapbackend.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPaymentByAccountId(int accountId, PaymentRequest paymentRequest);
    List<PaymentResponse> getPaymentHistoriesByAccountId(int accountId);
}
