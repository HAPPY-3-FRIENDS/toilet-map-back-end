package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.entity.ComboEntity;
import com.happy3friends.toiletmapbackend.entity.OrderEntity;
import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.OrderMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.ComboRepository;
import com.happy3friends.toiletmapbackend.repository.OrderRepository;
import com.happy3friends.toiletmapbackend.repository.UserInfoRepository;
import com.happy3friends.toiletmapbackend.request.OrderRequest;
import com.happy3friends.toiletmapbackend.response.OrderResponse;
import com.happy3friends.toiletmapbackend.service.OrderService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ComboRepository comboRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderResponse createOrderByAccountId(OrderRequest orderRequest) {

        // Validate Payment Method
        if (!orderRequest.getPaymentMethod().equals(PaymentTypeEnum.BALANCE.getPaymentValue())
            && !orderRequest.getPaymentMethod().equals(PaymentTypeEnum.VN_PAY.getPaymentValue())
            && !orderRequest.getPaymentMethod().equals(PaymentTypeEnum.BANK_TRANSFER.getPaymentValue()))
            throw new BadRequestException("Invalid payment method!");

        // Validate Role - Check account's role is User role
        CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByAccountId(orderRequest.getAccountId());
        if (!customAccountInfoDTO.getRole().equals(RoleEnum.USER.getRoleName()))
            throw new BadRequestException("Invalid account Id!");

        // TODO: check userToken with account from accountId

        // Validate Combo
        Optional<ComboEntity> comboEntity = comboRepository.findById(orderRequest.getComboId());
        if (!comboEntity.isPresent())
            throw new NotFoundException("Combo", "Id", orderRequest.getComboId());

        // Compare Account balance with Combo Price
        int comboPrice = comboEntity.get().getPrice();
        if (orderRequest.getPaymentMethod().equals(PaymentTypeEnum.BALANCE.getPaymentValue())
            && customAccountInfoDTO.getAccountBalance() < comboPrice)
            throw new BadRequestException("Your account balance is not greater than combo price!");

        Timestamp now = DateTimeUtil.getTimestampNow();
        // Create order - combo
        orderRepository.createOrderByAccountId(orderRequest.getAccountId(),
                comboEntity.get().getTotalTurn(),
                comboEntity.get().getPrice(),
                orderRequest.getPaymentMethod(),
                now);

        // Update userinfo with new accountTurn and accountBalance (if paymentMethod is balance)
        int newAccountTurn = customAccountInfoDTO.getAccountTurn() + comboEntity.get().getTotalTurn();
        if (orderRequest.getPaymentMethod().equals(PaymentTypeEnum.BALANCE.getPaymentValue())) {
            int newAccountBalance = customAccountInfoDTO.getAccountBalance() - comboPrice;
            userInfoRepository.updateAccountBalanceAndAccountTurn(orderRequest.getAccountId(), newAccountBalance, newAccountTurn);
        } else {
            userInfoRepository.updateAccountTurn(orderRequest.getAccountId(), newAccountTurn);
        }

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setAccountId(orderRequest.getAccountId());
        orderEntity.setTotalTurn(comboEntity.get().getTotalTurn());
        orderEntity.setTotalPrice(comboPrice);
        orderEntity.setPaymentMethod(orderRequest.getPaymentMethod());
        orderEntity.setDateTime(now);

        return orderMapper.convertOrderEntityToOrderResponse(orderEntity);
    }

    @Override
    public List<OrderResponse> getOrderHistoriesByAccountId(int accountId, BasePaginationRequest paginationRequest) {

        // Prepare pagination & sort
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.DATETIME);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        // Validate Account
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent()) throw new NotFoundException("Account", "Id", accountId);

        List<OrderEntity> orderEntities = orderRepository.findAllByAccountId(accountId, pageable);

        return orderEntities.stream()
                .map(entity -> orderMapper.convertOrderEntityToOrderResponse(entity))
                .collect(Collectors.toList());
    }

    @Override
    public int count(Integer accountId) {

        if (accountId != null) {
            Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
            if (!accountEntity.isPresent()) throw new NotFoundException("Account", "Id", accountId);

            return orderRepository.countOrderHistoriesByAccountId(accountId);
        }

        return (int) orderRepository.count();
    }
}
