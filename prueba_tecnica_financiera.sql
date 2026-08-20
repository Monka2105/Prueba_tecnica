-- =========================================================
-- Base de datos: prueba_tecnica_financiera
-- Prueba técnica Java Spring Boot - Entidad Financiera
-- CRUD de clientes, cuentas y transacciones
-- Este esquema refleja las entidades JPA actuales
-- (Cliente, Cuenta, Transaccion). No es necesario ejecutarlo
-- a mano: con spring.jpa.hibernate.ddl-auto=update Hibernate
-- crea/actualiza las tablas automáticamente al levantar la app.
-- =========================================================
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS prueba_tecnica_financiera
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE prueba_tecnica_financiera;

-- ---------------------------------------------------------
-- Tabla: cliente
-- ---------------------------------------------------------
CREATE TABLE cliente (
    id_cliente          INT AUTO_INCREMENT PRIMARY KEY,
    tipo_documento      VARCHAR(20)  NOT NULL,      -- CC, CE, TI, PASAPORTE
    numero_documento    VARCHAR(20)  NOT NULL UNIQUE,
    nombres             VARCHAR(80)  NOT NULL,
    apellidos           VARCHAR(80)  NOT NULL,
    email               VARCHAR(120) NOT NULL UNIQUE,
    fecha_nacimiento    DATE NOT NULL,
    fecha_creacion      DATETIME,
    fecha_modificacion  DATETIME
);

-- ---------------------------------------------------------
-- Tabla: cuenta (cuenta de ahorros o corriente de un cliente)
-- ---------------------------------------------------------
CREATE TABLE cuenta (
    id_cuenta           INT AUTO_INCREMENT PRIMARY KEY,
    tipo_cuenta         ENUM('CUENTA_AHORROS','CUENTA_CORRIENTE') NOT NULL,
    numero_cuenta       CHAR(10) NOT NULL UNIQUE,    -- 10 dígitos: 53xxxxxxxx (ahorros) / 33xxxxxxxx (corriente)
    estado              ENUM('ACTIVA','INACTIVA','CANCELADA') NOT NULL DEFAULT 'ACTIVA',
    saldo               DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    saldo_disponible    DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    exenta_gmf          BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion      DATETIME,
    fecha_modificacion  DATETIME,
    cliente_id          INT NOT NULL,
    CONSTRAINT fk_cuenta_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id_cliente)
);

-- ---------------------------------------------------------
-- Tabla: transaccion (movimientos sobre una cuenta)
-- ---------------------------------------------------------
CREATE TABLE transaccion (
    id_transaccion        INT AUTO_INCREMENT PRIMARY KEY,
    cuenta_id             INT NOT NULL,
    cuenta_relacionada_id INT NULL,                  -- solo se usa en transferencias
    tipo_transaccion      ENUM('CONSIGNACION','RETIRO','TRANSFERENCIA') NOT NULL,
    tipo_movimiento       ENUM('CREDITO','DEBITO') NOT NULL,
    monto                 DECIMAL(15,2) NOT NULL,
    saldo_resultante      DECIMAL(15,2) NOT NULL,
    fecha_transaccion     DATETIME,
    descripcion           VARCHAR(255),
    CONSTRAINT fk_transaccion_cuenta            FOREIGN KEY (cuenta_id)             REFERENCES cuenta(id_cuenta),
    CONSTRAINT fk_transaccion_cuenta_relacionada FOREIGN KEY (cuenta_relacionada_id) REFERENCES cuenta(id_cuenta)
);
