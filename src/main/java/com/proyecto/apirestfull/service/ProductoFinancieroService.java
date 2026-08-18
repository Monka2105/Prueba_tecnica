package com.proyecto.apirestfull.service;

import com.proyecto.apirestfull.model.ProductoFinanciero;

import java.util.List;

public interface ProductoFinancieroService {
    List<ProductoFinanciero> listarTodos();
    ProductoFinanciero obtenerPorId(Integer id);
    ProductoFinanciero crear(ProductoFinanciero producto);
    ProductoFinanciero actualizar(Integer id, ProductoFinanciero producto);
    void eliminar(Integer id);
}
