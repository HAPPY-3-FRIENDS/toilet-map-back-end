package com.happy3friends.toiletmapbackend.sercurity;

import com.happy3friends.toiletmapbackend.dto.CustomAccountInfoDTO;
import com.happy3friends.toiletmapbackend.exception.NotFoundException;
import com.happy3friends.toiletmapbackend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Khi người dùng đăng nhập thì Spring Security sẽ cần lấy các thông tin UserDetails hiện có để kiểm tra
 * Class CustomUserDetailsService sẽ làm nhiệm vụ này
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByUsername(username);

        if (customAccountInfoDTO == null) throw new NotFoundException("Account", "username", username);

        return CustomUserDetails.create(customAccountInfoDTO);
    }

    @Transactional
    public UserDetails loadUserById(int id) {
        CustomAccountInfoDTO customAccountInfoDTO = accountRepository.getCustomAccountInfoByAccountId(id);

        if (customAccountInfoDTO == null) throw new NotFoundException("Account", "id", id);

        return CustomUserDetails.create(customAccountInfoDTO);
    }
}
