package com.happy3friends.toiletmapbackend.response;

import com.happy3friends.toiletmapbackend.dto.CheckInFullAToiletTotal;
import com.happy3friends.toiletmapbackend.dto.CheckInScriptTotal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckInFullAToiletResponse {
    private List<String> listUserCheckIn;
    private CheckInFullAToiletTotal total;
}
