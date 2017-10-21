package com.csye6225.demo.controllers;

import com.csye6225.demo.model.UserAccount;
import com.csye6225.demo.datalayer.UserRepository;
import com.csye6225.demo.service.UserAccountService;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

/*
<Ekta Singh>, <001258567>, <singh.ek@husky.neu.edu>
<Karan Bhavsar>, <001225621>, <bhavsar.ka@husky.neu.edu>
<Bhavesh Sachdev>, <001280940>, <sachdev.b@husky.neu.edu>
<Nikita Dulani>, <001280944>, <dulani.n@husky.neu.edu>
*/

@Controller
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String welcome() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "Welcome");

        return jsonObject.toString();
    }

    @RequestMapping(value = "/validUser", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String validateUser(HttpServletRequest httpRequest) {

        String auth = httpRequest.getHeader("Authorization");

        return userAccountService.validateUser(auth);
    }

    public long getUserId(String authString) {

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

        JsonObject jsonObject = new JsonObject();
        boolean validUser = false;
        long result=0;

        if(userRepository.findAll() == null){

            UserAccount userAccount = new UserAccount();
            String bcyrptPassword = BCrypt.hashpw("a", BCrypt.gensalt());
            userAccount.setPassword(bcyrptPassword);
            userAccount.setEmail("a@a.com");
            userAccount.setUserId(1);
            userRepository.save(userAccount);

            if (userAccount.getEmail().equalsIgnoreCase(email) && BCrypt.checkpw(password, userAccount.getPassword())) {
                //result = userAccount.getUserId();
                validUser = true;
            }

        }else{

            Iterable<UserAccount> users = userRepository.findAll();
            Iterator iterator = users.iterator();

            while (iterator.hasNext()) {
                UserAccount userAccount = (UserAccount) iterator.next();

                if (userAccount.getEmail().equalsIgnoreCase(email) && BCrypt.checkpw(password, userAccount.getPassword())) {
                    result = userAccount.getUserId();
                    validUser = true;
                    break;
                }
            }
        }


        return result;
    }

    public UserAccount getUser(String email)
    {
        Iterable<UserAccount> users = userRepository.findAll();
        Iterator iterator = users.iterator();

        while (iterator.hasNext()) {
            UserAccount userAccount = (UserAccount) iterator.next();

            if (userAccount.getEmail().equalsIgnoreCase(email)) {
                return userAccount;
            }
        }

        return null;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String registerUser(@RequestBody UserAccount userAccount) {

        String bcyrptPassword = BCrypt.hashpw(userAccount.getPassword(), BCrypt.gensalt());
        userAccount.setPassword(bcyrptPassword);
        userRepository.save(userAccount);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "authorized for /testPost");
        return jsonObject.toString();

    }

}
