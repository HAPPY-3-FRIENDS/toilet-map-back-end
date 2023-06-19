package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.constant.FacilityNameConstant;
import com.happy3friends.toiletmapbackend.constant.StatusConstant;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDTO;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDetailsInfoDTO;
import com.happy3friends.toiletmapbackend.dto.SuggestionDTO;
import com.happy3friends.toiletmapbackend.dto.ToiletFacilityDTO;
import com.happy3friends.toiletmapbackend.entity.*;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.enums.ServiceEnum;
import com.happy3friends.toiletmapbackend.enums.StatusEnum;
import com.happy3friends.toiletmapbackend.enums.ToiletMapErrorCodeEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.SuggestionMapper;
import com.happy3friends.toiletmapbackend.mapper.ToiletMapper;
import com.happy3friends.toiletmapbackend.repository.*;
import com.happy3friends.toiletmapbackend.request.ToiletCreateRequest;
import com.happy3friends.toiletmapbackend.response.*;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.FilterKeysUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ToiletServiceImpl implements ToiletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToiletServiceImpl.class);

    @Value("${goong.api_key}")
    private String GOONG_API_KEY;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ToiletImageRepository toiletImageRepository;

    @Autowired
    private ToiletFacilityRepository toiletFacilityRepository;

    @Autowired
    private SuggestionRepository suggestionRepository;

    @Autowired
    private ToiletMapper toiletMapper;

    @Autowired
    private SuggestionMapper suggestionMapper;

    public LinkedHashMap<Integer, List<CustomToiletDetailsInfoDTO>> getMapIdListCustomToiletDetailsInfoDTO(
            List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS) {

        return customToiletDetailsInfoDTOS.stream()
                .collect(Collectors.groupingBy(
                        CustomToiletDetailsInfoDTO::getId,
                        LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));
    }

    private ToiletDetailsInfoResponse getToiletFromListCustomToiletDetailsInfoDTOS(
            List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS) {

        // Prepare list toilet-facilities DTO
        List<ToiletFacilityDTO> toiletFacilityDTOS = customToiletDetailsInfoDTOS.stream()
                .filter(FilterKeysUtil.distinctByKeys(CustomToiletDetailsInfoDTO::getFacilityName))
                .map(dto -> {
                    ToiletFacilityDTO toiletFacilityDTO = new ToiletFacilityDTO();
                    toiletFacilityDTO.setFacilityId(dto.getFacilityId());
                    toiletFacilityDTO.setFacilityName(dto.getFacilityName());
                    toiletFacilityDTO.setFacilityType(dto.getFacilityType());
                    toiletFacilityDTO.setQuantity(dto.getFacilityQuantity());
                    toiletFacilityDTO.setDescription(dto.getFacilityDescription());
                    return toiletFacilityDTO;
                })
                .sorted((o1, o2) -> {
                    if(Objects.equals(o1.getFacilityType(), o2.getFacilityType()))
                        return o1.getFacilityName().compareTo(o2.getFacilityName());
                    else if(o1.getFacilityType().compareTo(o2.getFacilityType()) > 0)
                        return 1;
                    else return -1;
                })
                .collect(Collectors.toList());

        // Prepare list toilet-image sources
        List<String> toiletImageSources = customToiletDetailsInfoDTOS.stream()
                .filter(FilterKeysUtil.distinctByKeys(CustomToiletDetailsInfoDTO::getToiletImage))
                .map(dto -> dto.getToiletImage())
                .collect(Collectors.toList());

        // Create Toilet Details Information Response
        ToiletDetailsInfoResponse response = customToiletDetailsInfoDTOS.stream()
                .map(dto -> {
                    ToiletDetailsInfoResponse toiletDetailsInfoResponse = new ToiletDetailsInfoResponse();
                    toiletDetailsInfoResponse.setId(dto.getId());
                    toiletDetailsInfoResponse.setToiletName(dto.getToiletName());
                    toiletDetailsInfoResponse.setAddress(dto.getAddress());
                    toiletDetailsInfoResponse.setWard(dto.getWard());
                    toiletDetailsInfoResponse.setDistrict(dto.getDistrict());
                    toiletDetailsInfoResponse.setProvince(dto.getProvince());
                    toiletDetailsInfoResponse.setLatitude(dto.getLatitude());
                    toiletDetailsInfoResponse.setLongitude(dto.getLongitude());
                    toiletDetailsInfoResponse.setNearBy(dto.getNearBy());
                    toiletDetailsInfoResponse.setOpenTime(DateTimeUtil.convertSqlTimeToHHMMPattern(dto.getOpenTime()));
                    toiletDetailsInfoResponse.setCloseTime(DateTimeUtil.convertSqlTimeToHHMMPattern(dto.getCloseTime()));
                    toiletDetailsInfoResponse.setFree(dto.getIsFree());
                    toiletDetailsInfoResponse.setMinPrice(dto.getMinPrice());
                    toiletDetailsInfoResponse.setMaxPrice(dto.getMaxPrice());
                    toiletDetailsInfoResponse.setRatingStar(dto.getRatingStar());
                    toiletDetailsInfoResponse.setToiletFacilities(toiletFacilityDTOS);
                    toiletDetailsInfoResponse.setToiletImageSources(toiletImageSources);
                    toiletDetailsInfoResponse.setUsername(dto.getUsername());
                    toiletDetailsInfoResponse.setStatus(dto.getStatus());
                    return toiletDetailsInfoResponse;
                })
                .findAny().orElse(null);

        return response;
    }

    public List<ToiletDetailsInfoResponse> getAllToilets() {
        List<CustomToiletDTO> toiletEntities = toiletRepository.getAllToiletsIncludeIdLatitudeLongitude();

        return toiletEntities.stream()
                .map(dto -> toiletMapper.convertCustomToiletDTOToToiletDetailsInfoResponse(dto))
                .collect(Collectors.toList());
    }

    private List<Element> getElementsFromGoongDistanceMatrixAPI(double currentLatitude, double currentLongitude, String destinations, String vehicle) {
        WebClient webClient = WebClient.create("https://rsapi.goong.io");
        String url = "/DistanceMatrix?" +
                "origins=" + Double.toString(currentLatitude) + "," + Double.toString(currentLongitude) + "&" +
                "destinations=" + destinations + "&" +
                "vehicle=" + vehicle + "&" +
                "api_key=" + GOONG_API_KEY;
        Flux<DistanceMatrixResponse> fluxDistanceMatrixResponse = webClient.get().uri(url).retrieve().bodyToFlux(DistanceMatrixResponse.class);
        List<DistanceMatrixResponse> listDistanceMatrixResponse = fluxDistanceMatrixResponse.collectList().block();

        return new ArrayList<>(listDistanceMatrixResponse.get(0).getRows().get(0).getElements());
    }

    private List<ToiletDetailsInfoResponse> mapDurationAndDistanceFromGoongAPI(
            Double currentLatitude, Double currentLongitude,
            List<ToiletDetailsInfoResponse> toiletDetailsInfoResponses) {

        // Prepare query params
        List<String> listLatLongDestination = toiletDetailsInfoResponses.stream()
                .map(obj  -> {
                    String latitude = Double.toString(obj.getLatitude());
                    String longitude = Double.toString(obj.getLongitude());

                    return String.join(",", latitude, longitude);
                })
                .collect(Collectors.toList());

        String destinations = String.join("|", listLatLongDestination);

        // Connect to Goong API
        List<Element> elements = getElementsFromGoongDistanceMatrixAPI(currentLatitude, currentLongitude, destinations, "car");

        // Map Distance and Duration to response
        return IntStream.range(0, toiletDetailsInfoResponses.size())
                .mapToObj(index -> {
                    toiletDetailsInfoResponses.get(index).setDuration(elements.get(index).getDuration().getText());
                    toiletDetailsInfoResponses.get(index).setDistance(elements.get(index).getDistance().getText());

                    return toiletDetailsInfoResponses.get(index);
                })
                .collect(Collectors.toList());
    }

    public List<ToiletDetailsInfoResponse> getTop10ToiletsNearByCurrentLocation(Double currentLatitude, Double currentLongitude) {

        // Get top 10 toilets nearby current location
        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS
                = toiletRepository.getTop10ToiletsNearByCurrentLocation(currentLatitude, currentLongitude);

        LinkedHashMap<Integer, List<CustomToiletDetailsInfoDTO>> mapIdListCustomToiletDetailsInfoDTO
                = getMapIdListCustomToiletDetailsInfoDTO(customToiletDetailsInfoDTOS);

        List<ToiletDetailsInfoResponse> toiletDetailsInfoResponses = mapIdListCustomToiletDetailsInfoDTO.entrySet()
                .stream().map(dto -> getToiletFromListCustomToiletDetailsInfoDTOS(dto.getValue()))
                .limit(10)
                .collect(Collectors.toList());

        return mapDurationAndDistanceFromGoongAPI(currentLatitude, currentLongitude, toiletDetailsInfoResponses);
    }

    public List<ToiletDetailsInfoResponse> getAllToiletsByCompanyId(Integer companyId, BasePaginationRequest paginationRequest) {

        // Prepare pagination & sort
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.ASC, DefaultSortPropertyConstant.ID);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        // Validate Company
        Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
        if (!companyEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY, ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY.getMessage());

        // Get all toilets of Company by Company ID
        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS
                = toiletRepository.getAllToiletsByCompanyId(companyId, pageable);
        List<Integer> listToiletIds = customToiletDetailsInfoDTOS.stream()
                .map(dto -> dto.getId())
                .collect(Collectors.toList());
        List<SuggestionEntity> suggestionEntities = suggestionRepository.getListSuggestionByListToiletIds(companyId, listToiletIds);
        List<SuggestionDTO> suggestionDTOS = suggestionEntities.stream()
                .map(entity -> suggestionMapper.convertSuggestionEntityToSuggestionDTO(entity))
                .collect(Collectors.toList());
        Map<Integer, List<SuggestionDTO>> mapToiletIdListSuggestionDTO = getMapToiletIdListSuggestionDTO(suggestionDTOS);

        List<ToiletDetailsInfoResponse> toiletDetailsInfoResponses =
                customToiletDetailsInfoDTOS.stream()
                        .map(dto -> toiletMapper.convertCustomToiletDetailsInfoDTOToToiletDetailsInfoResponse(dto))
                        .collect(Collectors.toList());
        return toiletDetailsInfoResponses.stream()
                .map(res -> {
                    int toiletId = res.getId();
                    List<SuggestionDTO> suggestionDTOs = mapToiletIdListSuggestionDTO.get(toiletId);
                    if (suggestionDTOs != null && suggestionDTOs.size() == 2) {
                        res.setSuggestions(suggestionDTOs);
                        res.setSuggestionMessage(suggestionDTOs.size() + " quý liên tục");
                    }
                    return res;
                })
                .collect(Collectors.toList());
    }

    public LinkedHashMap<Integer, List<SuggestionDTO>> getMapToiletIdListSuggestionDTO(
            List<SuggestionDTO> suggestionDTOS) {

        return suggestionDTOS.stream()
                .collect(Collectors.groupingBy(
                        SuggestionDTO::getToiletId,
                        LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public List<ToiletDetailsInfoResponse> getAllToilets(
            Integer companyId,
            Double currentLatitude,
            Double currentLongitude,
            BasePaginationRequest paginationRequest) {

        List<ToiletDetailsInfoResponse> responses;

        if (companyId != null) {
            responses = getAllToiletsByCompanyId(companyId, paginationRequest);
        } else if (currentLatitude != null && currentLongitude != null) {
            responses = getTop10ToiletsNearByCurrentLocation(currentLatitude, currentLongitude);
        } else {
            responses = getAllToilets();
        }

        return responses;
    }

    @Override
    public ToiletDetailsInfoResponse getToiletByToiletId(int toiletId) {
        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS
                = toiletRepository.getCustomToiletInfoDTOByToiletId(toiletId);

        return getToiletFromListCustomToiletDetailsInfoDTOS(customToiletDetailsInfoDTOS);
    }

    @Override
    public int count(Integer companyId) {

        if (companyId != null) {
            Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
            if (!companyEntity.isPresent())
                throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY, ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY.getMessage());

            return (int) toiletRepository.countByCompanyId(companyId);
        }

        return (int) toiletRepository.count();
    }

    public LinkedHashMap<Integer, List<CustomToiletDetailsInfoDTO>> getMapIdListService(
            List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS) {

        return customToiletDetailsInfoDTOS.stream()
                .collect(Collectors.groupingBy(
                        CustomToiletDetailsInfoDTO::getId,
                        LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));
    }

    @Override
    @Transactional
    public void createToilet(ToiletCreateRequest toiletCreateRequest) throws Exception {

        // Validate company ID
        Optional<CompanyEntity> companyEntity = companyRepository.findById(toiletCreateRequest.getCompanyId());
        if (!companyEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY, ToiletMapErrorCodeEnum.NOT_FOUND_COMPANY.getMessage());

        // Validate Username
        if (accountRepository.findByUsername(toiletCreateRequest.getUsername()) != null)
            throw new BadRequestException(ToiletMapErrorCodeEnum.EXISTED_USERNAME, ToiletMapErrorCodeEnum.EXISTED_USERNAME.getMessage());

        // Validate Status
        if (StatusEnum.getByValue(toiletCreateRequest.getStatus()) == null)
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_STATUS, ToiletMapErrorCodeEnum.INVALID_STATUS.getMessage());

        // Find role Toilet
        List<RoleEntity> roleEntities = roleRepository.findAll();
        int roleId = roleEntities.stream()
                .filter(entity -> entity.getName().equals(RoleEnum.TOILET.getRoleName()))
                .findFirst().orElseThrow(() -> new Exception("Can not find Toilet Role in database!")).getId();

        // Validate Facility
        List<ToiletFacilityDTO> toiletFacilityDTOS = toiletCreateRequest.getToiletFacilities();
        List<FacilityEntity> facilityEntities = facilityRepository.findAll();
        List<Integer> listFacilityIds = facilityEntities.stream().map(FacilityEntity::getId).collect(Collectors.toList());
        if (!new HashSet<>(listFacilityIds).containsAll(toiletFacilityDTOS.stream().map(ToiletFacilityDTO::getFacilityId).collect(Collectors.toList())))
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_FACILITY, ToiletMapErrorCodeEnum.INVALID_FACILITY.getMessage());
        if (toiletFacilityDTOS.stream().anyMatch(dto -> dto.getQuantity() < 1))
            throw new BadRequestException(ToiletMapErrorCodeEnum.INVALID_FACILITY_QUANTITY, ToiletMapErrorCodeEnum.INVALID_FACILITY_QUANTITY.getMessage());

        LOGGER.info("-- Create Toilet - Start save Account Entity! --");
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUsername(toiletCreateRequest.getUsername());
        accountEntity.setPassword(toiletCreateRequest.getPassword());
        accountEntity.setRoleId(roleId);
        accountEntity.setStatus(toiletCreateRequest.getStatus());
        accountEntity.setCompanyId(toiletCreateRequest.getCompanyId());
        AccountEntity savedAccountEntity = accountRepository.save(accountEntity);
        LOGGER.info("-- Create Toilet - Finish save Account Entity! --");

        LOGGER.info("-- Create Toilet - Start save Toilet Entity & its information! --");
        // Toilet Entity
        ToiletEntity toiletEntity = toiletMapper.convertToiletCreateRequestToiletEntity(toiletCreateRequest);
        int toiletId = savedAccountEntity.getId();
        toiletEntity.setAccountById(accountEntity);

        // Toilet Image Entity
        List<ToiletImageEntity> toiletImageEntities = toiletCreateRequest.getToiletImages().stream()
                .map(o -> {
                    ToiletImageEntity toiletImageEntity = new ToiletImageEntity();
                    toiletImageEntity.setToiletId(toiletId);
                    toiletImageEntity.setImageSource(o);

                    return toiletImageEntity;
                })
                .collect(Collectors.toList());
        toiletEntity.setToiletImagesById(toiletImageEntities);

        // Toilet Facility
        List<ToiletFacilityEntity> toiletFacilityEntities = toiletFacilityDTOS.stream()
                .filter(FilterKeysUtil.distinctByKeys(ToiletFacilityDTO::getFacilityId))
                .map(dto -> {
                    ToiletFacilityEntity toiletFacilityEntity = new ToiletFacilityEntity();
                    toiletFacilityEntity.setToiletId(toiletId);
                    toiletFacilityEntity.setFacilityId(dto.getFacilityId());
                    toiletFacilityEntity.setQuantity(dto.getQuantity());

                    return toiletFacilityEntity;
                }).collect(Collectors.toList());
        toiletEntity.setToiletFacilitiesById(toiletFacilityEntities);

        // Toilet Service
        List<ServiceEntity> serviceEntities = serviceRepository.findAll();
        List<Integer> listFacilityIdRequest = toiletFacilityDTOS.stream().filter(FilterKeysUtil.distinctByKeys(ToiletFacilityDTO::getFacilityId)).map(ToiletFacilityDTO::getFacilityId).collect(Collectors.toList());
        Map<Integer, FacilityEntity> mapIdFacilityEntities = facilityEntities.stream()
                .filter(entity -> listFacilityIdRequest.contains(entity.getId()))
                .collect(Collectors.toMap(FacilityEntity::getId, facilityEntity -> facilityEntity));

        ToiletServiceEntity addingDoubleToiletServiceEntity = new ToiletServiceEntity();
        List<ToiletServiceEntity> toiletServiceEntities = toiletFacilityDTOS.stream()
                .filter(FilterKeysUtil.distinctByKeys(ToiletFacilityDTO::getFacilityId))
                .map(dto -> {
                    ToiletServiceEntity toiletServiceEntity = new ToiletServiceEntity();
                    toiletServiceEntity.setToiletId(toiletId);

                    switch (mapIdFacilityEntities.get(dto.getFacilityId()).getName()) {
                        case FacilityNameConstant.TOILET_ROOM:
                            toiletServiceEntity.setServiceId(
                                    serviceEntities.stream()
                                            .filter(entity -> entity.getName().equals(ServiceEnum.PEE.getServiceName()))
                                            .findAny().get().getId()
                            );

                            addingDoubleToiletServiceEntity.setToiletId(toiletId);
                            addingDoubleToiletServiceEntity.setServiceId(
                                    serviceEntities.stream()
                                            .filter(entity -> entity.getName().equals(ServiceEnum.POOP.getServiceName()))
                                            .findAny().get().getId()
                            );
                            break;
                        case FacilityNameConstant.BATHROOM:
                            toiletServiceEntity.setServiceId(
                                    serviceEntities.stream()
                                            .filter(entity -> entity.getName().equals(ServiceEnum.SHOWER.getServiceName()))
                                            .findAny().get().getId()
                            );
                            break;
                    }
                    return toiletServiceEntity;
                }).collect(Collectors.toList());
        toiletServiceEntities.add(addingDoubleToiletServiceEntity);
        List<ToiletServiceEntity> toiletServiceEntitiesFinal = toiletServiceEntities.stream()
                .filter(e -> e.getServiceId() != 0)
                .collect(Collectors.toList());
        toiletEntity.setToiletServicesById(toiletServiceEntitiesFinal);

        toiletRepository.save(toiletEntity);
        LOGGER.info("-- Create Toilet - Finish save Toilet Entity & its information! --");
    }

    @Override
    public ToiletDetailsInfoResponse getNearestToilet(Double lat, Double lng, String vehicle) {
        List<ToiletDetailsInfoResponse> list10ToiletNearByLatLng = getTop10ToiletsNearByCurrentLocation(lat, lng);
        if (list10ToiletNearByLatLng == null || list10ToiletNearByLatLng.isEmpty()) {
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET_NEARBY, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET_NEARBY.getMessage());
        }

        List<String> listDestinations = new ArrayList<>();
        for (ToiletDetailsInfoResponse toilet : list10ToiletNearByLatLng) {
            listDestinations.add(toilet.getLatitude() + "," + toilet.getLongitude());
        }
        String destinations = String.join("|", listDestinations);

        WebClient webClient = WebClient.create("https://rsapi.goong.io");
        String url = "/DistanceMatrix?" +
                "origins=" + lat + "," + lng + "&" +
                "destinations=" + destinations + "&" +
                "vehicle=" + vehicle + "&" +
                "api_key=OnwCCr1HHOeOCqofMVaXBaf9t82lC25Vqsm6iJJa";
        Flux<DistanceMatrixResponse> fluxDistanceMatrixResponse = webClient.get().uri(url).retrieve().bodyToFlux(DistanceMatrixResponse.class);
        List<DistanceMatrixResponse> listDistanceMatrixResponse = fluxDistanceMatrixResponse.collectList().block();

        Element element = listDistanceMatrixResponse.get(0).getRows().get(0).getElements().stream()
                .min((x, y) -> x.getDuration().getValue() - y.getDuration().getValue())
                .get();

        int index = listDistanceMatrixResponse.get(0).getRows().get(0).getElements().indexOf(element);

        return list10ToiletNearByLatLng.get(index);
    }

    @Override
    public UpdateToiletInfoResponse updateToiletInfo(Integer id, Map<String, Object> fields) {
        Optional<ToiletEntity> toiletEntity = toiletRepository.findById(id);
        if (!toiletEntity.isPresent())
            throw new NotFoundException(ToiletMapErrorCodeEnum.NOT_FOUND_TOILET, ToiletMapErrorCodeEnum.NOT_FOUND_TOILET.getMessage());

        LOGGER.info("-- Update Toilet info - Start save Toilet Entity and its information! --");
        fields.forEach((key, value) -> {
            if (key.equals("toiletImagesById")) {
                //Delete all images
                toiletImageRepository.deleteByToiletId(id);

                //Update new images
                Collection<ToiletImageEntity> toiletImageEntities = new ArrayList<>();

                String value2String = value.toString().substring(1, value.toString().length() - 1);
                List<String> listImages = Arrays.asList(value2String.split(", "));

                listImages.forEach((s) -> {
                    ToiletImageEntity toiletImageEntity = new ToiletImageEntity();
                    toiletImageEntity.setToiletId(id);
                    toiletImageEntity.setImageSource(s);
                    toiletImageEntities.add(toiletImageEntity);
                });

                toiletEntity.get().setToiletImagesById(toiletImageEntities);
            } else if(key.equals("toiletFacilitiesById")) {
                //Delete all toilet facility
                toiletFacilityRepository.deleteByToiletId(id);

                //Update new toilet facility
                Collection<ToiletFacilityEntity> toiletFacilityEntities = new ArrayList<>();

                try {
                    String json = new ObjectMapper().writeValueAsString(value);
                    JSONArray array = new JSONArray(json);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        ToiletFacilityEntity toiletFacilityEntity = new ToiletFacilityEntity();
                        toiletFacilityEntity.setToiletId(id);
                        toiletFacilityEntity.setFacilityId(object.getInt("facilityId"));
                        toiletFacilityEntity.setQuantity(object.getInt("quantity"));
                        toiletFacilityEntities.add(toiletFacilityEntity);
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                toiletEntity.get().setToiletFacilitiesById(toiletFacilityEntities);
            } else if (key.equals("openTime")) {
                String value2String = value.toString();
                toiletEntity.get().setOpenTime(new Time(DateTimeUtil.convertStringToDate(value2String, "HH:mm").getTime()));
            } else if (key.equals("closeTime")) {
                String value2String = value.toString();
                toiletEntity.get().setCloseTime(new Time(DateTimeUtil.convertStringToDate(value2String, "HH:mm").getTime()));
            } else if (key.equals("status")) {
                switch (toiletEntity.get().getStatus()) {
                    case StatusConstant.ACTIVE:
                        toiletEntity.get().setStatus(StatusEnum.IN_ACTIVE.getStatus());
                        break;
                    case StatusConstant.IN_ACTIVE:
                        toiletEntity.get().setStatus(StatusEnum.ACTIVE.getStatus());
                        break;
                }
            } else {

                Field field = ReflectionUtils.findField(ToiletEntity.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, toiletEntity.get(), value);
            }
        });

        ToiletEntity entity = toiletRepository.save(toiletEntity.get());
        LOGGER.info("-- Update Toilet info - Finish save Toilet Entity and its information! --");
        return toiletMapper.convertToiletEntityToUpdateToiletInfoResponse(entity);
    }

    @Override
    public List<Integer> getAllToiletId() {
        return toiletRepository.getAllToiletId();
    }

    @Override
    public List<ToiletFacilityResponse> getListToiletFacilityByToiletId(int toiletId) {
        List<ToiletFacilityEntity> entities = toiletFacilityRepository.getListToiletFacilityByToiletId(toiletId);
        return entities.stream()
                .map(e -> toiletMapper.convertToiletFacilityEntityToToiletFacilityResponse(e))
                .collect(Collectors.toList());
    }
}