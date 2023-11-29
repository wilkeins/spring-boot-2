package com.user.demo.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDTO {
    @NotBlank(message = "El número de teléfono no puede estar vacío")
    private String number;
    @NotBlank(message = "El código de ciudad no puede estar vacío")
    private String citycode;
    @NotBlank(message = "El código de país no puede estar vacío")
    private String countrycode;

}
