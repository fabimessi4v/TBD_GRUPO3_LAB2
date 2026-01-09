-- Creación de tablas - visualización climatica
-- Para habilitar PostGIS:
CREATE EXTENSION IF NOT EXISTS postgis;

-- 1. Usuarios

Create table Usuarios (
	IdUsuario SERIAL PRIMARY KEY,
	Nombre VARCHAR(50) NOT NULL,
	Email VARCHAR(100) UNIQUE NOT NULL,
	ConHash VARCHAR(100) NOT NULL,
	Rol VARCHAR(12) CHECK (rol IN ('usuario','admin')) DEFAULT 'usuario',
	FechaRegistro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	
);

-- 2. Datasets
Create table Datasets (
	IdDataset SERIAL PRIMARY KEY,
	Nombre VARCHAR(50) NOT NULL,
	Descripcion VARCHAR(150),
	Fuente VARCHAR(100),
	FechaActualizacion DATE DEFAULT CURRENT_DATE
	
);

-- 3. PuntosMedicion
Create table PuntosMedicion (
	IdPunto SERIAL PRIMARY KEY,
	Nombre VARCHAR(50),
	Latitud DECIMAL (10,6),
	Longitud DECIMAL (10,6),
	TipoSensor VARCHAR(12),
	Activo BOOLEAN DEFAULT TRUE,
	geom geometry(Point, 4326)
	
);

-- 4. Mediciones
Create table Mediciones (
	IdMedicion SERIAL PRIMARY KEY,
	IdPunto INT NOT NULL,
	IdDataset INT NOT NULL,
	Valor NUMERIC(12,2),
	FechaHora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (IdPunto) REFERENCES PuntosMedicion(IdPunto),
	FOREIGN KEY (IdDataset) REFERENCES Datasets(IdDataset)

);

-- 5. AreasAfectadas
CREATE TABLE AreasAfectadas (
    IdArea SERIAL PRIMARY KEY,
    Nombre VARCHAR(50),
    Descripcion VARCHAR(150),
    TipoRiesgo VARCHAR(30),
    geom geometry(Polygon, 4326)
);
	
	
	
