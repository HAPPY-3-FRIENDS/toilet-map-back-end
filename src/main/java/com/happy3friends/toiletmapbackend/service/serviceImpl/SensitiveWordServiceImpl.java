package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.entity.SensitiveWordEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.repository.SensitiveWordRepository;
import com.happy3friends.toiletmapbackend.request.SensitiveWordRequest;
import com.happy3friends.toiletmapbackend.service.SensitiveWordService;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {

    @Autowired
    private SensitiveWordRepository sensitiveWordRepository;

    @Override
    public List<SensitiveWordEntity> getAllSensitiveWords(BasePaginationRequest basePaginationRequest) {
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.ASC, DefaultSortPropertyConstant.WORD);
        Pageable pageable = PaginationUtil.getPageable(basePaginationRequest, defaultSortOrder);

        Page<SensitiveWordEntity> sensitiveWordEntities = sensitiveWordRepository.findAll(pageable);

        return sensitiveWordEntities.getContent();
    }

    @Override
    public int count() {
        return (int) sensitiveWordRepository.count();
    }

    @Override
    public void create(SensitiveWordRequest sensitiveWordRequest) {
        SensitiveWordEntity sensitiveWordEntity = new SensitiveWordEntity();
        sensitiveWordEntity.setWord(sensitiveWordRequest.getWord());
        sensitiveWordRepository.save(sensitiveWordEntity);
    }

    @Override
    public SensitiveWordEntity update(int id, SensitiveWordRequest sensitiveWordRequest) {

        Optional<SensitiveWordEntity> sensitiveWordEntity = sensitiveWordRepository.findById(id);
        if (!sensitiveWordEntity.isPresent())
            throw new BadRequestException(ToiletMapErrorCodeEnum.NOT_FOUND_SENSITIVE_WORD, ToiletMapErrorCodeEnum.NOT_FOUND_SENSITIVE_WORD.getMessage());

        sensitiveWordEntity.get().setWord(sensitiveWordRequest.getWord());
        sensitiveWordRepository.save(sensitiveWordEntity.get());

        return sensitiveWordEntity.get();
    }

    @Override
    public void delete(int id) {

        Optional<SensitiveWordEntity> sensitiveWordEntity = sensitiveWordRepository.findById(id);
        if (!sensitiveWordEntity.isPresent())
            throw new BadRequestException(ToiletMapErrorCodeEnum.NOT_FOUND_SENSITIVE_WORD, ToiletMapErrorCodeEnum.NOT_FOUND_SENSITIVE_WORD.getMessage());

        sensitiveWordRepository.delete(sensitiveWordEntity.get());
    }

    @Override
    public SensitiveWordEntity getById(int id) {

        Optional<SensitiveWordEntity> sensitiveWordEntity = sensitiveWordRepository.findById(id);
        if (!sensitiveWordEntity.isPresent())
            throw new BadRequestException(ToiletMapErrorCodeEnum.NOT_FOUND_SENSITIVE_WORD, ToiletMapErrorCodeEnum.NOT_FOUND_SENSITIVE_WORD.getMessage());

        return sensitiveWordEntity.get();
    }
}
