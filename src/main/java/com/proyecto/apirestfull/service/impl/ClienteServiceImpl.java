package com.proyecto.apirestfull.service.impl;

import com.proyecto.apirestfull.exception.ResourceNotFoundException;
import com.proyecto.apirestfull.model.Cliente;
import com.proyecto.apirestfull.repository.ClienteRepository;
import com.proyecto.apirestfull.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    @Override
    public Cliente crear(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Integer id, Cliente cliente) {
        Cliente existente = obtenerPorId(id);
        existente.setTipoDocumento(cliente.getTipoDocumento());
        existente.setNumeroDocumento(cliente.getNumeroDocumento());
        existente.setNombres(cliente.getNombres());
        existente.setApellidos(cliente.getApellidos());
        existente.setEmail(cliente.getEmail());
        existente.setTelefono(cliente.getTelefono());
        existente.setFechaNacimiento(cliente.getFechaNacimiento());
        existente.setDireccion(cliente.getDireccion());
        return clienteRepository.save(existente);
    }

    @Override
    public void eliminar(Integer id) {
        Cliente existente = obtenerPorId(id);
        clienteRepository.delete(existente);
    }
}
