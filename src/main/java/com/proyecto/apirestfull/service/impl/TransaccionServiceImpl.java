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

        return switch (transaccion.getTipoTransaccion()) {
            case CONSIGNACION -> procesarConsignacion(cuenta, transaccion);
            case RETIRO -> procesarRetiro(cuenta, transaccion);
            case TRANSFERENCIA -> procesarTransferencia(cuenta, transaccion);
        };
    }

    private Transaccion procesarConsignacion(Cuenta cuenta, Transaccion transaccion) {
        BigDecimal nuevoSaldo = cuenta.getSaldo().add(transaccion.getMonto());
        cuenta.setSaldo(nuevoSaldo);
        cuenta.setSaldoDisponible(nuevoSaldo);
        cuentaRepository.save(cuenta);

        transaccion.setTipoMovimiento(Transaccion.TipoMovimiento.CREDITO);
        transaccion.setSaldoResultante(nuevoSaldo);
        return transaccionRepository.save(transaccion);
    }

    private Transaccion procesarRetiro(Cuenta cuenta, Transaccion transaccion) {
        if (cuenta.getSaldo().compareTo(transaccion.getMonto()) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta " + cuenta.getNumeroCuenta());
        }
        BigDecimal nuevoSaldo = cuenta.getSaldo().subtract(transaccion.getMonto());
        cuenta.setSaldo(nuevoSaldo);
        cuenta.setSaldoDisponible(nuevoSaldo);
        cuentaRepository.save(cuenta);

        transaccion.setTipoMovimiento(Transaccion.TipoMovimiento.DEBITO);
        transaccion.setSaldoResultante(nuevoSaldo);
        return transaccionRepository.save(transaccion);
    }

    private Transaccion procesarTransferencia(Cuenta origen, Transaccion transaccion) {
        if (transaccion.getCuentaRelacionada() == null || transaccion.getCuentaRelacionada().getId() == null) {
            throw new IllegalArgumentException("La transferencia requiere una cuenta destino (cuentaRelacionada)");
        }

        Cuenta destino = cuentaRepository.findById(transaccion.getCuentaRelacionada().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta destino no encontrada"));

        if (origen.getSaldo().compareTo(transaccion.getMonto()) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta " + origen.getNumeroCuenta());
        }

        // Débito en la cuenta de envío
        BigDecimal saldoOrigen = origen.getSaldo().subtract(transaccion.getMonto());
        origen.setSaldo(saldoOrigen);
        origen.setSaldoDisponible(saldoOrigen);
        cuentaRepository.save(origen);

        // Crédito en la cuenta de recepción
        BigDecimal saldoDestino = destino.getSaldo().add(transaccion.getMonto());
        destino.setSaldo(saldoDestino);
        destino.setSaldoDisponible(saldoDestino);
        cuentaRepository.save(destino);

        // Movimiento débito (cuenta origen) - el que se devuelve como respuesta principal
        Transaccion debito = Transaccion.builder()
                .cuenta(origen)
                .cuentaRelacionada(destino)
                .tipoTransaccion(Transaccion.TipoTransaccion.TRANSFERENCIA)
                .tipoMovimiento(Transaccion.TipoMovimiento.DEBITO)
                .monto(transaccion.getMonto())
                .saldoResultante(saldoOrigen)
                .descripcion(transaccion.getDescripcion())
                .build();
        transaccionRepository.save(debito);

        // Movimiento crédito (cuenta destino)
        Transaccion credito = Transaccion.builder()
                .cuenta(destino)
                .cuentaRelacionada(origen)
                .tipoTransaccion(Transaccion.TipoTransaccion.TRANSFERENCIA)
                .tipoMovimiento(Transaccion.TipoMovimiento.CREDITO)
                .monto(transaccion.getMonto())
                .saldoResultante(saldoDestino)
                .descripcion(transaccion.getDescripcion())
                .build();
        transaccionRepository.save(credito);

        return debito;
    }
}
