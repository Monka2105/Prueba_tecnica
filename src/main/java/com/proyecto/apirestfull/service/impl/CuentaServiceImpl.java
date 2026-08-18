package com.proyecto.apirestfull.service.impl;

import com.proyecto.apirestfull.exception.ResourceNotFoundException;
import com.proyecto.apirestfull.model.Cliente;
import com.proyecto.apirestfull.model.Cuenta;
import com.proyecto.apirestfull.model.ProductoFinanciero;
import com.proyecto.apirestfull.repository.ClienteRepository;
import com.proyecto.apirestfull.repository.CuentaRepository;
import com.proyecto.apirestfull.repository.ProductoFinancieroRepository;
import com.proyecto.apirestfull.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoFinancieroRepository productoRepository;

    @Override
    public List<Cuenta> listarTodas() {
        return cuentaRepository.findAll();
    }

    @Override
    public Cuenta obtenerPorId(Integer id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id: " + id));
    }

    @Override
    public List<Cuenta> obtenerPorCliente(Integer clienteId) {
        return cuentaRepository.findByClienteId(clienteId);
    }

    @Override
    public Cuenta crear(Cuenta cuenta) {
        Cliente cliente = clienteRepository.findById(cuenta.getCliente().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con id: " + cuenta.getCliente().getId()));
        ProductoFinanciero producto = productoRepository.findById(cuenta.getProducto().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + cuenta.getProducto().getId()));

        cuenta.setCliente(cliente);
        cuenta.setProducto(producto);
        return cuentaRepository.save(cuenta);
    }

    @Override
    public void eliminar(Integer id) {
        Cuenta existente = obtenerPorId(id);
        cuentaRepository.delete(existente);
    }
}
