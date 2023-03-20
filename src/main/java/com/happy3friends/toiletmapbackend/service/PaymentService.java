package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.PaymentRequest;
import com.happy3friends.toiletmapbackend.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPaymentByAccountId(int accountId, PaymentRequest paymentRequest);
}
