package com.groupeisi.controller;

import com.groupeisi.dto.RoleDTO;
import com.groupeisi.services.RolesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("keycloakapp/api")
@Slf4j
public class RolesController {
    private static final String MESSAGE = "message";
    private static final String RESULT = "data";
    private static final String STATUT = "responseCode";
    @Autowired
    private RolesService rolesService;
    @Autowired
    private MessageSource messageSource;

    @PostMapping("/roles")
    public ResponseEntity<Object> addRole(@RequestBody RoleDTO roleDTO){
        Map<String, Object> output = new HashMap<>();
        try {
            rolesService.createRole(roleDTO);
            output.put(MESSAGE, messageSource.getMessage("success.addRole", null, Locale.getDefault()));
            output.put(STATUT, 200);
            return ResponseEntity.ok(output);}
        catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/roles/{roleName}")
    public ResponseEntity<Object> getRoleByName(@PathVariable String roleName){
        Map<String, Object> output = new HashMap<>();
        try {
            output.put(STATUT, 200);
            output.put(RESULT, rolesService.getRoleByName(roleName));
            return ResponseEntity.ok(output);
        }  catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<Object> getAllRoles(){
        Map<String, Object> output = new HashMap<>();
        try {
            output.put(MESSAGE, messageSource.getMessage("success.listRole", null, Locale.getDefault()));
            output.put(STATUT, 200);
            output.put(RESULT, rolesService.getAllRoles());
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/roles/{roleName}")
    public ResponseEntity<Object> updateRole(@PathVariable String roleName,
                                             @RequestBody RoleDTO roleDTO){
        Map<String, Object> output = new HashMap<>();
        try {
            rolesService.updateRole(roleName, roleDTO);
            output.put(MESSAGE, messageSource.getMessage("success.updateRole", null, Locale.getDefault()));
            output.put(STATUT, 200);
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/roles/{roleName}")
    public ResponseEntity<Object> deleteRole(@PathVariable String roleName){
        Map<String, Object> output = new HashMap<>();
        try {
            rolesService.deleteRole(roleName);
            output.put(MESSAGE, messageSource.getMessage("success.deleteRole", null, Locale.getDefault()));
            output.put(STATUT, 200);
            return ResponseEntity.ok(output);
        }  catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}