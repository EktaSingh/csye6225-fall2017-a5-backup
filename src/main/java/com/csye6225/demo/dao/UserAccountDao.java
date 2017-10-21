package com.csye6225.demo.dao;

import com.csye6225.demo.datalayer.UserRepository;
import com.csye6225.demo.model.UserAccount;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.Iterator;

@Repository
public class UserAccountDao {

    @Autowired
    private UserRepository userRepository;

    public boolean validateUser(String auth)
    {
        return getUserId(auth);
    }


    private boolean getUserId(String authString) {

        String decodedAuth = "";
        // Header is in the format "Basic 5tyc0uiDat4"
        // We need to extract data before decoding it back to original string
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
        // Decode the data back to original string
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        decodedAuth = new String(bytes);
        System.out.println(decodedAuth);

        String email = decodedAuth.split(":")[0];
        String password = decodedAuth.split(":")[1];

        boolean validUser = false;

            Iterable<UserAccount> users = userRepository.findAll();
            Iterator iterator = users.iterator();

            while (iterator.hasNext()) {
                UserAccount userAccount = (UserAccount) iterator.next();

                if (userAccount.getEmail().equalsIgnoreCase(email) && BCrypt.checkpw(password, userAccount.getPassword())) {
                    validUser = true;
                    break;
                }
            }


        return validUser;
    }

}
