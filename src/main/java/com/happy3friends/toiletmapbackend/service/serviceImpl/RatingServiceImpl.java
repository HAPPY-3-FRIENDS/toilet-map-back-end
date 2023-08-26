package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.dto.CustomRatingDetailsDTO;
import com.happy3friends.toiletmapbackend.dto.RatingDetailsDTO;
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
import com.happy3friends.toiletmapbackend.utils.FilterKeysUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Autowired
    private SensitiveWordRepository sensitiveWordRepository;

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

    private List<RatingResponse> getAllRatingsByToiletId(Integer toiletId, FilterRatingRequest filterRatingRequest, BasePaginationRequest paginationRequest) {
        // Validate Toilet
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
        if (!toiletEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

        // Prepare pagination & sort
        List<Sort.Order> sortOrders = new ArrayList<>();
        sortOrders.add(new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.DATETIME));
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, sortOrders);

        String strListIdCommonComment = null;
        String strListStars = null;
        String strListStatus = null;
        if (null != filterRatingRequest.getListIdCommonComment()) {
            List<String> listIdCommonComment = filterRatingRequest.getListIdCommonComment().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            strListIdCommonComment = String.join(",", listIdCommonComment);
        }
        if (null != filterRatingRequest.getListStars()) {
            List<String> listStars = filterRatingRequest.getListStars().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            strListStars = String.join(",", listStars);
        }
        if (null != filterRatingRequest.getListStatus()) {
            strListStatus = String.join(",", filterRatingRequest.getListStatus());
        }

        // Get list rating by toilet Id without rating image and common comment
        List<CustomRatingDetailsDTO> customRatingDetailsDTOS
                = ratingRepository.getAllRatingsByToiletId(
                        toiletId,
                        strListIdCommonComment,
                        strListStars,
                        strListStatus,
                        pageable);
        List<RatingDetailsDTO> ratingDetailsDTOS =
                customRatingDetailsDTOS
                        .stream()
                        .map(o -> ratingMapper.convertCustomRatingDetailsDTOToRatingDetailsDTO(o))
                        .collect(Collectors.toList());

        // List rating add rating image and common comment
        List<Integer> lstRatingIds = customRatingDetailsDTOS.stream().map(o -> o.getId()).collect(Collectors.toList());
        List<CustomRatingDetailsDTO> listRatingImageAndRatingCommonComment
                = ratingRepository.getAllRatingImageAndRatingCommonCommentByListRatingIds(lstRatingIds);
        HashMap<Integer, List<CustomRatingDetailsDTO>> mapIdsListImageAndComment
                = getMapIdListCustomRatingDetailsDTO(listRatingImageAndRatingCommonComment);

        List<RatingDetailsDTO> ratingDetailsDTOList = ratingDetailsDTOS.stream()
                .map(o -> {
                    List<CustomRatingDetailsDTO> lstImageComment = mapIdsListImageAndComment.get(o.getId());
                    List<String> imageSources = lstImageComment.stream()
                            .filter(FilterKeysUtil.distinctByKeys(CustomRatingDetailsDTO::getImageSource))
                            .map(CustomRatingDetailsDTO::getImageSource)
                            .collect(Collectors.toList());
                    List<String> comments = lstImageComment.stream()
                            .filter(FilterKeysUtil.distinctByKeys(CustomRatingDetailsDTO::getCommonComment))
                            .map(CustomRatingDetailsDTO::getCommonComment)
                            .collect(Collectors.toList());
                    o.setImageSources(imageSources);
                    o.setCommonComments(comments);

                    return o;
                })
                .collect(Collectors.toList());

        return ratingDetailsDTOList.stream()
                .map(dto -> ratingMapper.convertRatingDetailsDTOToRatingResponse(dto))
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingResponse> getAllRatings(Integer toiletId, FilterRatingRequest filterRatingRequest, BasePaginationRequest paginationRequest) {

        if (toiletId != null) {
            return getAllRatingsByToiletId(toiletId, filterRatingRequest, paginationRequest);
        } else { // If toiletId == null --> find all Ratings with pagination and default sort by datetime
            // Prepare pagination & sort
            Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.DESC, DefaultSortPropertyConstant.DATETIME);
            Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

            List<CustomRatingDetailsDTO> customRatingDetailsDTOS = ratingRepository.getAllRatings(pageable);

            return getListRatingResponseFromListCustomRatingDetailsDTO(customRatingDetailsDTOS);
        }
    }

    private String replaceSensitiveContent(final String text, Map<String,String> tokens) {

        String patternString = StringUtils.join(tokens.keySet(), "|");
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        StringBuilder sb = new StringBuilder();
        while(matcher.find()) {
            if (!tokens.isEmpty()) {
                matcher.appendReplacement(sb, tokens.get(matcher.group(0).toLowerCase()));
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
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
        if (checkInEntity.get().getDateTime().compareTo(new Timestamp(DateTimeUtil.getTimestampNow().getTime() - 60 * 60 * 1000)) < 0) {
            throw new NotFoundException(ToiletMapErrorCodeEnum.EXPIRED_RATING, ToiletMapErrorCodeEnum.EXPIRED_RATING.getMessage());
        }

        // Save Rating Entity
        // Hide keywords which are sensitive in comment content
        List<String> sensitiveWords = sensitiveWordRepository.getListSensitiveWordsFromContent(ratingRequest.getComment());
        Map<String, String> map = new HashMap<>();
        sensitiveWords.forEach(s -> map.put(s.toLowerCase(), "***"));
        ratingRequest.setComment(replaceSensitiveContent(ratingRequest.getComment(), map));
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
    public int count(Integer toiletId, FilterRatingRequest filterRatingRequest) {

        if (toiletId != null) {
            Optional<ToiletEntity> toiletEntity = toiletRepository.findById(toiletId);
            if (!toiletEntity.isPresent()) {
                throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());
            }

            String strListIdCommonComment = null;
            String strListStars = null;
            String strListStatus = null;
            if (null != filterRatingRequest.getListIdCommonComment()) {
                List<String> listIdCommonComment = filterRatingRequest.getListIdCommonComment().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                strListIdCommonComment = String.join(",", listIdCommonComment);
            }
            if (null != filterRatingRequest.getListStars()) {
                List<String> listStars = filterRatingRequest.getListStars().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
                strListStars = String.join(",", listStars);
            }
            if (null != filterRatingRequest.getListStatus()) {
                strListStatus = String.join(",", filterRatingRequest.getListStatus());
            }

            return (int) ratingRepository.countByToiletId(
                    toiletId,
                    strListIdCommonComment,
                    strListStars,
                    strListStatus);
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

    @Override
    public RatingResponse getRatingByRatingId(int ratingId) {
        Optional<RatingEntity> ratingEntity = ratingRepository.findById(ratingId);
        if (!ratingEntity.isPresent()) {
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_RATING, ToiletMapErrorCodeEnum.NOT_FOUND_RATING.getMessage());
        }

        List<CustomRatingDetailsDTO> customRatingDetailsDTOS = ratingRepository.getRatingById(ratingId);
        List<String> imageSources = customRatingDetailsDTOS.stream().map(CustomRatingDetailsDTO::getImageSource).filter(Objects::nonNull).collect(Collectors.toList());
        List<String> commonComments = customRatingDetailsDTOS.stream().map(CustomRatingDetailsDTO::getCommonComment).filter(Objects::nonNull).collect(Collectors.toList());
        RatingResponse ratingResponse = new RatingResponse();
        ratingResponse.setId(customRatingDetailsDTOS.get(0).getId());
        ratingResponse.setStar(customRatingDetailsDTOS.get(0).getStar());
        ratingResponse.setComment(customRatingDetailsDTOS.get(0).getComment());
        ratingResponse.setDateTime(customRatingDetailsDTOS.get(0).getDateTime());
        ratingResponse.setImageSources(imageSources);
        ratingResponse.setCommonComments(commonComments);
        ratingResponse.setStatus(customRatingDetailsDTOS.get(0).getStatus());

        return ratingResponse;
    }
}
