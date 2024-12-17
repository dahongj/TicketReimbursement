package com.ticket.web.repository;

import com.ticket.web.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer>{
    Optional<Users> findByUsername(String username);
    Optional<Users> findById(Integer id);
    Optional<Users> findByUsernameAndPassword(String username,String password);
}
