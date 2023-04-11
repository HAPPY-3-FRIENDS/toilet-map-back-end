package com.happy3friends.toiletmapbackend.base.models;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ParameterObject
public class BasePaginationRequest {
    @Parameter(name = "pageIndex", description = "Page index (start at 1)", in = ParameterIn.QUERY, example = "1")
    private Integer pageIndex;
    @Parameter(name = "pageSize", description = "Page size", in = ParameterIn.QUERY, example = "10")
    private Integer pageSize;
    private List<String> sort;
}
