package Grupo3TBD.ClimateViewer.controllers;

import Grupo3TBD.ClimateViewer.DTO.CorrelacionDTO;
import Grupo3TBD.ClimateViewer.DTO.PuntoMedicionDTO;
import Grupo3TBD.ClimateViewer.DTO.PuntoUltimaMedicionDTO;
import Grupo3TBD.ClimateViewer.entities.PuntoMedicion;
import Grupo3TBD.ClimateViewer.repository.PuntoMedicionRepository;
import Grupo3TBD.ClimateViewer.servic.PuntoMedicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puntos")
public class PuntoMedicionController {

    @Autowired
    private PuntoMedicionRepository puntoMedicionRepository;
    @Autowired
    private PuntoMedicionService puntoMedicionService;






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



    // ----------- CRUD -----------

    // CREATE
    @PostMapping
    public void crearPunto(@RequestBody PuntoMedicionDTO puntoDTO) {
        PuntoMedicion entidad = dtoToEntidad(puntoDTO);
        puntoMedicionService.crearNuevoPunto(entidad);
    }

    // READ paginado por nombre
    @GetMapping
    public List<PuntoMedicionDTO> getPagedPuntos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String nombre
    ) {
        return puntoMedicionService.getPuntosPaginadosDTO(page, size, nombre);
    }

    // UPDATE
    @PutMapping("/{id}")
    public void actualizarPunto(@PathVariable Long id, @RequestBody PuntoMedicionDTO puntoDTO) {
        PuntoMedicion entidad = dtoToEntidad(puntoDTO);
        entidad.setId(id);
        puntoMedicionService.actualizarPunto(entidad);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void eliminarPunto(@PathVariable Long id) {
        puntoMedicionService.eliminarPunto(id);
    }

    // Utilidad para convertir DTO a entidad
    private PuntoMedicion dtoToEntidad(PuntoMedicionDTO dto) {
        PuntoMedicion p = new PuntoMedicion();
        p.setId(dto.getId());
        p.setNombre(dto.getNombre());
        p.setLatitud(dto.getLatitud());
        p.setLongitud(dto.getLongitud());
        p.setTipoSensor(dto.getTipoSensor());
        p.setActivo(dto.isActivo());
        p.setGeom(dto.getGeom());
        return p;
    }


}