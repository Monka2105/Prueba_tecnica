package com.proyecto.apirestfull.repository;

import com.proyecto.apirestfull.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
    List<Transaccion> findByCuentaId(Integer cuentaId);
}
