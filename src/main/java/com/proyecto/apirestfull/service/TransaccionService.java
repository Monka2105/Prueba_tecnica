package com.proyecto.apirestfull.service;

import com.proyecto.apirestfull.model.Transaccion;

import java.util.List;

public interface TransaccionService {
    List<Transaccion> listarTodas();
    Transaccion obtenerPorId(Integer id);
    List<Transaccion> obtenerPorCuenta(Integer cuentaId);
    Transaccion registrar(Transaccion transaccion);
}
