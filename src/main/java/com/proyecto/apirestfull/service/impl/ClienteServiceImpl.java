package com.proyecto.apirestfull.service.impl;

import com.proyecto.apirestfull.exception.ClienteConCuentasException;
import com.proyecto.apirestfull.exception.ClienteMenorEdadException;
import com.proyecto.apirestfull.exception.ResourceNotFoundException;
import com.proyecto.apirestfull.model.Cliente;
import com.proyecto.apirestfull.repository.ClienteRepository;
import com.proyecto.apirestfull.repository.CuentaRepository;
import com.proyecto.apirestfull.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private static final int EDAD_MINIMA = 18;

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;

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
        validarMayorDeEdad(cliente.getFechaNacimiento());
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Integer id, Cliente cliente) {
        Cliente existente = obtenerPorId(id);
        validarMayorDeEdad(cliente.getFechaNacimiento());
        existente.setTipoDocumento(cliente.getTipoDocumento());
        existente.setNumeroDocumento(cliente.getNumeroDocumento());
        existente.setNombres(cliente.getNombres());
        existente.setApellidos(cliente.getApellidos());
        existente.setEmail(cliente.getEmail());
        existente.setFechaNacimiento(cliente.getFechaNacimiento());
        return clienteRepository.save(existente);
    }

    @Override
    public void eliminar(Integer id) {
        Cliente existente = obtenerPorId(id);
        boolean tieneCuentas = !cuentaRepository.findByClienteId(id).isEmpty();
        if (tieneCuentas) {
            throw new ClienteConCuentasException(
                    "No se puede eliminar el cliente con id " + id + " porque tiene cuentas vinculadas");
        }
        clienteRepository.delete(existente);
    }

    private void validarMayorDeEdad(LocalDate fechaNacimiento) {
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < EDAD_MINIMA) {
            throw new ClienteMenorEdadException(
                    "El cliente debe ser mayor de edad (" + EDAD_MINIMA + " años) para ser registrado");
        }
    }
}
