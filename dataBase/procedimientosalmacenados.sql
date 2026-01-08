-- 5. Interpolación datos semanales

-- Crear la tabla de resumen
CREATE TABLE IF NOT EXISTS resumen_semanal (
  iddataset INTEGER REFERENCES Datasets(IdDataset),
  semana DATE,
  promedio NUMERIC,
  PRIMARY KEY (iddataset, semana)
);

CREATE OR REPLACE PROCEDURE interpolar_datos_semanales(p_dataset_id INTEGER)
LANGUAGE plpgsql
AS $$
BEGIN
  -- Borrar datos anteriores del dataset recibido
  DELETE FROM resumen_semanal
  WHERE iddataset = p_dataset_id;

  -- Insertar los nuevos promedios semanales
  INSERT INTO resumen_semanal (iddataset, semana, promedio)
  SELECT
    m.IdDataset,
    DATE_TRUNC('week', m.fechahora)::date AS semana,
    AVG(m.Valor) AS promedio
  FROM Mediciones m
  WHERE m.Iddataset = p_dataset_id
  GROUP BY m.Iddataset, DATE_TRUNC('week', m.fechahora)::date;
END;
$$;
-- Fin consulta 5

-- Consulta 6. Serie temporal

CREATE OR REPLACE PROCEDURE agregacion_de_datos(
  IN a_id_dataset INTEGER,
  IN rango_fecha_comienzo DATE,
  IN rango_fecha_fin DATE
)
LANGUAGE plpgsql
AS $$
DECLARE
  dias INTEGER;
  sql_query TEXT;
BEGIN
  dias := rango_fecha_fin - rango_fecha_comienzo;

  EXECUTE 'DROP MATERIALIZED VIEW IF EXISTS vista_serie_temporal';

  IF dias <= 31 THEN
    sql_query := format(
      $SQL$
      CREATE MATERIALIZED VIEW vista_serie_temporal AS
      SELECT DISTINCT ON (date_trunc('week', m.fechahora))
         m.iddataset,
         date_trunc('week', m.fechahora)::date AS periodo,
         m.valor
      FROM mediciones m
      WHERE m.iddataset = %s
        AND m.fechahora::date BETWEEN %L AND %L
      ORDER BY date_trunc('week', m.fechahora), m.fechahora DESC;
      $SQL$,
      a_id_dataset, rango_fecha_comienzo, rango_fecha_fin
    );
  ELSE
    sql_query := format(
      $SQL$
      CREATE MATERIALIZED VIEW vista_serie_temporal AS
      SELECT DISTINCT ON (date_trunc('month', m.fechahora))
         m.iddataset,
         date_trunc('month', m.fechahora)::date AS periodo,
         m.valor
      FROM mediciones m
      WHERE m.iddataset = %s
        AND m.fechahora::date BETWEEN %L AND %L
      ORDER BY date_trunc('month', m.fechahora), m.fechahora DESC;
      $SQL$,
      a_id_dataset, rango_fecha_comienzo, rango_fecha_fin
    );
  END IF;

  EXECUTE sql_query;
END;
$$;
-- Fin consulta 6

-- Consulta 7. Medidas sin georreferenciación
SELECT 
    p.IdPunto,
    p.Nombre,
    MAX(m.FechaHora) AS UltimaMedicion
FROM PuntosMedicion p
LEFT JOIN Mediciones m ON p.IdPunto = m.IdPunto
WHERE 
    p.Latitud IS NULL
    OR p.Longitud IS NULL
    OR p.Latitud = 0
    OR p.Longitud = 0
GROUP BY 
    p.IdPunto, p.Nombre
ORDER BY 
    UltimaMedicion DESC NULLS LAST;
-- Fin consulta 7

	
-- Consulta 8. vista materializada de tendencia histórica mensual
-- vista que hace cálculo de promedios
CREATE MATERIALIZED VIEW v_promedios_mensual AS
WITH base AS (
    SELECT
        m.idpunto,
        m.iddataset,
        date_trunc('month', m.fechahora) AS mes,
        m.valor
    FROM mediciones m
)
SELECT
    pm.tiposensor           AS tipo_sensor,
    d.nombre                AS nombre_dataset,
    pm.nombre               AS nombre_punto,
    b.idpunto,
    b.iddataset,
    b.mes,
    ROUND(AVG(b.valor), 2)  AS promedio_mensual,
    COUNT(*)                AS cantidad_mediciones
FROM base b
JOIN puntosmedicion pm ON pm.idpunto = b.idpunto
JOIN datasets d        ON d.iddataset = b.iddataset
GROUP BY
    pm.tiposensor, d.nombre, pm.nombre,
    b.idpunto, b.iddataset, b.mes;
-- índice que actualice promedios
CREATE UNIQUE INDEX IF NOT EXISTS v_promedios_mensual_in
ON v_promedios_mensual (idpunto, iddataset, mes);
-- segunda vista que almacena cálculo de promedios
CREATE MATERIALIZED VIEW tendencia_mensual AS
SELECT
    tipo_sensor,
    nombre_dataset,
    nombre_punto,
    mes,
    ROUND(
        SUM(promedio_mensual * cantidad_mediciones)
        / NULLIF(SUM(cantidad_mediciones), 0),
        2
    ) AS promedio_mensual,
    SUM(cantidad_mediciones) AS cantidad_mediciones
FROM v_promedios_mensual
GROUP BY
    tipo_sensor, nombre_dataset, nombre_punto, mes;
-- indices para el análisis de tendencia
CREATE UNIQUE INDEX IF NOT EXISTS tendencia_mensual_in
ON tendencia_mensual (tipo_sensor, nombre_dataset, nombre_punto, mes);

-- REFRESH MATERIALIZED VIEW v_promedios_mensual;
-- REFRESH MATERIALIZED VIEW CONCURRENTLY tendencia_mensual;
-- Fin consulta 8
