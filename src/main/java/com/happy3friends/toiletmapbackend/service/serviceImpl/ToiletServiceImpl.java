package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.constant.DefaultSortPropertyConstant;
import com.happy3friends.toiletmapbackend.constant.FacilityNameConstant;
import com.happy3friends.toiletmapbackend.constant.ToiletConstant;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDTO;
import com.happy3friends.toiletmapbackend.dto.CustomToiletDetailsInfoDTO;
import com.happy3friends.toiletmapbackend.dto.ToiletFacilityDTO;
import com.happy3friends.toiletmapbackend.entity.*;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.enums.ServiceEnum;
import com.happy3friends.toiletmapbackend.enums.StatusEnum;
import com.happy3friends.toiletmapbackend.exception.BadRequestException;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.mapper.ToiletImageMapper;
import com.happy3friends.toiletmapbackend.mapper.ToiletMapper;
import com.happy3friends.toiletmapbackend.repository.*;
import com.happy3friends.toiletmapbackend.request.ToiletCreateRequest;
import com.happy3friends.toiletmapbackend.response.ToiletDetailsInfoResponse;
import com.happy3friends.toiletmapbackend.service.ToiletService;
import com.happy3friends.toiletmapbackend.utils.FilterKeysUtil;
import com.happy3friends.toiletmapbackend.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ToiletServiceImpl implements ToiletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToiletServiceImpl.class);

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
    private ToiletMapper toiletMapper;

    @Autowired
    private ToiletImageMapper toiletImageMapper;

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
                    toiletDetailsInfoResponse.setOpenTime(dto.getOpenTime());
                    toiletDetailsInfoResponse.setCloseTime(dto.getCloseTime());
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

    public List<ToiletDetailsInfoResponse> getTop10ToiletsNearByCurrentLocation(Double currentLatitude, Double currentLongitude) {

        // Create deviation location depend on current location
        Double deviationLatitudeMax = currentLatitude + ToiletConstant.LOCATED_DEVIATION;
        Double deviationLongitudeMax = currentLongitude + ToiletConstant.LOCATED_DEVIATION;

        // Distance of radius between current location and deviation location
        Double distanceCurrentAndDeviationMax = Math.sqrt(
                Math.pow(currentLatitude - deviationLatitudeMax, 2)
                        + Math.pow(currentLongitude - deviationLongitudeMax, 2)
        );

        // Get top 10 toilets nearby current location
        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS
                = toiletRepository.getTop10ToiletsNearByCurrentLocation(currentLatitude,
                currentLongitude,
                distanceCurrentAndDeviationMax);

        LinkedHashMap<Integer, List<CustomToiletDetailsInfoDTO>> mapIdListCustomToiletDetailsInfoDTO
                = getMapIdListCustomToiletDetailsInfoDTO(customToiletDetailsInfoDTOS);

        return mapIdListCustomToiletDetailsInfoDTO.entrySet()
                .stream().map(dto -> getToiletFromListCustomToiletDetailsInfoDTOS(dto.getValue()))
                .collect(Collectors.toList());
    }

    public List<ToiletDetailsInfoResponse> getAllToiletsByCompanyId(Integer companyId, BasePaginationRequest paginationRequest) {

        // Prepare pagination & sort
        Sort.Order defaultSortOrder = new Sort.Order(Sort.Direction.ASC, DefaultSortPropertyConstant.ID);
        Pageable pageable = PaginationUtil.getPageable(paginationRequest, defaultSortOrder);

        // Validate Company
        Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
        if (!companyEntity.isPresent())
            throw new NotFoundException("Company", "Id", companyId);

        // Get all toilets of Company by Company ID
        List<CustomToiletDetailsInfoDTO> customToiletDetailsInfoDTOS
                = toiletRepository.getAllToiletsByCompanyId(companyId, pageable);

        return customToiletDetailsInfoDTOS.stream()
                .map(dto -> toiletMapper.convertCustomToiletDetailsInfoDTOToToiletDetailsInfoResponse(dto))
                .collect(Collectors.toList());
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
                throw new NotFoundException("Company", "Id", companyId);

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
            throw new NotFoundException("Company", "Id", toiletCreateRequest.getCompanyId());

        // Validate Username
        if (accountRepository.findByUsername(toiletCreateRequest.getUsername()) != null)
            throw new BadRequestException("Username '" + toiletCreateRequest.getUsername() + "' is not unique! It's already used by another toilet!");

        // Validate Status
        if (StatusEnum.getByValue(toiletCreateRequest.getStatus()) == null)
            throw new BadRequestException("Invalid status!");

        // Find role Toilet
        List<RoleEntity> roleEntities = roleRepository.findAll();
        int roleId = roleEntities.stream()
                .filter(entity -> entity.getName().equals(RoleEnum.TOILET.getRoleName()))
                .findFirst().orElseThrow(() -> new NotFoundException("Can not find Toilet Role in database!")).getId();

        // Validate Facility
        List<ToiletFacilityDTO> toiletFacilityDTOS = toiletCreateRequest.getToiletFacilities();
        List<FacilityEntity> facilityEntities = facilityRepository.findAll();
        List<Integer> listFacilityIds = facilityEntities.stream().map(FacilityEntity::getId).collect(Collectors.toList());
        if (!new HashSet<>(listFacilityIds).containsAll(toiletFacilityDTOS.stream().map(ToiletFacilityDTO::getFacilityId).collect(Collectors.toList())))
            throw new BadRequestException("Invalid facility Id!");
        if (toiletFacilityDTOS.stream().anyMatch(dto -> dto.getQuantity() < 1))
            throw new BadRequestException("Quantity of facility is less than 1");

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
                .map(dto -> {
                    ToiletImageEntity toiletImageEntity = new ToiletImageEntity();
                    toiletImageEntity.setToiletId(toiletId);
                    toiletImageEntity.setImageSource(dto.getImageSource());

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
        toiletEntity.setToiletServicesById(toiletServiceEntities);

        toiletRepository.save(toiletEntity);
        LOGGER.info("-- Create Toilet - Finish save Toilet Entity & its information! --");
    }
}