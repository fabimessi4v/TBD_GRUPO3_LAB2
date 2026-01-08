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

