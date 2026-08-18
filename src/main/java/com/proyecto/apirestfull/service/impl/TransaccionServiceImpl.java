package com.proyecto.apirestfull.service.impl;

import com.proyecto.apirestfull.exception.ResourceNotFoundException;
import com.proyecto.apirestfull.exception.SaldoInsuficienteException;
import com.proyecto.apirestfull.model.Cuenta;
import com.proyecto.apirestfull.model.Transaccion;
import com.proyecto.apirestfull.repository.CuentaRepository;
import com.proyecto.apirestfull.repository.TransaccionRepository;
import com.proyecto.apirestfull.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;

    @Override
    public List<Transaccion> listarTodas() {
        return transaccionRepository.findAll();
    }

    @Override
    public Transaccion obtenerPorId(Integer id) {
        return transaccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transacción no encontrada con id: " + id));
    }

    @Override
    public List<Transaccion> obtenerPorCuenta(Integer cuentaId) {
        return transaccionRepository.findByCuentaId(cuentaId);
    }

    @Override
    @Transactional
    public Transaccion registrar(Transaccion transaccion) {
        Cuenta cuenta = cuentaRepository.findById(transaccion.getCuenta().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta no encontrada con id: " + transaccion.getCuenta().getId()));

        BigDecimal nuevoSaldo;

        switch (transaccion.getTipoTransaccion()) {
            case DEPOSITO -> nuevoSaldo = cuenta.getSaldo().add(transaccion.getMonto());
            case RETIRO, PAGO -> {
                if (cuenta.getSaldo().compareTo(transaccion.getMonto()) < 0) {
                    throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta " + cuenta.getNumeroCuenta());
                }
                nuevoSaldo = cuenta.getSaldo().subtract(transaccion.getMonto());
            }
            case TRANSFERENCIA -> {
                if (cuenta.getSaldo().compareTo(transaccion.getMonto()) < 0) {
                    throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta " + cuenta.getNumeroCuenta());
                }
                nuevoSaldo = cuenta.getSaldo().subtract(transaccion.getMonto());

                if (transaccion.getCuentaDestino() != null) {
                    Cuenta destino = cuentaRepository.findById(transaccion.getCuentaDestino().getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Cuenta destino no encontrada"));
                    destino.setSaldo(destino.getSaldo().add(transaccion.getMonto()));
                    cuentaRepository.save(destino);
                }
            }
            default -> throw new IllegalArgumentException("Tipo de transacción no soportado");
        }

        cuenta.setSaldo(nuevoSaldo);
        cuentaRepository.save(cuenta);

        transaccion.setCuenta(cuenta);
        transaccion.setSaldoResultante(nuevoSaldo);
        return transaccionRepository.save(transaccion);
    }
}
