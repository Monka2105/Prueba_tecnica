package com.proyecto.apirestfull.repository;

import com.proyecto.apirestfull.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);
    boolean existsByEmail(String email);
}
