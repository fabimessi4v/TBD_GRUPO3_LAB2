-- PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

-- tabla para limpiar interpolacion
CREATE TABLE IF NOT EXISTS app_flags (
  id INT PRIMARY KEY DEFAULT 1,
  interpolacion_activa BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT app_flags_single_row CHECK (id = 1)
);

INSERT INTO app_flags (id, interpolacion_activa)
VALUES (1, FALSE)
ON CONFLICT (id) DO NOTHING;

-- creacion de vista materializada
CREATE MATERIALIZED VIEW mv_mediciones_nulas_interp AS
WITH flag AS (
  SELECT interpolacion_activa
  FROM app_flags
  WHERE id = 1
)
SELECT
  m.iddataset,
  m.idpunto,
  m.fechahora,
  pm.nombre AS nombre_punto,
  d.nombre AS sensor,
  pm.activo,
  ST_X(pm.geom) AS longitud,
  ST_Y(pm.geom) AS latitud,
  m.valor AS valor_real,

  CASE
    WHEN (SELECT interpolacion_activa FROM flag) = FALSE THEN NULL
    WHEN nn.cnt < 3 THEN NULL
    ELSE nn.avg_val
  END AS valor_estimado,

  TRUNC(
    CASE
      WHEN (SELECT interpolacion_activa FROM flag) = FALSE THEN NULL
      WHEN nn.cnt < 3 THEN NULL
      ELSE nn.avg_val
    END::numeric, 2
  ) AS valor_final,

  (
    (SELECT interpolacion_activa FROM flag) = TRUE
    AND nn.cnt = 3
  ) AS es_interpolado

FROM mediciones m
JOIN puntosmedicion pm
  ON pm.idpunto = m.idpunto
JOIN datasets d
  ON d.iddataset = m.iddataset

LEFT JOIN LATERAL (
  SELECT
    COUNT(*) AS cnt,
    AVG(x.valor_vecino) AS avg_val
  FROM (
    SELECT m2.valor AS valor_vecino
    FROM mediciones m2
    JOIN puntosmedicion pm2 ON pm2.idpunto = m2.idpunto
    WHERE m2.iddataset = m.iddataset
      AND m2.fechahora = m.fechahora
      AND m2.valor IS NOT NULL
      AND pm2.activo = TRUE
      AND pm2.geom IS NOT NULL
      AND pm2.idpunto <> pm.idpunto
    ORDER BY pm2.geom <-> pm.geom
    LIMIT 3
  ) x
) nn ON TRUE

WHERE
  m.valor IS NULL
  AND pm.activo = TRUE
  AND pm.geom IS NOT NULL
  AND NOT (ST_X(pm.geom)=0 AND ST_Y(pm.geom)=0);

-- creacion de indices
CREATE UNIQUE INDEX IF NOT EXISTS mv_mediciones_nulas_interp_pk
ON mv_mediciones_nulas_interp (iddataset, idpunto, fechahora);

CREATE INDEX IF NOT EXISTS puntosmedicion_geom_gix
ON puntosmedicion USING GIST (geom);

CREATE INDEX IF NOT EXISTS mediciones_ds_fecha
ON mediciones (iddataset, fechahora);

-- refresh a la vista
REFRESH MATERIALIZED VIEW mv_mediciones_nulas_interp;
