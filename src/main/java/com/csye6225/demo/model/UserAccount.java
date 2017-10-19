package com.csye6225.demo.model;

import javax.persistence.*;
import java.util.Set;
/*
<Ekta Singh>, <001258567>, <singh.ek@husky.neu.edu>
<Karan Bhavsar>, <001225621>, <bhavsar.ka@husky.neu.edu>
<Bhavesh Sachdev>, <001280940>, <sachdev.b@husky.neu.edu>
<Nikita Dulani>, <001280944>, <dulani.n@husky.neu.edu>
*/

@Entity
@Table(name = "user")
public class UserAccount {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long userId;

    @Column
    private String password;

    @Column(unique = true)
    private String email;

    /*@OneToMany
    private Set<String> taskIds;*/

    public UserAccount() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public Set<String> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(Set<String> taskIds) {
        this.taskIds = taskIds;
    }*/
}