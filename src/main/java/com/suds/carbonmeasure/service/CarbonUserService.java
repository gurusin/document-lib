package com.suds.carbonmeasure.service;

import com.suds.carbonmeasure.config.CustomUserDetails;
import com.suds.carbonmeasure.domain.CarbonUser;
import com.suds.carbonmeasure.domain.repo.CarbonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CarbonUserService implements UserDetailsService {

    @Autowired
    private CarbonUserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CarbonUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // Return an instance of your custom UserDetails implementation
        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}

