package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.dto.CustomRatingDetailsDTO;
import com.happy3friends.toiletmapbackend.entity.*;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.RatingMapper;
import com.happy3friends.toiletmapbackend.repository.*;
import com.happy3friends.toiletmapbackend.request.FilterRatingRequest;
import com.happy3friends.toiletmapbackend.request.RatingRequest;
import com.happy3friends.toiletmapbackend.response.RatingResponse;
import com.happy3friends.toiletmapbackend.service.RatingService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private RatingCommonCommentRepository ratingCommonCommentRepository;

    private LinkedHashMap<Integer, List<CustomRatingDetailsDTO>> getMapIdListCustomRatingDetailsDTO(
            List<CustomRatingDetailsDTO> customRatingDetailsDTOS) {

        return customRatingDetailsDTOS.stream()
                .collect(Collectors.groupingBy(
                        CustomRatingDetailsDTO::getId,
                        LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));
    }

    private RatingResponse getRatingResponseFromListCustomRatingDetailsDTO(
            List<CustomRatingDetailsDTO> customRatingDetailsDTOS) {

        // Prepare list rating-image-sources DTO
        List<String> imageSources = customRatingDetailsDTOS.stream()
                .map(CustomRatingDetailsDTO::getImageSource)
                .collect(Collectors.toList());

        List<String> resultImageSources = imageSources.stream()
                .distinct()
                .collect(Collectors.toList());

        List<String> commonComments = customRatingDetailsDTOS.stream()
                .map(CustomRatingDetailsDTO::getCommonComment)
                .collect(Collectors.toList());

        List<String> resultCommonComments = commonComments.stream()
                .distinct()
                .collect(Collectors.toList());

        return new RatingResponse(
                customRatingDetailsDTOS.get(0).getId(),
                customRatingDetailsDTOS.get(0).getFullName(),
                customRatingDetailsDTOS.get(0).getStar(),
                customRatingDetailsDTOS.get(0).getComment(),
                customRatingDetailsDTOS.get(0).getDateTime(),
                resultImageSources,
                customRatingDetailsDTOS.get(0).getAvatar(),
                customRatingDetailsDTOS.get(0).getStatus(),
                resultCommonComments
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
        sortOrders.add(new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.DATETIME));
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, sortOrders);
        List<String> listSort = new ArrayList<>();
        pageable.getSort().forEach(sort -> {
            listSort.add(sort.getProperty() + " " + sort.getDirection());
        });
        String strListSort = StringUtils.join(listSort, ",");

        List<CustomRatingDetailsDTO> customRatingDetailsDTOS
                = ratingRepository.getAllRatingsByToiletId(
                        toiletId,
                        paginationRequest.getPageSize(),
                        paginationRequest.getPageIndex(),
                        strListSort);

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
        // Validate only rating one time in a check-in
        boolean checkRating = ratingRepository.existsByCheckInId(ratingRequest.getCheckInId());
        if (checkRating) {
            throw new NotFoundException(ToiletMapErrorCodeEnum.EXISTED_RATING, ToiletMapErrorCodeEnum.EXISTED_RATING.getMessage());
        }

        // Validate expired rating
        Optional<CheckInEntity> checkInEntity = checkInRepository.findById(ratingRequest.getCheckInId());
        if (checkInEntity.get().getDateTime().compareTo(new Timestamp(DateTimeUtil.getTimestampNow().getTime() - 8 * 60 * 60 * 1000)) < 0) {
            throw new NotFoundException(ToiletMapErrorCodeEnum.EXPIRED_RATING, ToiletMapErrorCodeEnum.EXPIRED_RATING.getMessage());
        }

        // Save Rating Entity
        LOGGER.info("-- Create Rating - Start save Rating Entity! --");
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

        // Prepare Rating Common Comment Entity for saving
        if (ratingRequest.getCommonComments() != null) {
            LOGGER.info("-- Create Rating - Start save List Rating Common Comment Entity! --");
            List<RatingCommonCommentEntity> ratingCommonCommentEntities = ratingRequest.getCommonComments().stream()
                    .map(o -> {
                        RatingCommonCommentEntity ratingCommonCommentEntity = new RatingCommonCommentEntity();
                        ratingCommonCommentEntity.setRatingId(savedRatingEntity.getId());
                        ratingCommonCommentEntity.setCommonCommentId(o);

                        return ratingCommonCommentEntity;
                    }).collect(Collectors.toList());
            ratingCommonCommentRepository.saveAll(ratingCommonCommentEntities);
            LOGGER.info("-- Create Rating - Finish save List Rating Common Comment Entity! --");
        }

        // Return rating response
        return new RatingResponse(
                savedRatingEntity.getId(),
                customAccountInfoDTO.getFullName(),
                ratingRequest.getStar(),
                ratingRequest.getComment(),
                new Date(timestampNow.getTime()),
                ratingRequest.getImageSources(),
                null,
                null,
                null
        );
    }

    @Override
    public int count(Integer toiletId) {

        if (toiletId != null) {
            Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
            if (!toiletEntity.isPresent()) {
                throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());
            }

            return (int) ratingRepository.countByToiletId(toiletId);
        }

        return (int) ratingRepository.count();
    }

    @Override
    public RatingResponse update(int id, Map<String, Object> fields) {
        Optional<RatingEntity> ratingEntity = ratingRepository.findById(id);
        if (!ratingEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_RATING, ToiletMapErrorCodeEnum.NOT_FOUND_RATING.getMessage());

        fields.forEach((key, value) -> {
            if (key.equals("status")) {
                ratingEntity.get().setStatus(value.toString());
            }
        });

        RatingEntity entity = ratingRepository.save(ratingEntity.get());

        return ratingMapper.convertRatingEntityToRatingResponse(ratingEntity.get());
    }

    @Override
    public List<RatingResponse> filterRatingByStar(Integer toiletId, Integer star, BasePaginationRequest paginationRequest) {
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.DATETIME);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        List<CustomRatingDetailsDTO> customRatingDetailsDTOS = ratingRepository.filterRatingByStar(toiletId, star, paginationRequest.getPageSize(), paginationRequest.getPageIndex());

        return getListRatingResponseFromListCustomRatingDetailsDTO(customRatingDetailsDTOS);
    }

    @Override
    public int countTheListRatingWhenFilterByStar(Integer toiletId, Integer star) {

        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent()) {
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());
        }

        return ratingRepository.countRatingByStar(toiletId, star);
    }

    @Override
    public List<RatingResponse> filterRating(FilterRatingRequest request, BasePaginationRequest paginationRequest) {

        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(request.getToiletId());
        if (!toiletEntity.isPresent()) {
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());
        }

        List<CustomRatingDetailsDTO> customRatingDetailsDTOS = ratingRepository.filterRating(request.getToiletId(),
                request.getListCommonComment(),
                request.getListStars(),
                request.getListStatus(),
                paginationRequest.getPageSize(),
                paginationRequest.getPageIndex());

        return getListRatingResponseFromListCustomRatingDetailsDTO(customRatingDetailsDTOS);
    }

    @Override
    public int countFilterRating(FilterRatingRequest request) {

        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(request.getToiletId());
        if (!toiletEntity.isPresent()) {
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());
        }

        List<CustomRatingDetailsDTO> customRatingDetailsDTOS = ratingRepository.filterRatingToCount(request.getToiletId(),
                request.getListCommonComment(),
                request.getListStars(),
                request.getListStatus());
        return getListRatingResponseFromListCustomRatingDetailsDTO(customRatingDetailsDTOS).size();
    }
}
