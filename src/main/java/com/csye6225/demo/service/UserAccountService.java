package com.csye6225.demo.service;


import com.csye6225.demo.model.UserAccount;

public interface UserAccountService
{
    public String validateUser(String auth);
    public UserAccount findUserByEmail(String email);
}
