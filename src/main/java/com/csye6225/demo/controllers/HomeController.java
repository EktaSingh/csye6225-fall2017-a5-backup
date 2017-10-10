package com.csye6225.demo.controllers;

import com.csye6225.demo.bean.User;
import com.csye6225.demo.repository.UserRepository;
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
public class HomeController {

    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public  HomeController(UserRepository userRepository){
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

        JsonObject jsonObject = new JsonObject();
        boolean validUser = isUserAuthenticated(auth);

        if (validUser) {
            jsonObject.addProperty("message", "you are logged in. current time is " + new Date().toString());
        } else {
            jsonObject.addProperty("message", "you are not authorized!!!");
        }

        return jsonObject.toString();
    }

    private boolean isUserAuthenticated(String authString) {

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

        if(userRepository.findAll() == null){

            User user = new User();
            String bcyrptPassword = BCrypt.hashpw("a", BCrypt.gensalt());
            user.setPassword(bcyrptPassword);
            user.setEmail("a@a.com");
            user.setUserId(1);
            userRepository.save(user);

            if (user.getEmail().equalsIgnoreCase(email) && BCrypt.checkpw(password, user.getPassword())) {
                validUser = true;
            }

        }else{

            Iterable<User> users = userRepository.findAll();
            Iterator iterator = users.iterator();

            while (iterator.hasNext()) {
                User user = (User) iterator.next();

                if (user.getEmail().equalsIgnoreCase(email) && BCrypt.checkpw(password, user.getPassword())) {
                    validUser = true;
                    break;
                }
            }
        }


        return validUser;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String registerUser(@RequestBody User user) {

        String bcyrptPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(bcyrptPassword);
        userRepository.save(user);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "authorized for /testPost");
        return jsonObject.toString();

    }

}
