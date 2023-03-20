package com.happy3friends.toiletmapbackend.mapper;

import com.happy3friends.toiletmapbackend.entity.OrderEntity;
import com.happy3friends.toiletmapbackend.response.OrderResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderMapper.class);

    @Autowired
    private ModelMapper modelMapper;

    public OrderResponse convertOrderEntityToOrderResponse(OrderEntity orderEntity) {
        return Objects.isNull(orderEntity)
                ? null
                : modelMapper.map(orderEntity, OrderResponse.class);
    }
}
