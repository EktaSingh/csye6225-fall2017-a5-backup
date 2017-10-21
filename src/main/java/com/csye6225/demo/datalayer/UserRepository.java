package com.csye6225.demo.datalayer;

import com.csye6225.demo.model.UserAccount;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserAccount,Long>{

    @Override
    UserAccount findOne(Long aLong);

    UserAccount findByEmail(String email);
}