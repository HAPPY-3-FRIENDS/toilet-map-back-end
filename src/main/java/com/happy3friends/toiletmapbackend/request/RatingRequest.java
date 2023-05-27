package com.happy3friends.toiletmapbackend.request;

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
    private int checkInId;
    private List<String> imageSources;
    private List<Integer> commonComments;
}
