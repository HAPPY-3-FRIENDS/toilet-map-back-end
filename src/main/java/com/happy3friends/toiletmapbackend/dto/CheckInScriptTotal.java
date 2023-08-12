package com.happy3friends.toiletmapbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckInScriptTotal {
    private int toiletId;
    private String toiletName;
    private int pee;
    private int poop;
    private int bath;
}
