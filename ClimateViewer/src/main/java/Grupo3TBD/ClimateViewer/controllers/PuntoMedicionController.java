package Grupo3TBD.ClimateViewer.controllers;

import Grupo3TBD.ClimateViewer.DTO.CorrelacionDTO;
import Grupo3TBD.ClimateViewer.DTO.PuntoUltimaMedicionDTO;
import Grupo3TBD.ClimateViewer.repository.PuntoMedicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/puntos")
public class PuntoMedicionController {

    @Autowired
    private PuntoMedicionRepository puntoMedicionRepository;
    /**
     * Obtiene la lista de sensores de CO2 que están a menos de 50 km de un punto de medición de temperatura dado.
     *
     * @param idPuntoTemperatura ID del punto de medición de temperatura.
     * @return Lista de objetos CorrelacionDTO con información de los puntos y la distancia entre ellos.
     */
    @GetMapping("/correlacion")
    public List<CorrelacionDTO> getCorrelacion(@RequestParam Long idPuntoTemperatura) {
        return puntoMedicionRepository.findCO2CercanosPorPuntoTemperatura(idPuntoTemperatura);
    }

    @GetMapping("/georreferencia")
    public List<PuntoUltimaMedicionDTO> listarPuntosSinGeorreferencia() {
        return puntoMedicionRepository.findPuntosSinGeorreferenciacion();
    }
}