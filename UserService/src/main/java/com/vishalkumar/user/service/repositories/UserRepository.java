package com.vishalkumar.user.service.repositories;

import com.vishalkumar.user.service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String>
{
    //if you want to implement any custom method or query
    //write

    //User Operation


}
