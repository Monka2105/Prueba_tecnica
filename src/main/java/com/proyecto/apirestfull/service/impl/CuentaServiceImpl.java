package com.proyecto.apirestfull.service.impl;

import com.proyecto.apirestfull.exception.CuentaNoCancelableException;
import com.proyecto.apirestfull.exception.ResourceNotFoundException;
import com.proyecto.apirestfull.exception.SaldoInvalidoException;
import com.proyecto.apirestfull.model.Cliente;
import com.proyecto.apirestfull.model.Cuenta;
import com.proyecto.apirestfull.repository.ClienteRepository;
import com.proyecto.apirestfull.repository.CuentaRepository;
import com.proyecto.apirestfull.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

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

        if (cuenta.getSaldo() == null) {
            cuenta.setSaldo(BigDecimal.ZERO);
        }

        if (cuenta.getTipoCuenta() == Cuenta.TipoCuenta.CUENTA_AHORROS
                && cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoInvalidoException("Una cuenta de ahorros no puede crearse con saldo negativo");
        }

        cuenta.setCliente(cliente);
        cuenta.setNumeroCuenta(generarNumeroCuenta(cuenta.getTipoCuenta()));
        cuenta.setSaldoDisponible(cuenta.getSaldo());
        cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta activar(Integer id) {
        Cuenta cuenta = obtenerPorId(id);
        cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta inactivar(Integer id) {
        Cuenta cuenta = obtenerPorId(id);
        cuenta.setEstado(Cuenta.EstadoCuenta.INACTIVA);
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta cancelar(Integer id) {
        Cuenta cuenta = obtenerPorId(id);
        if (cuenta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new CuentaNoCancelableException(
                    "Solo se pueden cancelar cuentas con saldo en $0. Saldo actual: " + cuenta.getSaldo());
        }
        cuenta.setEstado(Cuenta.EstadoCuenta.CANCELADA);
        return cuentaRepository.save(cuenta);
    }

    private String generarNumeroCuenta(Cuenta.TipoCuenta tipo) {
        String prefijo = tipo == Cuenta.TipoCuenta.CUENTA_AHORROS ? "53" : "33";
        String numero;
        do {
            StringBuilder sb = new StringBuilder(prefijo);
            for (int i = 0; i < 8; i++) {
                sb.append(RANDOM.nextInt(10));
            }
            numero = sb.toString();
        } while (cuentaRepository.existsByNumeroCuenta(numero));
        return numero;
    }
}
