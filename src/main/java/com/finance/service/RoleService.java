package com.finance.service;

import com.finance.dto.RoleDTO;
import com.finance.model.Role;
import com.finance.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public RoleDTO addRole(RoleDTO roleDTO) {
        if (roleRepository.existsByRoleName(roleDTO.getRoleName())) {
            throw new IllegalArgumentException("Role already exists");
        }

        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        Role savedRole = roleRepository.save(role);

        return convertToDTO(savedRole);
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(role.getRoleId(), role.getRoleName());
    }
}
