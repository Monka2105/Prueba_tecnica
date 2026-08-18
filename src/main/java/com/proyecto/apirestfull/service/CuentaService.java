package com.proyecto.apirestfull.service;

import com.proyecto.apirestfull.model.Cuenta;

import java.util.List;

public interface CuentaService {
    List<Cuenta> listarTodas();
    Cuenta obtenerPorId(Integer id);
    List<Cuenta> obtenerPorCliente(Integer clienteId);
    Cuenta crear(Cuenta cuenta);
    void eliminar(Integer id);
}
