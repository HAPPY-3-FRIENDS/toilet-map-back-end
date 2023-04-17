package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.dto.CustomRatingDetailsDTO;
import com.happy3friends.toiletmapbackend.entity.RatingEntity;
import com.happy3friends.toiletmapbackend.entity.RatingImageEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.RatingMapper;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import com.happy3friends.toiletmapbackend.repository.RatingImageRepository;
import com.happy3friends.toiletmapbackend.repository.RatingRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
import com.happy3friends.toiletmapbackend.request.RatingRequest;
import com.happy3friends.toiletmapbackend.response.RatingResponse;
import com.happy3friends.toiletmapbackend.service.RatingService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingServiceImpl.class);

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingImageRepository ratingImageRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RatingMapper ratingMapper;

    private LinkedHashMap<Integer, List<CustomRatingDetailsDTO>> getMapIdListCustomRatingDetailsDTO(
            List<CustomRatingDetailsDTO> customRatingDetailsDTOS) {

        return customRatingDetailsDTOS.stream()
                .collect(Collectors.groupingBy(
                        CustomRatingDetailsDTO::getStar,
                        LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));
    }

    private RatingResponse getRatingResponseFromListCustomRatingDetailsDTO(
            List<CustomRatingDetailsDTO> customRatingDetailsDTOS) {

        // Prepare list rating-image-sources DTO
        List<String> imageSources = customRatingDetailsDTOS.stream()
                .map(CustomRatingDetailsDTO::getImageSource)
                .collect(Collectors.toList());

        return new RatingResponse(
                customRatingDetailsDTOS.get(0).getId(),
                customRatingDetailsDTOS.get(0).getFullName(),
                customRatingDetailsDTOS.get(0).getStar(),
                customRatingDetailsDTOS.get(0).getComment(),
                customRatingDetailsDTOS.get(0).getDateTime(),
                imageSources
        );
    }

    private List<RatingResponse> getListRatingResponseFromListCustomRatingDetailsDTO(
            List<CustomRatingDetailsDTO> customRatingDetailsDTOS
    ) {
        LinkedHashMap<Integer, List<CustomRatingDetailsDTO>> mapIdListCustomRatingDetailsDTO
                = getMapIdListCustomRatingDetailsDTO(customRatingDetailsDTOS);

        return mapIdListCustomRatingDetailsDTO.values()
                .stream().map(this::getRatingResponseFromListCustomRatingDetailsDTO)
                .collect(Collectors.toList());
    }

    private List<RatingResponse> getAllRatingsByToiletId(Integer toiletId, BasePaginationRequest paginationRequest) {
        // Validate Toilet
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

        // Prepare pagination & sort
        List<Sort.Order> sortOrders = new ArrayList<>();
        sortOrders.add(new Sort.Order(Sort.Direction.ASC, DefaultSortPropertyConstant.STAR));
        sortOrders.add(new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.DATETIME));
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, sortOrders);

        List<CustomRatingDetailsDTO> customRatingDetailsDTOS = ratingRepository.getAllRatingsByToiletId(toiletId, pageable);

        return getListRatingResponseFromListCustomRatingDetailsDTO(customRatingDetailsDTOS);
    }

    @Override
    public List<RatingResponse> getAllRatings(Integer toiletId, BasePaginationRequest paginationRequest) {

        if (toiletId != null) {
            return getAllRatingsByToiletId(toiletId, paginationRequest);
        } else { // If toiletId == null --> find all Ratings with pagination and default sort by datetime
            // Prepare pagination & sort
            Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.DATETIME);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

            List<CustomRatingDetailsDTO> customRatingDetailsDTOS = ratingRepository.getAllRatings(pageable);

            return getListRatingResponseFromListCustomRatingDetailsDTO(customRatingDetailsDTOS);
        }
    }

    @Override
    public RatingResponse createRating(RatingRequest ratingRequest) {

        // Validate Toilet
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(ratingRequest.getToiletId());
        if (!toiletEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

        // Validate Account
        CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByAccountId(ratingRequest.getAccountId());
        if (customAccountInfoDTO == null)
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT, ToiletMapErrorCodeEnum.NOT_FOUND_ACCOUNT.getMessage());
        if (!customAccountInfoDTO.getRole().equals(RoleEnum.USER.getRoleName()))
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_ROLE, ToiletMapErrorCodeEnum.INVALID_ROLE.getMessage());
        // TODO: validate account with check-in for rating

        // Save Rating Entity
        LOGGER.info("-- Create Rating - Start save Rating Entity! --");
        // TODO: Common rating comment
        Timestamp timestampNow = DateTimeUtil.getTimestampNow();
        RatingEntity ratingEntity = ratingMapper.convertRatingRequestToRatingEntity(ratingRequest);
        ratingEntity.setToiletId(ratingEntity.getToiletId());
        ratingEntity.setDateTime(timestampNow);
        RatingEntity savedRatingEntity = ratingRepository.save(ratingEntity);
        LOGGER.info("-- Create Rating - Finish save Rating Entity! --");

        // Prepare Rating Image Entity for saving
        if (ratingRequest.getImageSources() != null) {
            LOGGER.info("-- Create Rating - Start save List Rating Image Entity! --");
            List<RatingImageEntity> ratingImageEntities = ratingRequest.getImageSources().stream()
                    .map(o -> {
                        RatingImageEntity ratingImageEntity = new RatingImageEntity();
                        ratingImageEntity.setRatingId(savedRatingEntity.getId());
                        ratingImageEntity.setImageSource(o);

                        return ratingImageEntity;
                    }).collect(Collectors.toList());
            // Save Rating Image
            ratingImageRepository.saveAll(ratingImageEntities);
            LOGGER.info("-- Create Rating - Finish save List Rating Image Entity! --");
        }

        // Return rating response
        return new RatingResponse(
                savedRatingEntity.getId(),
                customAccountInfoDTO.getFullName(),
                ratingRequest.getStar(),
                ratingRequest.getComment(),
                new Date(timestampNow.getTime()),
                ratingRequest.getImageSources()
        );
    }
}
