package com.user.demo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    @Email(message = "Formato de correo electrónico inválido")
    private String email;
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
    private Set<PhoneDTO> phones;
}
