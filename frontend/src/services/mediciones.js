import apiClient from '@/services/axios'

export const getAnomaliasTemperatura = () =>
  apiClient.get("/mediciones/anomalias");

export const getTopVariacionTemperatura = () =>
  apiClient.get("/mediciones/top-variacion");
