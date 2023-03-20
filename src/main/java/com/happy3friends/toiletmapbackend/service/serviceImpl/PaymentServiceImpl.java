package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.PaymentEntity;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.PaymentMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.PaymentRepository;
import com.happy3friends.toiletmapbackend.request.PaymentRequest;
import com.happy3friends.toiletmapbackend.response.PaymentResponse;
import com.happy3friends.toiletmapbackend.service.PaymentService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public PaymentResponse createPaymentByAccountId(int accountId, PaymentRequest paymentRequest) {
        CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByAccountId(accountId);

        if (customAccountInfoDTO == null)
            throw new NotFoundException("Account", "Id", accountId);
        if (!customAccountInfoDTO.getRole().equals(RoleEnum.USER.getRoleName()))
            throw new BadRequestException("Can not top up for any account except account with role 'User'!");
        if (!paymentRequest.getMethod().equals(PaymentTypeEnum.VN_PAY.getPaymentValue())
                && !paymentRequest.getMethod().equals(PaymentTypeEnum.CASH.getPaymentValue()))
            throw new BadRequestException("Invalid payment method!");

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setAccountId(accountId);
        paymentEntity.setTotal(paymentRequest.getTotal());
        paymentEntity.setMethod(paymentRequest.getMethod());
        paymentEntity.setCreatedDate(DateTimeUtil.getTimestampNow());
        paymentRepository.save(paymentEntity);

        // Add money to account balance by accountId
        int newAccountBalance = customAccountInfoDTO.getAccountBalance() + paymentRequest.getTotal();
        accountRepository.updateAccountBalance(accountId, newAccountBalance);

        return paymentMapper.convertPaymentEntityToPaymentResponse(paymentEntity);
    }
}
