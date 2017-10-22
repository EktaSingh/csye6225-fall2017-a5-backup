package com.csye6225.demo.controllers;

import com.csye6225.demo.datalayer.AttachmentRepository;
import com.csye6225.demo.model.Attachment;
import com.csye6225.demo.model.Task;
import com.csye6225.demo.model.UserAccount;
import com.csye6225.demo.datalayer.TaskRepository;
import com.csye6225.demo.datalayer.UserRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "//home//ekta//Desktop//Attachments//";

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
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

    @RequestMapping(value = "/tasks/{id}/attachments", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String saveAttachments(HttpServletRequest httpRequest, HttpServletResponse response,
                                  @PathVariable(value = "id")String id,
                                  @RequestParam("file") MultipartFile file) {

        JsonObject jsonObject = new JsonObject();
        String auth = httpRequest.getHeader("Authorization");
        UserAccount userAccount = userRepository.findOne(userController.getUserId(auth));

        if (userAccount != null) {

            if (file.isEmpty()) {
                jsonObject.addProperty("message", "Empty file");
                response.setStatus(400);
            }


            try {

                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                String filePath = file.getOriginalFilename();
                Path path = Paths.get(UPLOADED_FOLDER + filePath);
                Files.write(path, bytes);

                Task task = taskRepository.findOne(id);

                Attachment attachment = new Attachment();

                attachment.setMultipartFile(file);
               // attachment.setTask(task);

                Attachment savedAttachment = attachmentRepository.save(attachment);

                List<Attachment> attachments = task.getAttachmentList();

                if(attachments == null)
                {
                    attachments = new ArrayList<>();
                }

                attachments.add(savedAttachment);

                task.setAttachmentList(attachments);

                taskRepository.save(task);

                jsonObject.addProperty("id", attachment.getId());
                jsonObject.addProperty("url", attachment.getMultipartFile().toString());

            } catch (IOException e) {
                e.printStackTrace();
                jsonObject.addProperty("message", e.getLocalizedMessage());
            }

        } else {
            jsonObject.addProperty("message", "Not authorized");
            response.setStatus(403);
        }

        return jsonObject.toString();
    }
}

