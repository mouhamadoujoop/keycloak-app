package com.groupeisi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String id;
    @NotBlank(message = "Veillez renseigner le prenom de l'utilisateur !!!")
    private String firstName;
    @NotBlank(message = "Veillez renseigner le nom de l'utilisateur !!!")
    private String lastName;
    private String email;
    @NotBlank(message = "Veillez renseigner le nom d'utilisateur !!!")
    private String userName;
    @NotBlank(message = "Veillez renseigner le mot de passe de l'utilisateur !!!")
    private String password;
    private List<String> roles;
}