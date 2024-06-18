package com.WhatsappClone.Repository;

import com.WhatsappClone.Modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {    //jpa contains methods for crud operations.

    public User findByEmail(String email);
    @Query("select u from User u where u.Name Like %:query% or u.email Like %:query%")   //to search name letter by letter
    public List<User> searchUser(@Param("query") String query);
}
