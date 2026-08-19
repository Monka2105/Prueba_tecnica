-- =========================================================
-- DML: datos de prueba (v2, ajustado al PDF)
-- Nota: sin tildes en los datos de ejemplo a proposito,
-- para evitar problemas de codificacion al cargar via Docker.
-- =========================================================

SET NAMES utf8mb4;

USE prueba_tecnica_financiera;

-- ---------------------------------------------------------
-- Clientes (todos mayores de edad)
-- ---------------------------------------------------------
INSERT INTO cliente (tipo_documento, numero_documento, nombres, apellidos, email, fecha_nacimiento) VALUES
('CC', '1075263841', 'Laura',     'Martinez Rios',   'laura.martinez@example.com',   '1995-03-14'),
('CC', '1082456321', 'Andres',    'Gomez Perdomo',   'andres.gomez@example.com',     '1988-11-02'),
('CE', '4587912',    'Carlos',    'Ferreira Souza',  'carlos.ferreira@example.com',  '1990-07-22'),
('CC', '1090345678', 'Valentina', 'Rojas Cabrera',   'valentina.rojas@example.com',  '1998-01-30');

-- ---------------------------------------------------------
-- Cuentas (numero_cuenta ya generado con el prefijo correcto:
-- 53 = ahorros, 33 = corriente)
-- ---------------------------------------------------------
INSERT INTO cuenta (tipo_cuenta, numero_cuenta, estado, saldo, saldo_disponible, exenta_gmf, cliente_id) VALUES
('CUENTA_AHORROS',   '5300000001', 'ACTIVA', 1500000.00, 1500000.00, FALSE, 1),
('CUENTA_CORRIENTE', '3300000001', 'ACTIVA', 8000000.00, 8000000.00, TRUE,  2),
('CUENTA_AHORROS',   '5300000002', 'ACTIVA', 500000.00,  500000.00,  FALSE, 3),
('CUENTA_CORRIENTE', '3300000002', 'ACTIVA', 3000000.00, 3000000.00, TRUE,  4);

-- ---------------------------------------------------------
-- Transacciones de ejemplo
-- ---------------------------------------------------------
-- Consignacion en la cuenta de Laura
INSERT INTO transaccion (cuenta_id, tipo_transaccion, tipo_movimiento, monto, saldo_resultante, descripcion) VALUES
(1, 'CONSIGNACION', 'CREDITO', 200000.00, 1700000.00, 'Consignacion en efectivo');

-- Retiro de la cuenta de Andres
INSERT INTO transaccion (cuenta_id, tipo_transaccion, tipo_movimiento, monto, saldo_resultante, descripcion) VALUES
(2, 'RETIRO', 'DEBITO', 500000.00, 7500000.00, 'Retiro por cajero');

-- Transferencia: cuenta 1 (Laura, origen) -> cuenta 3 (Carlos, destino)
INSERT INTO transaccion (cuenta_id, cuenta_relacionada_id, tipo_transaccion, tipo_movimiento, monto, saldo_resultante, descripcion) VALUES
(1, 3, 'TRANSFERENCIA', 'DEBITO', 300000.00, 1400000.00, 'Transferencia enviada');

INSERT INTO transaccion (cuenta_id, cuenta_relacionada_id, tipo_transaccion, tipo_movimiento, monto, saldo_resultante, descripcion) VALUES
(3, 1, 'TRANSFERENCIA', 'CREDITO', 300000.00, 800000.00, 'Transferencia recibida');
