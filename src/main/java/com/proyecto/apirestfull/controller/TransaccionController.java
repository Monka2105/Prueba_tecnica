package com.proyecto.apirestfull.controller;

import com.proyecto.apirestfull.model.Transaccion;
import com.proyecto.apirestfull.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @GetMapping
    public ResponseEntity<List<Transaccion>> listar() {
        return ResponseEntity.ok(transaccionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaccion> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(transaccionService.obtenerPorId(id));
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<Transaccion>> obtenerPorCuenta(@PathVariable Integer cuentaId) {
        return ResponseEntity.ok(transaccionService.obtenerPorCuenta(cuentaId));
    }

    @PostMapping
    public ResponseEntity<Transaccion> registrar(@Valid @RequestBody Transaccion transaccion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionService.registrar(transaccion));
    }
}
