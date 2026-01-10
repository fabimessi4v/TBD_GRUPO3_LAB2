package Grupo3TBD.ClimateViewer.controllers;

import Grupo3TBD.ClimateViewer.DTO.DeteccionDeZonasRiesgoDTO;
import Grupo3TBD.ClimateViewer.repository.DeteccionDeZonasRiesgoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/deteccionzonasderiesgo")
public class DeteccionZonasDeRiesgoController {
    @Autowired
    private DeteccionDeZonasRiesgoRepository deteccionDeZonasRiesgoRepository;
    /**
     * Obtiene la lista de los puntos que se encuentran en zonas de riesgo climático.
     * @return Lista de objetos DeteccionDeZonasRiesgoDTO, cada uno representando un punto ubicado en una zona
     * de riesgo climático.
     */
    @GetMapping("/obtenerpunto")
    public List<DeteccionDeZonasRiesgoDTO> obtenerPuntosEnZonasRiesgo() {
        return deteccionDeZonasRiesgoRepository.obtenerPuntosEnZonasRiesgo();
    }
}
