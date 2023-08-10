package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.ComboRequest;
import com.happy3friends.toiletmapbackend.response.ComboResponse;

import java.util.List;

public interface ComboService {
    List<ComboResponse> getAllCombo();

    ComboResponse getComboIdByComboId(int comboId);

    ComboResponse updateComboByComboId(int comboId, ComboRequest comboRequest);

    ComboResponse createCombo(ComboRequest comboRequest);

    void deleteComboByComboId(int comboId);
}
