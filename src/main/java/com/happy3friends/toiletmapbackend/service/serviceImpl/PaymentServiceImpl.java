package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
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
import com.happy3friends.toiletmapbackend.repository.UserInfoRepository;
import com.happy3friends.toiletmapbackend.request.PaymentRequest;
import com.happy3friends.toiletmapbackend.response.PaymentResponse;
import com.happy3friends.toiletmapbackend.service.PaymentService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public PaymentResponse createPaymentByAccountId(PaymentRequest paymentRequest) {
        CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByAccountId(paymentRequest.getAccountId());

        if (customAccountInfoDTO == null)
            throw new NotFoundException("Account", "Id", paymentRequest.getAccountId());
        if (!customAccountInfoDTO.getRole().equals(RoleEnum.USER.getRoleName()))
            throw new BadRequestException("Invalid account Id!");
        if (!paymentRequest.getMethod().equals(PaymentTypeEnum.VN_PAY.getPaymentValue())
                && !paymentRequest.getMethod().equals(PaymentTypeEnum.CASH.getPaymentValue()))
            throw new BadRequestException("Invalid payment method!");

        // TODO: check userToken with account from accountId

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setAccountId(paymentRequest.getAccountId());
        paymentEntity.setTotal(paymentRequest.getTotal());
        paymentEntity.setMethod(paymentRequest.getMethod());
        paymentEntity.setCreatedDate(DateTimeUtil.getTimestampNow());
        paymentRepository.save(paymentEntity);

        // Add money to account balance by accountId
        int newAccountBalance = customAccountInfoDTO.getAccountBalance() + paymentRequest.getTotal();
        userInfoRepository.updateAccountBalance(paymentRequest.getAccountId(), newAccountBalance);

        return paymentMapper.convertPaymentEntityToPaymentResponse(paymentEntity);
    }

    @Override
    public List<PaymentResponse> getPaymentHistoriesByAccountId(int accountId, BasePaginationRequest paginationRequest) {
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.CREATED_DATE);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent()) throw new NotFoundException("Account", "Id", accountId);

        List<PaymentEntity> paymentEntities = paymentRepository.findAllByAccountId(accountId, pageable);

        return paymentEntities.stream()
                .map(dto -> paymentMapper.convertPaymentEntityToPaymentResponse(dto))
                .collect(Collectors.toList());
    }
}
