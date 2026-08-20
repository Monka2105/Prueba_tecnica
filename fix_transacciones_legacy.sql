-- =========================================================
-- Fix puntual para datos legacy en la tabla `transaccion`
-- Ejecutar UNA VEZ contra la base de datos ya existente
-- (la que tiene filas con tipo_transaccion = 'DEPOSITO' o 'PAGO',
-- valores que ya no existen en el enum TipoTransaccion del código:
-- CONSIGNACION, RETIRO, TRANSFERENCIA)
-- =========================================================
USE prueba_tecnica_financiera;

UPDATE transaccion SET tipo_transaccion = 'CONSIGNACION' WHERE tipo_transaccion = 'DEPOSITO';
UPDATE transaccion SET tipo_transaccion = 'RETIRO'        WHERE tipo_transaccion = 'PAGO';

-- Si la columna tipo_movimiento quedó nula en filas antiguas, la completamos
-- (CONSIGNACION = CREDITO, RETIRO/TRANSFERENCIA débito por defecto)
UPDATE transaccion SET tipo_movimiento = 'CREDITO' WHERE tipo_movimiento IS NULL AND tipo_transaccion = 'CONSIGNACION';
UPDATE transaccion SET tipo_movimiento = 'DEBITO'  WHERE tipo_movimiento IS NULL AND tipo_transaccion IN ('RETIRO','TRANSFERENCIA');
