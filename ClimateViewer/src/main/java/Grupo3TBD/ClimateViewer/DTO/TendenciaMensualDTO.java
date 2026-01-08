package Grupo3TBD.ClimateViewer.DTO;

public class TendenciaMensualDTO {
    private String tipoSensor;
    private String nombreDataset;
    private String nombrePunto;
    private String mes;
    private Double promedioMensual;
    private Integer cantidadMediciones;

    public TendenciaMensualDTO() {}

    public TendenciaMensualDTO(String tipoSensor, String nombreDataset, String nombrePunto,
                               String mes, Double promedioMensual, Integer cantidadMediciones) {
        this.tipoSensor = tipoSensor;
        this.nombreDataset = nombreDataset;
        this.nombrePunto = nombrePunto;
        this.mes = mes;
        this.promedioMensual = promedioMensual;
        this.cantidadMediciones = cantidadMediciones;
    }


    public String getTipoSensor() {
        return tipoSensor;
    }


    public void setTipoSensor(String tipoSensor) {
        this.tipoSensor = tipoSensor;
    }


    public String getNombreDataset() {
        return nombreDataset;
    }


    public void setNombreDataset(String nombreDataset) {
        this.nombreDataset = nombreDataset;
    }


    public String getNombrePunto() {
        return nombrePunto;
    }


    public void setNombrePunto(String nombrePunto) {
        this.nombrePunto = nombrePunto;
    }


    public String getMes() {
        return mes;
    }


    public void setMes(String mes) {
        this.mes = mes;
    }


    public Double getPromedioMensual() {
        return promedioMensual;
    }


    public void setPromedioMensual(Double promedioMensual) {
        this.promedioMensual = promedioMensual;
    }


    public Integer getCantidadMediciones() {
        return cantidadMediciones;
    }


    public void setCantidadMediciones(Integer cantidadMediciones) {
        this.cantidadMediciones = cantidadMediciones;
    }
}


