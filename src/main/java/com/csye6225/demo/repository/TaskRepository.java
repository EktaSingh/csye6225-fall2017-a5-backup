package com.csye6225.demo.repository;


import com.csye6225.demo.bean.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task,String> {

}