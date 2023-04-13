package com.happy3friends.toiletmapbackend.service.serviceImpl;

import com.happy3friends.toiletmapbackend.dto.TokenDTO;
import com.happy3friends.toiletmapbackend.request.AuthenticateRequest;
import com.happy3friends.toiletmapbackend.sercurity.CustomUserDetailsService;
import com.happy3friends.toiletmapbackend.sercurity.TokenProvider;
import com.happy3friends.toiletmapbackend.service.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public TokenDTO authenticate(HttpServletRequest request, AuthenticateRequest authenticateRequest) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticateRequest.getUsername());

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Return jwt token to User
        String jwt = tokenProvider.generateToken(authentication);
        return new TokenDTO(jwt);
    }
}
