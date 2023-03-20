package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.entity.ComboEntity;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.ComboMapper;
import com.happy3friends.toiletmapbackend.repository.ComboRepository;
import com.happy3friends.toiletmapbackend.response.ComboResponse;
import com.happy3friends.toiletmapbackend.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
            throw new NotFoundException("List of all combos is not found!");

        return comboEntities.stream()
                .map(entity -> comboMapper.convertComboEntityToComboResponse(entity))
                .collect(Collectors.toList());
    }
}
