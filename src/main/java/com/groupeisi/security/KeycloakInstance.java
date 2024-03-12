package com.groupeisi.security;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KeycloakInstance {
    static Keycloak keycloak;
    @Value("${keycloak.baseUrl}")
    private String baseUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client-secret}")
    private String secret;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.name}")
    private String username;
    @Value("${keycloak.password}")
    private String password;
    public Keycloak getKeycloakInstance() {
        if(keycloak == null) {
            keycloak = KeycloakBuilder
                    .builder().serverUrl(baseUrl).realm(realm)
                    .clientSecret(secret)
                    .clientId(clientId).grantType(OAuth2Constants.PASSWORD)
                    .username(username).password(password).build();
        }
        return keycloak;
    }
}