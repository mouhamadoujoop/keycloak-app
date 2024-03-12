package com.groupeisi.services;

import com.groupeisi.dto.RoleDTO;
import com.groupeisi.dto.UserDTO;
import com.groupeisi.security.KeycloakInstance;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.ApplicationException;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UsersService {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private KeycloakInstance keycloakInstance;
    @Autowired
    private RolesService rolesService;
    @Value("${keycloak.realm}")
    private String realm;

    public void updatePassword(String userId, String newPassword) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        UsersResource usersResource = keycloak.realm(realm).users();
        UserResource userResource = usersResource.get(userId);

        // Create a new password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(newPassword);

        userResource.resetPassword(passwordCred);
    }

    public UserRepresentation addUser(UserDTO userDTO) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        // Get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Create user (requires manage-users role)
        Response response = usersResource.create(user);

        String userId = CreatedResponseUtil.getCreatedId(response);

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userDTO.getPassword());

        // Set password credential
        UserResource userResource = usersResource.get(userId);
        userResource.resetPassword(passwordCred);

        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()){
            for (String role: userDTO.getRoles()){
                assignRoleToUser(userId, role);
            }
        }

        return userResource.toRepresentation();
    }

    public UserRepresentation updateUser(String userId, UserDTO updatedUserDTO) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserResource userResource = usersResource.get(userId);
        UserRepresentation existingUser = userResource.toRepresentation();

        // Update user details
        existingUser.setFirstName(updatedUserDTO.getFirstName());
        existingUser.setLastName(updatedUserDTO.getLastName());
        existingUser.setEmail(updatedUserDTO.getEmail());

        // Update user
        userResource.update(existingUser);

        return existingUser;
    }

    public Response deleteUser(String userId) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserResource userResource = usersResource.get(userId);
        userResource.remove();

        return Response.noContent().build();
    }

    public Response getAllUsers() {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> users = usersResource.list();
        return Response.ok(users).build();
    }

    public Response getAllRolesForUser(String userId) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);

        // Get all roles for user
        RoleMappingResource roleMappingResource = realmResource.users().get(userId).roles();
        List<RoleRepresentation> userRoles = roleMappingResource.realmLevel().listAll();

        return Response.ok(userRoles).build();
    }

    public RoleRepresentation assignRoleToUser(String userId, String roleName) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        RolesResource rolesResource = realmResource.roles();

        List<RoleRepresentation> roles = rolesResource.list()
                .stream().filter(role-> role.getName().equalsIgnoreCase(roleName))
                .collect(Collectors.toList());

        if (roles==null || roles.isEmpty()){
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setName(roleName);
            rolesService.createRole(roleDTO);
        }

        // Get role by name
        RoleResource roleResource = rolesResource.get(roleName);
        RoleRepresentation roleRepresentation = roleResource.toRepresentation();

        // Get user by id
        realmResource.users().get(userId).toRepresentation();

        // Assign role to user
        realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));

        return roleRepresentation;
    }
    public Response getUserById(String userId) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserResource userResource = usersResource.get(userId);
        UserRepresentation user = userResource.toRepresentation();

        return Response.ok(user).build();
}

    public Response removeRoleFromUser(String userId, String roleName) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);

        // Get role by name
        RoleResource roleResource = realmResource.roles().get(roleName);
        RoleRepresentation roleRepresentation = roleResource.toRepresentation();

        // Remove role from user
        realmResource.users().get(userId).roles().realmLevel().remove(Collections.singletonList(roleRepresentation));

        return Response.noContent().build();
    }

}