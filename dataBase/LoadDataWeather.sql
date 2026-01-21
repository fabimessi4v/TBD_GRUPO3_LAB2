-- Poblamiento de tablas - Visualización Climática

-- 1 Usuarios
INSERT INTO Usuarios (Nombre, Email, ConHash, Rol)
VALUES
('Ana Torres', 'ana.torres@email.com', '$2b$12$fvw110ZS23tw/p5Oe/6P8ulnyVGxlUAzxQClK5fBu5S/8kSUvdfI2', 'admin'),
('Carlos Ruiz', 'carlos.ruiz@email.com', '$2b$12$QA/Sh1CWHcU4tnWJu/hN5OuMidM.yzK5V6zWSGAAg6itoSj0JQGd6', 'usuario'),
('Lucía Pérez', 'lucia.perez@email.com', '$2b$12$WdQeGYXB.p7f8RszKiXCOOkXPuMxQwHflWKBov3PHA/ktptx1hzKe', 'usuario');

-- 2 Datasets
INSERT INTO Datasets (Nombre, Descripcion, Fuente)
VALUES
('Temperatura', 'Registros de temperatura promedio', 'NOAA'),
('CO2', 'Concentración de dióxido de carbono', 'NASA'),
('NivelMar', 'Variación del nivel del mar', 'ESA');

-- 3 PuntosMedicion
INSERT INTO PuntosMedicion (Nombre, Latitud, Longitud, TipoSensor, Activo)
VALUES
('Estación Santiago Centro', -33.4489, -70.6693, 'Termómetro', TRUE),
('Estación Valparaíso', -33.0472, -71.6127, 'Sensor CO2', TRUE),
('Estación Antofagasta', -23.6500, -70.4000, 'Mareógrafo', TRUE),
('Estación Punta Arenas', -53.1638, -70.9171, 'Termómetro', TRUE),
('Estación Plaza de Armas', -33.45, -70.6667, 'Sensor CO2', TRUE),
('CO2 A', -34.650000, -58.400000, 'Sensor CO2', TRUE),
('CO2 B', -34.700000, -58.450000, 'Sensor CO2', TRUE),
('Temp BA', -34.603722, -58.381592, 'Termómetro', TRUE);

-- Dentro de "Zona Riesgo Incendio"
INSERT INTO PuntosMedicion (Nombre, Latitud, Longitud, TipoSensor, Activo, geom)
VALUES
(
  'Quebrada La Dormida',
  -33.0200,
  -70.9950,
  'Sensor CO2',
  TRUE,
  ST_SetSRID(ST_MakePoint(-70.9950, -33.0200), 4326)
),
(
  'Chacabuco',
  -33.0350,
  -70.9900,
  'Termómetro',
  TRUE,
  ST_SetSRID(ST_MakePoint(-70.9900, -33.0350), 4326)
);

-- Dentro de "Zona Deslizamiento"
INSERT INTO PuntosMedicion (Nombre, Latitud, Longitud, TipoSensor, Activo, geom)
VALUES
(
  'Ladera San Ramon',
  -33.5300,
  -70.7050,
  'Mareógrafo',
  TRUE,
  ST_SetSRID(ST_MakePoint(-70.7050, -33.5300), 4326)
),
(
  'Quebrada Macul',
  -33.5450,
  -70.7000,
  'Termómetro',
  TRUE,
  ST_SetSRID(ST_MakePoint(-70.7000, -33.5450), 4326)
);

-- Dentro de "Zona Aluvión"
INSERT INTO PuntosMedicion (Nombre, Latitud, Longitud, TipoSensor, Activo, geom)
VALUES
(
  'Cauce San Jose de Maipo',
  -33.6100,
  -70.7850,
  'Sensor CO2',
  TRUE,
  ST_SetSRID(ST_MakePoint(-70.7850, -33.6100), 4326)
),
(
  'Mareografo Bahia de Valparaiso',
  -33.6250,
  -70.7900,
  'Mareógrafo',
  TRUE,
  ST_SetSRID(ST_MakePoint(-70.7900, -33.6250), 4326)
);

-- 4 Mediciones

-- Dataset 1: Temperatura (°C)
INSERT INTO Mediciones (IdPunto, IdDataset, Valor, FechaHora)
VALUES
(1, 1, 22.5, '2025-01-03 14:00:00'),
(1, 1, 23.1, '2025-01-10 14:00:00'),
(1, 1, 25.0, '2025-01-17 14:00:00'),
(1, 1, 21.8, '2025-01-24 14:00:00'),
(1, 1, 24.1, '2025-02-10 14:00:00'),
(4, 1, 6.8,  '2025-01-03 14:00:00'),
(4, 1, 7.2,  '2025-01-10 14:00:00'),
(4, 1, 5.9,  '2025-01-17 14:00:00'),
(4, 1, 5.4,  '2025-01-24 14:00:00'),
(4, 1, 5.2,  '2025-02-10 14:00:00'),
(1, 1, 26.3, '2025-07-10 14:00:00'),
(1, 1, 27.0, '2025-07-24 14:00:00'),
(4, 1, 7.6,  '2025-07-10 14:00:00'),
(4, 1, 8.0,  '2025-07-24 14:00:00');

-- Dataset 2: CO2 (ppm)
INSERT INTO Mediciones (IdPunto, IdDataset, Valor, FechaHora)
VALUES
(2, 2, 417.3, '2025-01-03 10:00:00'),
(2, 2, 417.8, '2025-01-10 10:00:00'),
(2, 2, 418.4, '2025-01-17 10:00:00'),
(2, 2, 418.9, '2025-01-24 10:00:00'),
(2, 2, 419.5, '2025-02-10 10:00:00'),
(2, 2, 425.2, '2025-07-10 10:00:00'),
(2, 2, 426.0, '2025-07-24 10:00:00');

-- Dataset 3: Nivel del mar (metros)
INSERT INTO Mediciones (IdPunto, IdDataset, Valor, FechaHora)
VALUES
(3, 3, 0.10, '2025-01-03 09:00:00'),
(3, 3, 0.12, '2025-01-10 09:00:00'),
(3, 3, 0.14, '2025-01-17 09:00:00'),
(3, 3, 0.16, '2025-01-24 09:00:00'),
(3, 3, 0.18, '2025-02-15 09:00:00'),
(3, 3, 0.25, '2026-07-10 09:00:00'),
(3, 3, 0.27, '2026-07-24 09:00:00');

-- Mediciones
INSERT INTO Mediciones (IdPunto, IdDataset, Valor, FechaHora)
VALUES
(4, 1, 6.80, '2025-01-10 14:00:00'),
(4, 1, 5.20, '2025-02-10 14:00:00'),
(2, 2, 417.30, '2025-01-10 10:00:00'),
(2, 2, 419.50, '2025-02-10 10:00:00'),
(3, 3, 0.12, '2025-01-15 09:00:00'),
(3, 3, 0.18, '2025-02-15 09:00:00'),
(1, 1, 37.50, '2025-01-10 14:00:00'),
(1, 1, 38.10, '2025-02-10 14:00:00');

-- Mediciones
INSERT INTO Mediciones (IdPunto, IdDataset, Valor, FechaHora)
SELECT
  1,
  1,
  18 + random() * 10,
  d
FROM generate_series(
  '2020-01-01'::timestamp,
  CURRENT_DATE,
  INTERVAL '14 days'
) d;

INSERT INTO Mediciones (IdPunto, IdDataset, Valor, FechaHora)
SELECT
  4,
  1,
  18 + random() * 10,
  d
FROM generate_series(
  '2020-01-01'::timestamp,
  CURRENT_DATE,
  INTERVAL '14 days'
) d;

-- Mediciones
INSERT INTO PuntosMedicion (Nombre, Latitud, Longitud, TipoSensor, Activo)
VALUES
-- Sin coordenadas
('Estación Sin Coordenadas', NULL, NULL, 'Termómetro', TRUE),
-- Coordenadas inválidas (0,0)
('Estación Coordenadas Cero', 0, 0, 'Sensor CO2', TRUE),
-- Coordenada parcial inválida
('Estación Latitud Nula', NULL, -70.6500, 'Mareógrafo', TRUE);

INSERT INTO Mediciones (IdPunto, IdDataset, Valor, FechaHora)
VALUES
(5, 1, 19.8, '01-03-2025 12:00:00'),
(6, 2, 420.6, '02-03-2025 11:00:00'),
(7, 3, 0.21,  '03-03-2025 09:00:00');

-- Convertir puntos
UPDATE PuntosMedicion
SET geom = ST_SetSRID(
    ST_MakePoint(Longitud, Latitud),
    4326
)
WHERE Latitud IS NOT NULL
  AND Longitud IS NOT NULL;

-- Zonas de riesgo
INSERT INTO AreasAfectadas (Nombre, Descripcion, TipoRiesgo, geom)
VALUES
(
  'Zona Riesgo Santiago',
  'Zona de riesgo climático en Santiago Centro',
  'Sequía',
  ST_GeomFromText(
    'POLYGON((
      -70.75 -33.55,
      -70.55 -33.55,
      -70.55 -33.35,
      -70.75 -33.35,
      -70.75 -33.55
    ))',
    4326
  )
),
(
  'Zona Riesgo Incendio',
  'Área con alta vegetación seca',
  'Incendio',
  ST_GeomFromText(
    'POLYGON((
      -71.000 -33.000,
      -70.980 -33.010,
      -70.970 -33.030,
      -71.010 -33.040,
      -71.000 -33.000
    ))',
    4326
  )
),
(
  'Zona Deslizamiento',
  'Pendiente inestable en temporada de lluvias',
  'Deslizamiento',
  ST_GeomFromText(
    'POLYGON((
      -70.720 -33.520,
      -70.700 -33.500,
      -70.680 -33.530,
      -70.700 -33.550,
      -70.720 -33.520
    ))',
    4326
  )
),
(
  'Zona Aluvión',
  'Cauce seco con historial de aluviones',
  'Aluvión',
  ST_GeomFromText(
    'POLYGON((
      -70.800 -33.600,
      -70.780 -33.590,
      -70.770 -33.620,
      -70.790 -33.630,
      -70.800 -33.600
    ))',
    4326
  )
);

-- PUNTOS DE MEDICIÓN INVÁLIDOS

-- 1 Punto sin coordenadas ni geometría (geom = NULL)
INSERT INTO PuntosMedicion (Nombre, Latitud, Longitud, TipoSensor, Activo, geom)
VALUES (
  'Sensor Invalido Sin Geom',
  NULL,
  NULL,
  'Termómetro',
  TRUE,
  NULL
);

-- 2 Punto con coordenadas inválidas (0,0)
INSERT INTO PuntosMedicion (Nombre, Latitud, Longitud, TipoSensor, Activo, geom)
VALUES (
  'Sensor Invalido 0 0',
  0,
  0,
  'Sensor CO2',
  TRUE,
  ST_SetSRID(ST_MakePoint(0, 0), 4326)
);

-- 3 Punto con geometría inválida (SRID incorrecto)
INSERT INTO PuntosMedicion (Nombre, Latitud, Longitud, TipoSensor, Activo, geom)
VALUES (
  'Sensor Invalido SRID',
  -33.45,
  -70.66,
  'Mareógrafo',
  TRUE,
  ST_SetSRID(ST_MakePoint(-70.66, -33.45), 0) 
);
-- 2 interpolacion
-- caso con 3 valores
INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(1, 1, 22.10, '2026-01-21 08:00:00'),
(4, 1, 23.30, '2026-01-21 08:00:00'),
(5, 1, 21.80, '2026-01-21 08:00:00'),
(2, 1, NULL,  '2026-01-21 08:00:00');

INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(1, 2, 410.00, '2026-01-21 09:00:00'),
(4, 2, 415.00, '2026-01-21 09:00:00'),
(5, 2, 405.00, '2026-01-21 09:00:00'),
(2, 2, NULL,   '2026-01-21 09:00:00');

INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(1, 3, 0.25, '2026-01-21 10:00:00'),
(4, 3, 0.28, '2026-01-21 10:00:00'),
(5, 3, 0.26, '2026-01-21 10:00:00'),
(2, 3, NULL, '2026-01-21 10:00:00');

INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(1, 1, 20.50, '2026-01-21 11:00:00'),
(4, 1, 21.20, '2026-01-21 11:00:00'),
(5, 1, 20.90, '2026-01-21 11:00:00'),
(2, 1, NULL,  '2026-01-21 11:00:00');

INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(1, 2, 430.00, '2026-01-21 12:00:00'),
(4, 2, 428.00, '2026-01-21 12:00:00'),
(5, 2, 432.00, '2026-01-21 12:00:00'),
(2, 2, NULL,   '2026-01-21 12:00:00');

INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(1, 3, 0.31, '2026-01-21 13:00:00'),
(4, 3, 0.29, '2026-01-21 13:00:00'),
(5, 3, 0.30, '2026-01-21 13:00:00'),
(2, 3, NULL, '2026-01-21 13:00:00');

INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(1, 1, 24.10, '2026-01-21 14:00:00'),
(4, 1, 24.60, '2026-01-21 14:00:00'),
(5, 1, 23.90, '2026-01-21 14:00:00'),
(2, 1, NULL,  '2026-01-21 14:00:00');

INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(1, 2, 399.00, '2026-01-21 15:00:00'),
(4, 2, 402.00, '2026-01-21 15:00:00'),
(5, 2, 401.00, '2026-01-21 15:00:00'),
(2, 2, NULL,   '2026-01-21 15:00:00');

-- casos donde no debe interpolar
INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(1, 2, 421.00, '2026-01-24 00:00:00'); -- vecino único (no cuenta como caso null)

INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(4, 2, NULL,   '2026-01-24 00:00:00');

INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(1, 1, 24.00, '2026-01-25 00:00:00'),
(5, 1, 20.00, '2026-01-25 00:00:00');

INSERT INTO mediciones (idpunto, iddataset, valor, fechahora) VALUES
(2, 1, NULL,  '2026-01-25 00:00:00');
	
