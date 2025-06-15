package com.fiap.indora.repositories;

import com.fiap.indora.enums.RoleName;
import com.fiap.indora.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    Optional<RoleModel> findByRoleName(RoleName roleName);
}
