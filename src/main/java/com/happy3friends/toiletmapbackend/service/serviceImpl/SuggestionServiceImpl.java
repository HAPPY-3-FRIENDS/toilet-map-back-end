package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.SuggestionDTO;
import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import com.happy3friends.toiletmapbackend.entity.ToiletEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.SuggestionMapper;
import com.happy3friends.toiletmapbackend.repository.SuggestionRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
import com.happy3friends.toiletmapbackend.response.SuggestionAdminResponse;
import com.happy3friends.toiletmapbackend.service.SuggestionService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestionServiceImpl.class);

    @Autowired
    private SuggestionRepository suggestionRepository;

    @Autowired
    private SuggestionMapper suggestionMapper;

    @Autowired
    private ToiletServiceImpl toiletService;

    @Autowired
    private ToiletRepository toiletRepository;

    @Override
    public void save(SuggestionEntity entity) {
        suggestionRepository.save(entity);
    }

    @Override
    public void updateAcceptedSuggestion(List<Integer> suggestionIds, Boolean isAccepted) {
        List<SuggestionEntity> suggestionEntity = suggestionRepository.findAllById(suggestionIds);
        if (suggestionEntity.size() != suggestionIds.size())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_SUGGESTION, ToiletMapErrorCodeEnum.NOT_FOUND_SUGGESTION.getMessage());

        LOGGER.info("-- Update Suggestion - Start update isAccepted field for suggestion! --");
        suggestionEntity.forEach(entity -> entity.setIsAccepted(isAccepted));
        suggestionRepository.saveAll(suggestionEntity);
        LOGGER.info("-- Update Suggestion - Finish update isAccepted field for suggestion! --");
    }

    @Override
    public List<SuggestionAdminResponse> getListOfSuggestions() throws ParseException {
        Date now = DateTimeUtil.getDateNow();
        int year = now.getYear() + 1900;
        int quarter = (now.getMonth() / 3) + 1;

        String endDateStr;
        Date endDate;
        switch (quarter) {
            case 1:
                endDateStr = "01-01-" + year;
                break;
            case 2:
                endDateStr = "01-04-" + year;
                break;
            case 3:
                endDateStr = "01-07-" + year;
                break;
            default:
                endDateStr = "01-10-" + year;
                break;
        }
        endDate = new SimpleDateFormat("dd-MM-yyyy").parse(endDateStr);
        Date startDate = DateUtils.addMonths(endDate, -6);
        Date endDateOfQuarter = DateUtils.addDays(endDate, -1);
        List<SuggestionEntity> listSuggestionsIn2LastQuarter = suggestionRepository.getAllSuggestionsIn2LastQuarter(startDate, endDateOfQuarter);
        List<SuggestionDTO> suggestionDTOS = listSuggestionsIn2LastQuarter.stream()
                .map(entity -> suggestionMapper.convertSuggestionEntityToSuggestionDTO(entity))
                .collect(Collectors.toList());
        Map<Integer, List<SuggestionDTO>> mapToiletIdListSuggestionDTO = toiletService.getMapToiletIdListSuggestionDTO(suggestionDTOS);

        List<SuggestionAdminResponse> result = new ArrayList<>();
        mapToiletIdListSuggestionDTO.forEach((key, value) -> {
            SuggestionAdminResponse suggestionAdminResponse = new SuggestionAdminResponse();
            Optional<ToiletEntity> toiletEntity = toiletRepository.findById(key);
            if (!toiletEntity.isPresent())
                throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

            suggestionAdminResponse.setToiletId(toiletEntity.get().getId());
            suggestionAdminResponse.setName(toiletEntity.get().getName());
            suggestionAdminResponse.setAddress(toiletEntity.get().getAddress());
            suggestionAdminResponse.setWard(toiletEntity.get().getWard());
            suggestionAdminResponse.setDistrict(toiletEntity.get().getDistrict());
            suggestionAdminResponse.setProvince(toiletEntity.get().getProvince());
            suggestionAdminResponse.setSuggestions(value);

            result.add(suggestionAdminResponse);
        });

        return result.stream()
                .peek((r) -> {
                    String message = Integer.toString(r.getSuggestions().get(1).getStreak());
                    r.setSuggestionMessage(message + " quý liên tục");
                })
                .collect(Collectors.toList());
    }

    @Override
    public SuggestionEntity getPreviousQuarterSuggestion(int toiletId, Date endDate) {
        return suggestionRepository.getPreviousQuarterSuggestion(toiletId, endDate);
    }
}
