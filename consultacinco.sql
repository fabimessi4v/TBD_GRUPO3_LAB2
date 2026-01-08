/* Simulación de Interpolación de Datos: Escribe un procedimiento almacenado llamado interpolar_datos_semanales
 que reciba un ID de dataset. El procedimiento debe calcular el promedio semanal de las mediciones y almacenarlo
 en una tabla de resumen, llenando los días sin datos con el promedio semanal.*/


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

  RAISE NOTICE 'Promedios semanales actualizados para dataset %.', p_dataset_id;
END;
$$;
