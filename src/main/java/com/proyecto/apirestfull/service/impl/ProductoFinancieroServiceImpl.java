package com.proyecto.apirestfull.service.impl;

import com.proyecto.apirestfull.exception.ResourceNotFoundException;
import com.proyecto.apirestfull.model.ProductoFinanciero;
import com.proyecto.apirestfull.repository.ProductoFinancieroRepository;
import com.proyecto.apirestfull.service.ProductoFinancieroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoFinancieroServiceImpl implements ProductoFinancieroService {

    private final ProductoFinancieroRepository productoRepository;

    @Override
    public List<ProductoFinanciero> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public ProductoFinanciero obtenerPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    @Override
    public ProductoFinanciero crear(ProductoFinanciero producto) {
        return productoRepository.save(producto);
    }

    @Override
    public ProductoFinanciero actualizar(Integer id, ProductoFinanciero producto) {
        ProductoFinanciero existente = obtenerPorId(id);
        existente.setNombreProducto(producto.getNombreProducto());
        existente.setTipoProducto(producto.getTipoProducto());
        existente.setTasaInteres(producto.getTasaInteres());
        existente.setDescripcion(producto.getDescripcion());
        return productoRepository.save(existente);
    }

    @Override
    public void eliminar(Integer id) {
        ProductoFinanciero existente = obtenerPorId(id);
        productoRepository.delete(existente);
    }
}
