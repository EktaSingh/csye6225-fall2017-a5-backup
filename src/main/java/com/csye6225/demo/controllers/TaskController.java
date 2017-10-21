package com.csye6225.demo.controllers;

import com.csye6225.demo.model.Task;
import com.csye6225.demo.model.UserAccount;
import com.csye6225.demo.datalayer.TaskRepository;
import com.csye6225.demo.datalayer.UserRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/*
<Ekta Singh>, <001258567>, <singh.ek@husky.neu.edu>
<Karan Bhavsar>, <001225621>, <bhavsar.ka@husky.neu.edu>
<Bhavesh Sachdev>, <001280940>, <sachdev.b@husky.neu.edu>
<Nikita Dulani>, <001280944>, <dulani.n@husky.neu.edu>
*/


@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserController userController;

    @RequestMapping(value = "/tasks", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String createTask(HttpServletRequest httpRequest,  HttpServletResponse response,
                             @RequestBody Task task) {

        JsonObject jsonObject = new JsonObject();
        String auth = httpRequest.getHeader("Authorization");
        UserAccount userAccount = userRepository.findOne(userController.getUserId(auth));

        if(userAccount != null) {
            task.setUserAccount(userAccount);
            Task savedTask = taskRepository.save(task);
            jsonObject.addProperty("message", "Task Created!");
        }
        else {
            jsonObject.addProperty("message", "Not authorized");
            response.setStatus(403);
        }

        return jsonObject.toString();
    }


    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public String updateTask(HttpServletRequest httpRequest, HttpServletResponse response, @RequestBody Task task, @PathVariable(value = "id")String id) {

        JsonObject jsonObject = new JsonObject();
        boolean exists = taskRepository.exists(id);

        String auth = httpRequest.getHeader("Authorization");
        UserAccount userAccount = userRepository.findOne(userController.getUserId(auth));

        if(userAccount != null) {

            if (exists) {
                Task task1 = taskRepository.findOne(id);
                task1.setDescription(task.getDescription());
                taskRepository.save(task1);
                jsonObject.addProperty("message", "Task Updated!");
            } else {
                jsonObject.addProperty("message", "Task id doesn't exist");
            }
        }
        else {
            jsonObject.addProperty("message", "Not authorized");
            response.setStatus(403);
        }

        return jsonObject.toString();
    }


    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteTask(HttpServletRequest httpRequest, HttpServletResponse response,@PathVariable(value = "id")String id) {

       JsonObject jsonObject = new JsonObject();
       boolean exists = taskRepository.exists(id);

       String auth = httpRequest.getHeader("Authorization");
       UserAccount userAccount = userRepository.findOne(userController.getUserId(auth));

       if(userAccount != null) {
           if(exists){
               taskRepository.delete(id);
               jsonObject.addProperty("message", "Task Deleted!");
           }
           else
           {
               jsonObject.addProperty("message", "Task id doesn't exist");
           }
       }
        else {
        jsonObject.addProperty("message", "Not authorized");
        response.setStatus(403);
    }

        return jsonObject.toString();
    }

    @RequestMapping(value = "/tasks/{id}/attachments", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAttachments(HttpServletRequest httpRequest,  HttpServletResponse response,
                             @RequestBody Task task) {

        JsonObject jsonObject = new JsonObject();
        String auth = httpRequest.getHeader("Authorization");
        UserAccount userAccount = userRepository.findOne(userController.getUserId(auth));

        if(userAccount != null) {
            task.setUserAccount(userAccount);
            Task savedTask = taskRepository.save(task);
            jsonObject.addProperty("message", "Task Created!");
        }
        else {
            jsonObject.addProperty("message", "Not authorized");
            response.setStatus(403);
        }

        return jsonObject.toString();
    }
}

