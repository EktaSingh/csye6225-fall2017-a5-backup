package com.csye6225.demo.service;


import com.csye6225.demo.dao.UserAccountDao;
import com.csye6225.demo.datalayer.UserRepository;
import com.csye6225.demo.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAccountService
{
    public String validateUser(String auth);
    public UserAccount findUserByEmail(String email);
}
