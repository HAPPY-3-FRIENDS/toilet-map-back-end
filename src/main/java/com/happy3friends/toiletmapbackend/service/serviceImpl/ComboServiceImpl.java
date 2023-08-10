package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.ComboEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.ComboMapper;
import com.happy3friends.toiletmapbackend.repository.ComboRepository;
import com.happy3friends.toiletmapbackend.request.ComboRequest;
import com.happy3friends.toiletmapbackend.response.ComboResponse;
import com.happy3friends.toiletmapbackend.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComboServiceImpl implements ComboService {

    @Autowired
    private ComboRepository comboRepository;

    @Autowired
    private ComboMapper comboMapper;

    @Override
    public List<ComboResponse> getAllCombo() {
        List<ComboEntity> comboEntities = comboRepository.findAll();
        if (comboEntities.isEmpty())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMBO, ToiletMapErrorCodeEnum.NOT_FOUND_COMBO.getMessage());

        return comboEntities.stream()
                .map(entity -> comboMapper.convertComboEntityToComboResponse(entity))
                .collect(Collectors.toList());
    }

    @Override
    public ComboResponse getComboIdByComboId(int comboId) {
        Optional<ComboEntity> comboEntity = comboRepository.findById(comboId);
        if (comboEntity.isEmpty())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMBO, ToiletMapErrorCodeEnum.NOT_FOUND_COMBO.getMessage());

        return comboMapper.convertComboEntityToComboResponse(comboEntity.get());
    }

    @Override
    public ComboResponse updateComboByComboId(int comboId, ComboRequest comboRequest) {
        Optional<ComboEntity> comboEntity = comboRepository.findById(comboId);
        if (comboEntity.isEmpty())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMBO, ToiletMapErrorCodeEnum.NOT_FOUND_COMBO.getMessage());

        if (comboRequest.getTotalTurn() == 0)
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_COMBO_TOTALTURN_QUANTITY, ToiletMapErrorCodeEnum.INVALID_COMBO_TOTALTURN_QUANTITY.getMessage());

        if (comboRequest.getPrice() <= 0)
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_PRICE, ToiletMapErrorCodeEnum.INVALID_PRICE.getMessage());

        comboEntity.get().setTotalTurn(comboRequest.getTotalTurn());
        comboEntity.get().setPrice(comboRequest.getPrice());
        comboRepository.save(comboEntity.get());

        return comboMapper.convertComboEntityToComboResponse(comboEntity.get());
    }

    @Override
    public ComboResponse createCombo(ComboRequest comboRequest) {
        if (comboRequest.getTotalTurn() == 0)
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_COMBO_TOTALTURN_QUANTITY, ToiletMapErrorCodeEnum.INVALID_COMBO_TOTALTURN_QUANTITY.getMessage());

        if (comboRequest.getPrice() <= 0)
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_PRICE, ToiletMapErrorCodeEnum.INVALID_PRICE.getMessage());

        ComboEntity comboEntity = comboRepository.findByTotalTurn(comboRequest.getTotalTurn());
        if (comboEntity != null)
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_COMBO_TOTALTURN_QUANTITY, ToiletMapErrorCodeEnum.INVALID_COMBO_TOTALTURN_QUANTITY.getMessage());

        ComboEntity savingComboEntity = comboMapper.convertComboRequestToComboEntity(comboRequest);
        ComboEntity savedComboEntity = comboRepository.save(savingComboEntity);

        return comboMapper.convertComboEntityToComboResponse(savedComboEntity);
    }
}
