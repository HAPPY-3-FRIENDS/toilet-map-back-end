package com.happy3friends.toiletmapbackend.response;

import com.happy3friends.toiletmapbackend.dto.SuggestionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SuggestionAdminResponse {
    private int toiletId;
    private String name;
    private String address;
    private String ward;
    private String district;
    private String province;
    private String suggestionMessage;
    private List<SuggestionDTO> suggestions;
}