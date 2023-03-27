package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.CustomCheckInDTO;
import com.happy3friends.toiletmapbackend.entity.AccountEntity;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.CheckInMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.CheckInRepository;
import com.happy3friends.toiletmapbackend.response.CheckInResponse;
import com.happy3friends.toiletmapbackend.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CheckInServiceImpl implements CheckInService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private CheckInMapper checkInMapper;

    @Override
    public List<CheckInResponse> getCheckInHistoriesByAccountId(int accountId, String paymentMethod) {
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (!accountEntity.isPresent()) throw new NotFoundException("Account", "Id", accountId);

        List<CustomCheckInDTO> customCheckInDTOS = checkInRepository.getCheckInHistoriesByAccountId(accountId, paymentMethod);

        return customCheckInDTOS.stream()
                .map(dto -> checkInMapper.convertCustomCheckInDTOToCheckInResponse(dto))
                .collect(Collectors.toList());
    }
}
