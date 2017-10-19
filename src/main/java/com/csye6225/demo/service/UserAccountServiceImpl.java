package com.csye6225.demo.service;

import com.csye6225.demo.dao.UserAccountDao;
import com.csye6225.demo.datalayer.UserRepository;
import com.csye6225.demo.model.UserAccount;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service("userService")
public class UserAccountServiceImpl implements UserAccountService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private UserAccountDao userAccountDao;

    @Override
    public String validateUser(String auth)
    {

        boolean valid = userAccountDao.validateUser(auth);
        JsonObject jsonObject = new JsonObject();
        if (valid) {
            jsonObject.addProperty("message", "you are logged in. current time is " + new Date().toString());
        } else {
            jsonObject.addProperty("message", "you are not authorized!!!");
        }

        return jsonObject.toString();
    }

    @Override
    public UserAccount findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        UserAccount userRecord = userRepository.findByEmail(username);

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("User");

        UserDetails springUserRecord = new User(userRecord.getEmail(), userRecord.getPassword(), Arrays.asList(grantedAuthority));

        return springUserRecord;
    }
}
