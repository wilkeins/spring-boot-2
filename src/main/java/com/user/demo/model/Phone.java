package com.user.demo.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.*;


@Entity
@Table(name = "phones")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Pattern(regexp = "^[0-9]+$", message = "El número de teléfono debe ser numérico")
    //@Size(min = 7, max = 10, message = "El número de teléfono debe tener entre 7 y 10 dígitos")
    @Column(nullable = false)
    private String number;

    @NotBlank(message = "El código de ciudad no puede estar vacío")
    @Column(nullable = false)
    private String citycode;

    @NotBlank(message = "El código de país no puede estar vacío")
    @Column(nullable = false)
    private String countrycode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private Users users;

}

