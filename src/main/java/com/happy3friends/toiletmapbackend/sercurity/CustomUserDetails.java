package com.happy3friends.toiletmapbackend.sercurity;

import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Mặc định Spring Security sử dụng một đối tượng UserDetails để chứa toàn bộ thông tin về người dùng
 * CustomUserDetails chuyển thông tin từ AccountEntity thành UserDetails
 */
public class CustomUserDetails implements UserDetails {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private String defaultPayment;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public CustomUserDetails(int id, String username, String password, String fullName, String role, String defaultPayment, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.defaultPayment = defaultPayment;
        this.authorities = authorities;
    }

    public static CustomUserDetails create(CustomAccountInfoDTO customAccountInfoDTO) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_" + customAccountInfoDTO.getRole()));

        return new CustomUserDetails(
                customAccountInfoDTO.getAccountId(),
                customAccountInfoDTO.getUsername(),
                customAccountInfoDTO.getPassword(),
                customAccountInfoDTO.getFullName(),
                customAccountInfoDTO.getRole(),
                customAccountInfoDTO.getDefaultPayment(),
                authorities
        );
    }

    public static CustomUserDetails create(CustomAccountInfoDTO customAccountInfoDTO, Map<String, Object> attributes) {
        CustomUserDetails customUserDetails = CustomUserDetails.create(customAccountInfoDTO);
        customUserDetails.setAttributes(attributes);
        return customUserDetails;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public String getDefaultPayment() {
        return defaultPayment;
    }
}
