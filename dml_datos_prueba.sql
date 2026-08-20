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
INSERT INTO cliente (tipo_documento, numero_documento, nombres, apellidos, email, telefono, fecha_nacimiento, direccion, estado) VALUES
('CC', '1075263841', 'Laura', 'Martínez Ríos',  'laura.martinez@example.com', '3011234567', '1995-03-14', 'Cra 5 # 12-34, Neiva', TRUE),
('CC', '1082456321', 'Andrés', 'Gómez Perdomo', 'andres.gomez@example.com',   '3109876543', '1988-11-02', 'Cl 20 # 8-15, Neiva',  TRUE),
('CE', '4587912',    'Carlos', 'Ferreira Souza', 'carlos.ferreira@example.com','3157894561', '1990-07-22', 'Av 26 # 40-10, Bogotá',TRUE),
('CC', '1090345678', 'Valentina', 'Rojas Cabrera','valentina.rojas@example.com','3201122334','1998-01-30', 'Cl 9 # 3-21, Neiva',   TRUE);

-- ---------------------------------------------------------
-- Productos financieros (catálogo)
-- ---------------------------------------------------------
INSERT INTO producto_financiero (nombre_producto, tipo_producto, tasa_interes, descripcion, estado) VALUES
('Cuenta de Ahorros Clásica',   'CUENTA_AHORROS',   2.50, 'Cuenta de ahorros con rendimiento básico',              TRUE),
('Cuenta Corriente Empresarial','CUENTA_CORRIENTE', 0.00, 'Cuenta corriente para manejo de flujo de caja',         TRUE),
('Tarjeta de Crédito Gold',     'TARJETA_CREDITO',  24.90,'Tarjeta de crédito con cupo hasta 10 millones',         TRUE),
('Préstamo Libre Inversión',    'PRESTAMO',         18.50,'Préstamo de libre destinación a 36 meses',              TRUE),
('CDT a 180 días',              'CDT',              9.20, 'Certificado de depósito a término fijo a 180 días',     TRUE);

-- ---------------------------------------------------------
-- Cuentas (cada cliente abre una o más cuentas de un producto)
-- ---------------------------------------------------------
INSERT INTO cuenta (numero_cuenta, cliente_id, producto_id, saldo, estado) VALUES
('AH-000001', 1, 1, 1500000.00, TRUE),  -- Laura      -> Cuenta de Ahorros
('CC-000001', 2, 2, 8000000.00, TRUE),  -- Andrés     -> Cuenta Corriente
('AH-000002', 3, 1, 500000.00,  TRUE),  -- Carlos     -> Cuenta de Ahorros
('CDT-000001',4, 5, 3000000.00, TRUE);  -- Valentina  -> CDT

-- ---------------------------------------------------------
-- Transacciones de ejemplo
-- ---------------------------------------------------------
-- Depósito a la cuenta de Laura
INSERT INTO transaccion (cuenta_id, tipo_transaccion, monto, saldo_resultante, descripcion) VALUES
(1, 'DEPOSITO', 200000.00, 1700000.00, 'Consignación en efectivo');

-- Retiro de la cuenta de Andrés
INSERT INTO transaccion (cuenta_id, tipo_transaccion, monto, saldo_resultante, descripcion) VALUES
(2, 'RETIRO', 500000.00, 7500000.00, 'Retiro por cajero');

-- Pago con la cuenta de Carlos
INSERT INTO transaccion (cuenta_id, tipo_transaccion, monto, saldo_resultante, descripcion) VALUES
(3, 'PAGO', 100000.00, 400000.00, 'Pago de servicio público');

-- Transferencia de Laura hacia Carlos
INSERT INTO transaccion (cuenta_id, cuenta_destino_id, tipo_transaccion, monto, saldo_resultante, descripcion) VALUES
(1, 3, 'TRANSFERENCIA', 300000.00, 1400000.00, 'Transferencia entre cuentas propias/terceros');
