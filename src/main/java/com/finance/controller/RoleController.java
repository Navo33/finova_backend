package com.finance.controller;

import com.finance.dto.RoleDTO;
import com.finance.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role Management", description = "API for managing roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @Operation(summary = "Add a new role")
    public ResponseEntity<RoleDTO> addRole(@Valid @RequestBody RoleDTO roleDTO) {
        RoleDTO savedRole = roleService.addRole(roleDTO);
        return ResponseEntity.ok(savedRole);
    }

    @GetMapping
    @Operation(summary = "Get all roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}
