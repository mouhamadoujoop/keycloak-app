package com.groupeisi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDTO {
    private String id;
    @NotBlank(message = "Veillez renseigner le nom du role !!!")
    private String name;
    private String description;
    @JsonIgnore
    private List<UserDTO> users;
    @JsonIgnore
    private boolean deleted = false;
}