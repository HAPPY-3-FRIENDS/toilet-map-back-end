package com.happy3friends.toiletmapbackend.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterRatingRequest {
    private int toiletId;
    private List<String> listCommonComment;
    private List<Integer> listStars;
    private List<String> listStatus;
}
