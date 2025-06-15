package com.fiap.indora.services;

import com.fiap.indora.enums.RoleName;
import com.fiap.indora.model.RoleModel;

public interface RoleService {
    RoleModel findByRoleName(RoleName roleName);
}
