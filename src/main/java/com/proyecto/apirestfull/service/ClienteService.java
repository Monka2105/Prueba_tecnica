package com.proyecto.apirestfull.service;

import com.proyecto.apirestfull.model.Cliente;

import java.util.List;

public interface ClienteService {
    List<Cliente> listarTodos();
    Cliente obtenerPorId(Integer id);
    Cliente crear(Cliente cliente);
    Cliente actualizar(Integer id, Cliente cliente);
    void eliminar(Integer id);
}
