package com.csye6225.demo.datalayer;

import com.csye6225.demo.model.Attachment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AttachmentRepository extends CrudRepository<Attachment,String>{

}
