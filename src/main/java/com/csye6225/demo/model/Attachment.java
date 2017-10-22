package com.csye6225.demo.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

/*
<Ekta Singh>, <001258567>, <singh.ek@husky.neu.edu>
<Karan Bhavsar>, <001225621>, <bhavsar.ka@husky.neu.edu>
<Bhavesh Sachdev>, <001280940>, <sachdev.b@husky.neu.edu>
<Nikita Dulani>, <001280944>, <dulani.n@husky.neu.edu>
*/

@Entity
public class Attachment implements Persistable<String> {


    @Id
    @Column(unique = true)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Transient
    private MultipartFile multipartFile;

   /* @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="taskId", nullable = false)
    private Task task;
*/
    public String getId() {
        return id;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

   /* public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }*/

    @Override
    public boolean isNew() {
        return this.id == null;
    }

}
