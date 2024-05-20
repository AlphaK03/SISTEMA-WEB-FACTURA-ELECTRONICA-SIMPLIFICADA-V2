CREATE DATABASE IF NOT EXISTS BD;
USE BD;

CREATE TABLE Usuario (
    identificacion VARCHAR(20) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(10) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true, -- Nuevo atributo booleano para indicar si el usuario está activo
    PRIMARY KEY(identificacion)
);


CREATE TABLE Proveedor (
    identificacion VARCHAR(20) NOT NULL,
    nombre VARCHAR(30) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(20) NOT NULL,
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
    codigo int NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    precio VARCHAR(20) NOT NULL,
    proveedor VARCHAR(20) NOT NULL,
    PRIMARY KEY(codigo),
    FOREIGN KEY (proveedor) REFERENCES Proveedor(identificacion)
);

CREATE TABLE Factura (
    numero int NOT NULL AUTO_INCREMENT,
    fecha VARCHAR(10) NOT NULL,
    proveedor VARCHAR (20) NOT NULL,
    cliente VARCHAR(20) NOT NULL,
    PRIMARY KEY(numero),
    FOREIGN KEY (proveedor) REFERENCES Proveedor(identificacion),
    FOREIGN KEY (cliente) REFERENCES Cliente(identificacion)
);
CREATE TABLE Detalle (
	numero int NOT NULL AUTO_INCREMENT,
	numerofactura int NOT NULL,
	codigoproducto int NOT NULL,
	descripcion varchar (70) NOT NULL,
    cantidad int NOT NULL,
	PRIMARY KEY (numero),
	FOREIGN KEY (numerofactura) REFERENCES Factura (numero),
	FOREIGN KEY (codigoproducto) REFERENCES Producto (codigo)
	
)