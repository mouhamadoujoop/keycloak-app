package com.groupeisi.controller;

import com.groupeisi.dto.UserDTO;
import com.groupeisi.services.UsersService;
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
public class UsersController {
    @Autowired
    private UsersService usersService;
    private static String MESSAGE = "message";
    private static String RESULT = "data";
    private static String STATUT = "responseCode";
    @Autowired
    private MessageSource messageSource;

    @GetMapping("/users/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable String userId){
        Map<String, Object> output = new HashMap<>();
        try {
            output.put(STATUT, 200);
            output.put(RESULT, usersService.getUserById(userId));
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<Object> addUser(@RequestBody UserDTO userDTO){
        Map<String, Object> output = new HashMap<>();
        try {
            usersService.addUser(userDTO);
            output.put(MESSAGE, messageSource.getMessage("success.addUser", null, Locale.getDefault()));
            output.put(STATUT, 200);
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers(){
        Map<String, Object> output = new HashMap<>();
        try {
            output.put(MESSAGE, messageSource.getMessage("success.listUser", null, Locale.getDefault()));
            output.put(STATUT, 200);
            output.put(RESULT, usersService.getAllUsers());
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{userId}/roles")
    public ResponseEntity<Object> getAllRolesForUser(@PathVariable String userId){
        Map<String, Object> output = new HashMap<>();
        try {
            output.put(STATUT, 200);
            output.put(RESULT, usersService.getAllRolesForUser(userId));
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable String userId,
                                             @RequestBody UserDTO userDTO){
        Map<String, Object> output = new HashMap<>();
        try {
            usersService.updateUser(userId, userDTO);
            output.put(MESSAGE, messageSource.getMessage("success.updateUser", null, Locale.getDefault()));
            output.put(STATUT, 200);
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/setpassword/{userId}/{newPassword}")
    public ResponseEntity<Object> updatePassword(@PathVariable String userId,
                                                 @PathVariable String newPassword){
        Map<String, Object> output = new HashMap<>();
        try {
            usersService.updatePassword(userId, newPassword);
            output.put(MESSAGE, "Mot de passe modifie avec succes ...");
            output.put(STATUT, 200);
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable String userId){
        Map<String, Object> output = new HashMap<>();
        try {
            usersService.deleteUser(userId);
            output.put(MESSAGE, messageSource.getMessage("success.deleteUser", null, Locale.getDefault()));
            output.put(STATUT, 200);
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/roles/{userID}")
    public ResponseEntity<Object> getAllRolesByUser(@PathVariable String userID){
        Map<String, Object> output = new HashMap<>();
        try {
            output.put(MESSAGE, messageSource.getMessage("success.listRole", null, Locale.getDefault()));
            output.put(STATUT, 200);
            output.put(RESULT, usersService.getAllRolesForUser(userID));
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{userId}/{roleName}")
    public ResponseEntity<Object> assignRole(@PathVariable String userId,
                                             @PathVariable String roleName){
        Map<String, Object> output = new HashMap<>();
        try {
            usersService.assignRoleToUser(userId, roleName);
            output.put(MESSAGE, messageSource.getMessage("roleAssign", null, Locale.getDefault()));
            output.put(STATUT, 200);
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{userID}/unAssignRole/{roleName}")
    public ResponseEntity<Object> deleteRoleFromUser(@PathVariable String userID,
                                                     @PathVariable String roleName){
        Map<String, Object> output = new HashMap<>();
        try {
            usersService.removeRoleFromUser(userID, roleName);
            output.put(MESSAGE, messageSource.getMessage("roleDeleteFromUser", null, Locale.getDefault()));
            output.put(STATUT, 200);
            return ResponseEntity.ok(output);
        } catch (Exception e){
            log.info(e.toString());
            output.put(MESSAGE, messageSource.getMessage("error", null, Locale.getDefault()));
            output.put(STATUT, 500);
            return new ResponseEntity<>(output, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}