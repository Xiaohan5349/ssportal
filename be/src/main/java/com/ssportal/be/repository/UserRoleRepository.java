package com.ssportal.be.repository;

import com.ssportal.be.model.security.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
