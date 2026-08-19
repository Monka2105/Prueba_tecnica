package com.proyecto.apirestfull.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id;

    @NotBlank
    @Column(name = "tipo_documento", length = 20, nullable = false)
    private String tipoDocumento;

    @NotBlank
    @Column(name = "numero_documento", length = 20, nullable = false, unique = true)
    private String numeroDocumento;

    @NotBlank
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    @Column(name = "nombres", length = 80, nullable = false)
    private String nombres;

    @NotBlank
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    @Column(name = "apellidos", length = 80, nullable = false)
    private String apellidos;

    @Email
    @NotBlank
    @Column(name = "email", length = 120, nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    public void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        fechaCreacion = ahora;
        fechaModificacion = ahora;
    }

    @PreUpdate
    public void preUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
}
