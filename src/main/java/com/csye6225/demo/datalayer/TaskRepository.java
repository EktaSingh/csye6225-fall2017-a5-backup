package com.csye6225.demo.datalayer;


import com.csye6225.demo.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task,String> {

}