package com.groupeisi.services;

import com.groupeisi.dto.RoleDTO;
import com.groupeisi.exception.ApplicationException;
import com.groupeisi.security.KeycloakInstance;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
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
public class RolesService {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private KeycloakInstance keycloakInstance;
    @Value("${keycloak.realm}")
    private String realm;

    public RoleRepresentation createRole(RoleDTO roleDTO) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName(roleDTO.getName());
        roleRepresentation.setDescription(roleDTO.getDescription());

        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        realmResource.roles().create(roleRepresentation);

        RoleResource roleResource = realmResource.roles().get(roleDTO.getName());
        RoleRepresentation savedRole = roleResource.toRepresentation();
        return savedRole;
    }

    public RoleRepresentation getRoleByName(String roleName) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        RolesResource rolesResource = realmResource.roles();

        // Check if role exists
        if (rolesResource.get(roleName)==null) {
            throw new ApplicationException(messageSource.getMessage("role.notFound", null, Locale.getDefault()));
        }

        // Get role information by name
        RoleRepresentation roleRepresentation = rolesResource.get(roleName).toRepresentation();

        return roleRepresentation;
    }

    public Response getAllRoles() {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        RolesResource rolesResource = realmResource.roles();

        // Get all roles
        List<RoleRepresentation> allRoles = rolesResource.list();

        return Response.ok(allRoles).build();
    }

    public Response updateRole(String roleName, RoleDTO updatedRole) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        RolesResource rolesResource = realmResource.roles();

        // Vérifier si le rôle existe
        RoleRepresentation existingRole = rolesResource.get(roleName).toRepresentation();
        if (existingRole == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(
                    messageSource.getMessage("role.notFound", null, Locale.getDefault())
            ).build();
        }

        // Mettre à jour les propriétés du rôle
        existingRole.setName(updatedRole.getName());
        existingRole.setDescription(updatedRole.getDescription());

        // Mettre à jour le rôle
        rolesResource.get(roleName).update(existingRole);

        return Response.ok().build();
    }

    public Response getAllRolesForUser(String userId) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);

        // Get all roles for user
        RoleMappingResource roleMappingResource = realmResource.users().get(userId).roles();
        List<RoleRepresentation> userRoles = roleMappingResource.realmLevel().listAll();

        return Response.ok(userRoles).build();
    }

    public Response deleteRole(String roleName) {
        Keycloak keycloak = keycloakInstance.getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        RolesResource rolesResource = realmResource.roles();

        // Check if role exists
        if (rolesResource.get(roleName) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(
                    messageSource.getMessage("role.notFound", null, Locale.getDefault())
            ).build();
        }

        // Delete the role
        rolesResource.get(roleName).remove();

        return Response.noContent().build();
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
            createRole(roleDTO);
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