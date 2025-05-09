package com.ecommerce.repository;

import com.ecommerce.model.Role;
import com.ecommerce.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleEnum name);
}