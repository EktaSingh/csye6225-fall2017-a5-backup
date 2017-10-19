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
import java.util.ArrayList;
import java.util.List;

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
    private UserController homeController;

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
            String auth = httpRequest.getHeader("Authorization");
            UserAccount userAccount = userRepository.findOne(homeController.getUserId(auth));

            List<String> taskIdList = userAccount.getTaskIds();
            taskIdList.add(taskId);

            userAccount.setTaskIds(taskIdList);

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
        boolean exists = taskRepository.exists(id);

        if(exists){
            Task task1 = taskRepository.findOne(id);
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

    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Task> getTasks(HttpServletRequest httpRequest) {

        String auth = httpRequest.getHeader("Authorization");
        UserAccount userAccount = userRepository.findOne(homeController.getUserId(auth));

        List<Task> result = new ArrayList<>();

        if(userAccount != null){

            List<String> taskIdList = userAccount.getTaskIds();
            for(String taskId: taskIdList){
                Task task1 = taskRepository.findOne(taskId);
                result.add(task1);
            }
        }

        return result;
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteTask(@PathVariable(value = "id")String id) {

        JsonObject jsonObject = new JsonObject();
        boolean exists = taskRepository.exists(id);

        if(exists){
            taskRepository.delete(id);
            jsonObject.addProperty("message", "Task Deleted!");
        }
        else
        {
            jsonObject.addProperty("message", "Task id doesn't exist");
        }

        return jsonObject.toString();
    }


}
