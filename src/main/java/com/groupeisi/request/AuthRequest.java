package com.groupeisi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "Veillez renseigner le nom d'utilisateur !!!")
    private String username;
    @NotBlank(message = "Veillez renseigner le mot de passe !!!")
    private String password;
}