-- =========================================================
-- Base de datos: prueba_tecnica_financiera
-- Prueba técnica Java Spring Boot - Entidad Financiera
-- CRUD de clientes, productos financieros y transacciones
-- =========================================================

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
    telefono            VARCHAR(20),
    fecha_nacimiento    DATE,
    direccion           VARCHAR(150),
    fecha_registro      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado              BOOLEAN DEFAULT TRUE
);

-- ---------------------------------------------------------
-- Tabla: producto_financiero (catálogo de productos)
-- ---------------------------------------------------------
CREATE TABLE producto_financiero (
    id_producto         INT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto     VARCHAR(80)  NOT NULL,
    tipo_producto       ENUM('CUENTA_AHORROS','CUENTA_CORRIENTE','TARJETA_CREDITO','PRESTAMO','CDT') NOT NULL,
    tasa_interes        DECIMAL(5,2) DEFAULT 0.00,
    descripcion         VARCHAR(255),
    estado              BOOLEAN DEFAULT TRUE
);

-- ---------------------------------------------------------
-- Tabla: cuenta (la cuenta real que un cliente tiene abierta
-- de un producto financiero, con su saldo)
-- ---------------------------------------------------------
CREATE TABLE cuenta (
    id_cuenta           INT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta       VARCHAR(30) NOT NULL UNIQUE,
    cliente_id          INT NOT NULL,
    producto_id         INT NOT NULL,
    saldo               DECIMAL(15,2) DEFAULT 0.00,
    fecha_apertura      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado              BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_cuenta_cliente  FOREIGN KEY (cliente_id)  REFERENCES cliente(id_cliente),
    CONSTRAINT fk_cuenta_producto FOREIGN KEY (producto_id) REFERENCES producto_financiero(id_producto)
);

-- ---------------------------------------------------------
-- Tabla: transaccion (movimientos sobre una cuenta)
-- ---------------------------------------------------------
CREATE TABLE transaccion (
    id_transaccion      INT AUTO_INCREMENT PRIMARY KEY,
    cuenta_id           INT NOT NULL,
    cuenta_destino_id   INT NULL,                   -- solo se usa en transferencias
    tipo_transaccion    ENUM('DEPOSITO','RETIRO','TRANSFERENCIA','PAGO') NOT NULL,
    monto               DECIMAL(15,2) NOT NULL,
    saldo_resultante    DECIMAL(15,2) NOT NULL,
    fecha_transaccion   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descripcion         VARCHAR(255),
    CONSTRAINT fk_transaccion_cuenta         FOREIGN KEY (cuenta_id)         REFERENCES cuenta(id_cuenta),
    CONSTRAINT fk_transaccion_cuenta_destino FOREIGN KEY (cuenta_destino_id) REFERENCES cuenta(id_cuenta)
);
