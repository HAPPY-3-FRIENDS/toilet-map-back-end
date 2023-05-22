package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.entity.SensitiveWordEntity;
import com.happy3friends.toiletmapbackend.repository.SensitiveWordRepository;
import com.happy3friends.toiletmapbackend.service.SensitiveWordService;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void create(String word) {
        SensitiveWordEntity sensitiveWordEntity = new SensitiveWordEntity();
        sensitiveWordEntity.setWord(word);
        sensitiveWordRepository.save(sensitiveWordEntity);
    }
}
