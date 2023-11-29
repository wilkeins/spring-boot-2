package com.user.demo.model;




import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID uuid;
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column
    private String name;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Formato de email inválido")
    @Pattern(regexp = "^[\\w.]+@dominio\\.cl$", message = "El correo debe ser del dominio 'dominio.cl'")
    @Column(nullable = false, unique = true)
    private String email;
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(nullable = false)
    private String password;
    private boolean isActive;
    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime modified;
    private LocalDateTime lastLogin;
    private String token;
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Phone> phones = new HashSet<>();
    public void addPhone(Phone phone) {
        phones.add(phone);
        phone.setUsers(this);
    }

}

