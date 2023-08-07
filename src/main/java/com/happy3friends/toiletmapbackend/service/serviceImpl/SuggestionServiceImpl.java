package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.CustomSuggestionDTO;
import com.happy3friends.toiletmapbackend.dto.SuggestionDTO;
import com.happy3friends.toiletmapbackend.entity.SuggestionEntity;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.SuggestionMapper;
import com.happy3friends.toiletmapbackend.repository.SuggestionRepository;
import com.happy3friends.toiletmapbackend.repository.ToiletRepository;
import com.happy3friends.toiletmapbackend.response.SuggestionAdminResponse;
import com.happy3friends.toiletmapbackend.service.SuggestionService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    static final Logger LOGGER = LoggerFactory.getLogger(SuggestionServiceImpl.class);

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

    private LinkedHashMap<Integer, List<CustomSuggestionDTO>> getMapToiletIdListCustomSuggestionDTO(
            List<CustomSuggestionDTO> customSuggestionDTOS) {

        return customSuggestionDTOS.stream()
                .collect(Collectors.groupingBy(
                        CustomSuggestionDTO::getToiletId,
                        LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public List<SuggestionAdminResponse> getListOfSuggestions() {
        List<CustomSuggestionDTO> listSuggestionsIn2LastQuarter = suggestionRepository.getAllSuggestionsIn2LastQuarter();
        Map<Integer, List<CustomSuggestionDTO>> mapToiletIdListCustomSuggestionDTO
                = getMapToiletIdListCustomSuggestionDTO(listSuggestionsIn2LastQuarter);

        List<SuggestionAdminResponse> result = new ArrayList<>();
        mapToiletIdListCustomSuggestionDTO.forEach((key, value) -> {
            SuggestionAdminResponse suggestionAdminResponse = new SuggestionAdminResponse();

            List<SuggestionDTO> suggestionDTOs = value.stream()
                    .map(item -> suggestionMapper.convertCustomSuggestionDTOToSuggestionDTO(item))
                    .collect(Collectors.toList());

            suggestionAdminResponse.setToiletId(key);
            suggestionAdminResponse.setName(value.get(0).getName());
            suggestionAdminResponse.setAddress(value.get(0).getAddress());
            suggestionAdminResponse.setWard(value.get(0).getWard());
            suggestionAdminResponse.setDistrict(value.get(0).getDistrict());
            suggestionAdminResponse.setProvince(value.get(0).getProvince());

            if (suggestionDTOs != null) {
                if (suggestionDTOs.get(0).getEndDate().compareTo(DateTimeUtil.getEndDateOfPreviousQuarter()) == 0 && suggestionDTOs.size() == 2) {
                    if (suggestionDTOs.get(0).getIsLow()) {
                        if (suggestionDTOs.get(1).getIsLow()) {
                            suggestionAdminResponse.setBelowThreshold(true);
                            suggestionAdminResponse.setSuggestionMessage("Dưới ngưỡng " + suggestionDTOs.get(0).getStreak() + " quý liên tục");
                            suggestionAdminResponse.setSuggestions(suggestionDTOs);
                        }
                    } else if (!suggestionDTOs.get(1).getIsLow()) {
                        suggestionAdminResponse.setBelowThreshold(false);
                        suggestionAdminResponse.setSuggestionMessage("Vượt ngưỡng " + suggestionDTOs.get(0).getStreak() + " quý liên tục");
                        suggestionAdminResponse.setSuggestions(suggestionDTOs);
                    }
                }
            }

            result.add(suggestionAdminResponse);
        });

        return result;

        /*Date now = DateTimeUtil.getDateNow();
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
                .collect(Collectors.toList());*/
    }

    @Override
    public SuggestionEntity getPreviousQuarterSuggestion(int toiletId, Date endDate) {
        return suggestionRepository.getPreviousQuarterSuggestion(toiletId, endDate);
    }
}
