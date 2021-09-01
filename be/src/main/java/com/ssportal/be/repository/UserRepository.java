package com.ssportal.be.repository;

import com.ssportal.be.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);


    User findByEmail(String email);

    List<User> findAll();
}
