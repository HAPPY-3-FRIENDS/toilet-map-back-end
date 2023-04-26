package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.request.CommonCommentRequest;
import com.happy3friends.toiletmapbackend.response.CommonCommentResponse;

import java.util.List;
import java.util.Map;

public interface CommonCommentService {
    List<CommonCommentResponse> getAllCommonComment();

    CommonCommentResponse createCommonComment(CommonCommentRequest request);

    CommonCommentResponse updateCommonComment(Integer id, Map<String, Object> fields);
}
