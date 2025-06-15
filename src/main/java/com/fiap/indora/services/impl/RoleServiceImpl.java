package com.fiap.indora.services.impl;


import com.fiap.indora.enums.RoleName;
import com.fiap.indora.exceptions.NotFoundException;
import com.fiap.indora.model.RoleModel;
import com.fiap.indora.repositories.RoleRepository;
import com.fiap.indora.services.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleModel findByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(
                () -> new NotFoundException("Role not found")
        );
    }

}
