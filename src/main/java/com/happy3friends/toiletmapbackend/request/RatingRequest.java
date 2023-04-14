package com.happy3friends.toiletmapbackend.request;

import com.happy3friends.toiletmapbackend.dto.RatingImageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RatingRequest {
    private int toiletId;
    private int star;
    private String comment;
    private int accountId;
    private List<RatingImageDTO> ratingImagesById;
}
