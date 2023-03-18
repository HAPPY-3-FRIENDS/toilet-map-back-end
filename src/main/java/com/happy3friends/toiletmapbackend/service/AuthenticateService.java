package com.happy3friends.toiletmapbackend.service;

import com.happy3friends.toiletmapbackend.dto.TokenDTO;
import com.happy3friends.toiletmapbackend.request.AuthenticateRequest;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticateService {
    TokenDTO authenticate(HttpServletRequest request, AuthenticateRequest authenticateRequest);
}
