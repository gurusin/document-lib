package com.suds.carbonmeasure.service;

import com.suds.carbonmeasure.domain.CarbonUser;
import com.suds.carbonmeasure.domain.repo.CarbonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private CarbonUserRepository userRepository;

    public Object registerUser(CarbonUser user){
         CarbonUser db = userRepository.findByUsername(user.getUsername());
         if(db != null){
             throw  new RuntimeException("Username alreay exists");
         }
         return userRepository.save(user);
    }
}
