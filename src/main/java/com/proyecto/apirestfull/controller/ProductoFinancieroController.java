package com.proyecto.apirestfull.controller;

import com.proyecto.apirestfull.model.ProductoFinanciero;
import com.proyecto.apirestfull.service.ProductoFinancieroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor


public class ProductoFinancieroController {

    private final ProductoFinancieroService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoFinanciero>> listar() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoFinanciero> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductoFinanciero> crear(@Valid @RequestBody ProductoFinanciero producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoFinanciero> actualizar(@PathVariable Integer id,
                                                           @Valid @RequestBody ProductoFinanciero producto) {
        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
}
