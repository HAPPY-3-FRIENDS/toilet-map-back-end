package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.constant.StatusConstant;
import com.happy3friends.toiletmapbackend.entity.CommonCommentEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.CommonCommentMapper;
import com.happy3friends.toiletmapbackend.repository.CommonCommentRepository;
import com.happy3friends.toiletmapbackend.request.CommonCommentRequest;
import com.happy3friends.toiletmapbackend.response.CommonCommentResponse;
import com.happy3friends.toiletmapbackend.service.CommonCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommonCommentServiceImpl implements CommonCommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

    @Autowired
    private CommonCommentRepository commonCommentRepository;

    @Autowired
    private CommonCommentMapper commonCommentMapper;

    @Override
    public List<CommonCommentResponse> getAllCommonComment() {
        List<CommonCommentEntity> commonCommentEntities = commonCommentRepository.findAll();
        if (commonCommentEntities.isEmpty())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMMON_COMMENT, ToiletMapErrorCodeEnum.NOT_FOUND_COMMON_COMMENT.getMessage());

        return commonCommentEntities.stream()
                .map(entity -> commonCommentMapper.convertCommonCommentEntityToCommonCommentResponse(entity))
                .collect(Collectors.toList());
    }

    @Override
    public CommonCommentResponse createCommonComment(CommonCommentRequest request) {

        CommonCommentEntity entity = new CommonCommentEntity();
        entity.setName(request.getName());
        entity.setStatus(StatusConstant.COMMON_COMMENT_ACTIVE);

        return commonCommentMapper.convertCommonCommentEntityToCommonCommentResponse(commonCommentRepository.save(entity));
    }

    @Override
    public CommonCommentResponse updateCommonComment(Integer id, Map<String, Object> fields) {
        Optional<CommonCommentEntity> commonCommentEntity = commonCommentRepository.findById(id);
        if (!commonCommentEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMMON_COMMENT, ToiletMapErrorCodeEnum.NOT_FOUND_COMMON_COMMENT.getMessage());
        LOGGER.info("-- Update Common Comment - Start save Common Comment Entity and its information! --");
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(CommonCommentEntity.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, commonCommentEntity.get(), value);
        });

        CommonCommentEntity entity = commonCommentRepository.save(commonCommentEntity.get());
        LOGGER.info("-- Update Common Comment - Finish save Common Comment Entity and its information! --");
        return commonCommentMapper.convertCommonCommentEntityToCommonCommentResponse(entity);
    }

    @Override
    public boolean delete(Integer id) {
        boolean result = true;

        Optional<CommonCommentEntity> commonCommentEntity = commonCommentRepository.findById(id);
        if (!commonCommentEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMMON_COMMENT, ToiletMapErrorCodeEnum.NOT_FOUND_COMMON_COMMENT.getMessage());
        try {
            commonCommentRepository.deleteById(id);
        } catch (Exception e) {
            result = false;
        }

        return result;
    }
}
