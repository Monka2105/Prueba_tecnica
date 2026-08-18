package com.proyecto.apirestfull.repository;

import com.proyecto.apirestfull.model.ProductoFinanciero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoFinancieroRepository extends JpaRepository<ProductoFinanciero, Integer> {
}
