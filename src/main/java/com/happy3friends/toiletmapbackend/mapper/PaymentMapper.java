package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.PaymentEntity;
import com.happy3friends.toiletmapbackend.response.PaymentResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PaymentMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentMapper.class);

    @Autowired
    private ModelMapper modelMapper;

    public PaymentResponse convertPaymentEntityToPaymentResponse(PaymentEntity paymentEntity) {
        return Objects.isNull(paymentEntity)
                ? null
                : modelMapper.map(paymentEntity, PaymentResponse.class);
    }
}
