package com.happy3friends.toiletmapbackend.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ParameterObject
public class FilterRatingRequest {
    @Parameter(name = "listIdCommonComment",
            in = ParameterIn.QUERY,
            array = @ArraySchema(schema = @Schema(implementation = Integer.class)),
            allowReserved = true)
    private List<Integer> listIdCommonComment;
    @Parameter(hidden = true)
    private int toiletId;
    @Parameter(hidden = true)
    private List<String> listCommonComment;
    @Parameter(name = "listStars",
            in = ParameterIn.QUERY,
            array = @ArraySchema(schema = @Schema(implementation = Integer.class), maxItems = 5),
            allowReserved = true)
    private List<Integer> listStars;
    @Parameter(name = "listStatus",
            in = ParameterIn.QUERY,
            array = @ArraySchema(schema = @Schema(implementation = String.class), maxItems = 2),
            allowReserved = true)
    private List<String> listStatus;
}
