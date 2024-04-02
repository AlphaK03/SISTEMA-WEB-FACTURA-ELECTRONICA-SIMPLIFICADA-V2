CREATE DATABASE IF NOT EXISTS Prueba_proyecto;
USE Prueba_proyecto;

CREATE TABLE Administrador (
    identificacion VARCHAR(20) NOT NULL,
    nombre VARCHAR(30) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(20) NOT NULL,
    contrasena VARCHAR(20) NOT NULL,
    rol VARCHAR(3) NOT NULL DEFAULT 'ADM',  -- Rol predefinido como 'ADM' (Administrador)
    PRIMARY KEY(identificacion)
);

CREATE TABLE Proveedor (
    identificacion VARCHAR(20) NOT NULL,
    nombre VARCHAR(30) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(20) NOT NULL,
    contrasena VARCHAR(20) NOT NULL,
    rol VARCHAR(3) NOT NULL DEFAULT 'PRO',  -- Rol predefinido como 'PRO' (Proveedor)
    PRIMARY KEY(identificacion)
);

CREATE TABLE Cliente (
    identificacion VARCHAR(20) NOT NULL,
    nombre VARCHAR(30) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(20) NOT NULL,
    proveedor VARCHAR(20) NOT NULL,
    PRIMARY KEY(identificacion),
    FOREIGN KEY (proveedor) REFERENCES Proveedor(identificacion)
);

CREATE TABLE Producto (
    codigo VARCHAR(20) NOT NULL,
    nombre VARCHAR(30) NOT NULL,
    precio VARCHAR(20) NOT NULL,
    proveedor VARCHAR(20) NOT NULL,
    PRIMARY KEY(codigo),
    FOREIGN KEY (proveedor) REFERENCES Proveedor(identificacion)
);

CREATE TABLE Factura (
    numero VARCHAR(20) NOT NULL,
    fecha VARCHAR(10) NOT NULL,
    proveedor VARCHAR (20) NOT NULL,
    cliente VARCHAR(20) NOT NULL,
    PRIMARY KEY(numero),
    FOREIGN KEY (proveedor) REFERENCES Proveedor(identificacion),
    FOREIGN KEY (cliente) REFERENCES Cliente(identificacion)
);