package com.csye6225.demo.controllers;

import com.csye6225.demo.bean.Task;
import com.csye6225.demo.bean.User;
import com.csye6225.demo.repository.TaskRepository;
import com.csye6225.demo.repository.UserRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TaskController {


    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HomeController homeController;

    @RequestMapping(value = "/tasks", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String createTask(HttpServletRequest httpRequest,
                             @RequestBody Task task) {

        String validateUser = homeController.validateUser(httpRequest);
        boolean valid = validateUser.contains("you are logged in.");

        JsonObject jsonObject = new JsonObject();

        if(valid)
        {
            Task t1 = taskRepository.save(task);
            String taskId = t1.getId().toString();
           // userRepository.findOne();

            jsonObject.addProperty("message", "Task Created!");
        }
        else
        {
            jsonObject.addProperty("message", "Not authorized");
        }


        return jsonObject.toString();
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public String updateTask(@RequestBody Task task, @PathVariable(value = "id")String id) {

        JsonObject jsonObject = new JsonObject();
        Long longId = Long.valueOf(id);
        boolean exists = taskRepository.exists(longId);

        if(exists){
            Task task1 = taskRepository.findOne(longId);
            task1.setDescription(task.getDescription());
            taskRepository.save(task1);
            jsonObject.addProperty("message", "Task Updated!");
        }
        else
        {
            jsonObject.addProperty("message", "Task id doesn't exist");
        }

        return jsonObject.toString();
    }


}
