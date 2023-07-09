package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.config.VNPayConfig;
import com.happy3friends.toiletmapbackend.constant.DateTimeConstant;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.PaymentEntity;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.PaymentMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.PaymentRepository;
import com.happy3friends.toiletmapbackend.repository.TransactionRepository;
import com.happy3friends.toiletmapbackend.repository.UserInfoRepository;
import com.happy3friends.toiletmapbackend.request.PaymentRequest;
import com.happy3friends.toiletmapbackend.response.PaymentResponse;
import com.happy3friends.toiletmapbackend.service.PaymentService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
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
    TransactionRepository transactionRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    private PaymentResponse createPaymentUrlForVNPay(PaymentRequest paymentRequest) throws UnsupportedEncodingException {

        SimpleDateFormat formatter = new SimpleDateFormat(DateTimeConstant.yyyyMMddHHmmss);
        Date dateNow = DateTimeUtil.getDateNow();
        String vnp_CreateDate = formatter.format(dateNow);
        String vnp_ExpireDate = formatter.format(DateUtils.addMinutes(dateNow, 15));
        String vnp_TxnRef = vnp_CreateDate;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(paymentRequest.getTotal() * 100));
        vnp_Params.put("vnp_CurrCode", VNPayConfig.vnp_CurrCode);
        vnp_Params.put("vnp_BankCode", VNPayConfig.vnp_BankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", String.valueOf(paymentRequest.getAccountId()));
        vnp_Params.put("vnp_OrderType", VNPayConfig.vnp_OrderType);
        vnp_Params.put("vnp_Locale", VNPayConfig.vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.getVnpReturnUrl());
        vnp_Params.put("vnp_IpAddr", VNPayConfig.getIpAddress());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentUrl(paymentUrl);

        return paymentResponse;
    }

    @Override
    public PaymentResponse createPaymentByAccountId(PaymentRequest paymentRequest) throws UnsupportedEncodingException {

        CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByAccountId(paymentRequest.getAccountId());
        // Validate Account
        if (customAccountInfoDTO == null)
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());
        if (!customAccountInfoDTO.getRole().equals(RoleEnum.USER.getRoleName()))
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_ROLE, ToiletMapErrorCodeEnum.INVALID_ROLE.getMessage());

        // Validate Payment Method
        if (!paymentRequest.getMethod().equals(PaymentTypeEnum.VN_PAY.getPaymentValue())
                && !paymentRequest.getMethod().equals(PaymentTypeEnum.CASH.getPaymentValue())
                && !paymentRequest.getMethod().equals(PaymentTypeEnum.BANK_TRANSFER.getPaymentValue()))
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_PAYMENT_METHOD, ToiletMapErrorCodeEnum.INVALID_PAYMENT_METHOD.getMessage());

        // Create payment for VNPay payment method
        if (paymentRequest.getMethod().equals(PaymentTypeEnum.VN_PAY.getPaymentValue())) {
            return createPaymentUrlForVNPay(paymentRequest);
        }

        // Save Payment Entity
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

        // Prepare pagination & sort
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.CREATED_DATE);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        // Validate Account
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());

        List<PaymentEntity> paymentEntities = paymentRepository.findAllByAccountId(accountId, pageable);

        return paymentEntities.stream()
                .map(dto -> paymentMapper.convertPaymentEntityToPaymentResponse(dto))
                .collect(Collectors.toList());
    }

    @Override
    public int count(Integer accountId) {

        // Validate Account
        if (accountId != null) {
            Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
            if (!accountEntity.isPresent())
                throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());

            return (int) paymentRepository.countByAccountId(accountId);
        }

        return (int) paymentRepository.count();
    }

    @Override
    public PaymentResponse VNPayResponse(
            String vnp_Amount,
            String vnp_BankCode,
            String vnp_BankTranNo,
            String vnp_CardType,
            String vnp_OrderInfo,
            String vnp_PayDate,
            String vnp_ResponseCode,
            int vnp_TransactionNo) throws Exception {

        switch (vnp_ResponseCode) {
            case "09":
                throw new Exception(ToiletMapErrorCodeEnum.VNPAY_CARD_NOT_REGISTERED_INTERNETBANKING.getMessage());
            case "10":
                throw new Exception(ToiletMapErrorCodeEnum.VNPAY_VERIFY_CARD_NOT_CORRECT.getMessage());
            case "11":
                throw new Exception(ToiletMapErrorCodeEnum.VNPAY_PAYMENT_EXPIRED.getMessage());
            case "12":
                throw new Exception(ToiletMapErrorCodeEnum.VNPAY_LOCKED_CARD.getMessage());
            case "13":
                throw new Exception(ToiletMapErrorCodeEnum.VNPAY_WRONG_OTP.getMessage());
            case "24":
                throw new Exception(ToiletMapErrorCodeEnum.VNPAY_CUSTOMER_CANCEL_TRANSACTION.getMessage());
            case "51":
                throw new Exception(ToiletMapErrorCodeEnum.VNPAY_NOT_ENOUGH_BALANCE.getMessage());
            case "65":
                throw new Exception(ToiletMapErrorCodeEnum.VNPAY_EXCEEDED_DAILY_TRANSACTION_LIMIT.getMessage());
            case "75":
                throw new Exception(ToiletMapErrorCodeEnum.VNPAY_BANK_UNDER_MAINTENANCE.getMessage());
            case "79":
                throw new Exception(ToiletMapErrorCodeEnum.VNPAY_WRONG_PASSWORD.getMessage());
            case "99":
                throw new Exception(ToiletMapErrorCodeEnum.VNPAY_ERROR.getMessage());
            default:
                int total = Integer.parseInt(vnp_Amount) / 100;
                int accountId = Integer.parseInt(vnp_OrderInfo);
                Timestamp createdDate = DateTimeUtil.convertDateToTimestamp(DateTimeUtil.convertStringToDate(vnp_PayDate, DateTimeConstant.yyyyMMddHHmmss));

                // Save Payment Entity
                PaymentEntity paymentEntity = new PaymentEntity();
                paymentEntity.setAccountId(accountId);
                paymentEntity.setTotal(total);
                paymentEntity.setMethod(PaymentTypeEnum.VN_PAY.getPaymentValue());
                paymentEntity.setCreatedDate(createdDate);
                paymentEntity = paymentRepository.save(paymentEntity);

                // Add money to account balance by accountId
                CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByAccountId(accountId);
                int newAccountBalance = customAccountInfoDTO.getAccountBalance() + total;
                userInfoRepository.updateAccountBalance(accountId, newAccountBalance);

                // Save Transaction Entity
                transactionRepository.saveTransaction(
                        vnp_TransactionNo,
                        paymentEntity.getId(),
                        vnp_BankCode,
                        vnp_BankTranNo,
                        vnp_CardType,
                        createdDate
                );

                return paymentMapper.convertPaymentEntityToPaymentResponse(paymentEntity);
        }
    }
}
