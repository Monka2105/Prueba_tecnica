-- =========================================================
-- DML: datos de prueba
-- Ejecutar DESPUÉS de que existan las tablas
-- (ya sea con prueba_tecnica_financiera.sql o con ddl-auto=update)
-- =========================================================
SET NAMES utf8mb4;
USE prueba_tecnica_financiera;

-- ---------------------------------------------------------
-- Clientes
-- ---------------------------------------------------------
INSERT INTO cliente (tipo_documento, numero_documento, nombres, apellidos, email, fecha_nacimiento, fecha_creacion, fecha_modificacion) VALUES
('CC', '1075263841', 'Laura', 'Martínez Ríos',   'laura.martinez@example.com',  '1995-03-14', NOW(), NOW()),
('CC', '1082456321', 'Andrés', 'Gómez Perdomo',  'andres.gomez@example.com',    '1988-11-02', NOW(), NOW()),
('CE', '4587912',    'Carlos', 'Ferreira Souza', 'carlos.ferreira@example.com', '1990-07-22', NOW(), NOW()),
('CC', '1090345678', 'Valentina', 'Rojas Cabrera','valentina.rojas@example.com','1998-01-30', NOW(), NOW());

-- ---------------------------------------------------------
-- Cuentas (número de 10 dígitos: 53xxxxxxxx ahorros, 33xxxxxxxx corriente)
-- ---------------------------------------------------------
INSERT INTO cuenta (tipo_cuenta, numero_cuenta, estado, saldo, saldo_disponible, exenta_gmf, fecha_creacion, fecha_modificacion, cliente_id) VALUES
('CUENTA_AHORROS',   '5300000001', 'ACTIVA', 1500000.00, 1500000.00, FALSE, NOW(), NOW(), 1), -- Laura
('CUENTA_CORRIENTE', '3300000001', 'ACTIVA', 8000000.00, 8000000.00, FALSE, NOW(), NOW(), 2), -- Andrés
('CUENTA_AHORROS',   '5300000002', 'ACTIVA', 500000.00,  500000.00,  TRUE,  NOW(), NOW(), 3), -- Carlos
('CUENTA_AHORROS',   '5300000003', 'ACTIVA', 3000000.00, 3000000.00, FALSE, NOW(), NOW(), 4); -- Valentina

-- ---------------------------------------------------------
-- Transacciones de ejemplo
-- ---------------------------------------------------------
-- Consignación en efectivo a la cuenta de Laura
INSERT INTO transaccion (cuenta_id, tipo_transaccion, tipo_movimiento, monto, saldo_resultante, descripcion) VALUES
(1, 'CONSIGNACION', 'CREDITO', 200000.00, 1700000.00, 'Consignación en efectivo');

-- Retiro de la cuenta de Andrés
INSERT INTO transaccion (cuenta_id, tipo_transaccion, tipo_movimiento, monto, saldo_resultante, descripcion) VALUES
(2, 'RETIRO', 'DEBITO', 500000.00, 7500000.00, 'Retiro por cajero');

-- Transferencia de Laura hacia Carlos (débito en origen, crédito en destino)
INSERT INTO transaccion (cuenta_id, cuenta_relacionada_id, tipo_transaccion, tipo_movimiento, monto, saldo_resultante, descripcion) VALUES
(1, 3, 'TRANSFERENCIA', 'DEBITO', 300000.00, 1400000.00, 'Transferencia entre cuentas propias/terceros'),
(3, 1, 'TRANSFERENCIA', 'CREDITO', 300000.00, 800000.00, 'Transferencia entre cuentas propias/terceros');
