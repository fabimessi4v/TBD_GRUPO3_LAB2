package Grupo3TBD.ClimateViewer.controllers;


import Grupo3TBD.ClimateViewer.DTO.AreasAfectadasDTO;
import Grupo3TBD.ClimateViewer.entities.AreasAfectadas;
import Grupo3TBD.ClimateViewer.servic.AreasAfectadasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreasAfectadasController {

    @Autowired
    private AreasAfectadasService areasAfectadasService;

    // CREATE
    @PostMapping
    public void crearArea(@RequestBody AreasAfectadasDTO areaDTO) {
        AreasAfectadas entidad = dtoToEntidad(areaDTO);
        areasAfectadasService.crearNuevaArea(entidad);
    }

    // READ paginado por nombre
    @GetMapping
    public List<AreasAfectadasDTO> getPagedAreas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String nombre
    ) {
        return areasAfectadasService.getAreasPaginadasDTO(page, size, nombre);
    }

    // UPDATE
    @PutMapping("/{id}")
    public void actualizarArea(@PathVariable Long id, @RequestBody AreasAfectadasDTO areaDTO) {
        AreasAfectadas entidad = dtoToEntidad(areaDTO);
        entidad.setId(id);
        areasAfectadasService.actualizarArea(entidad);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void eliminarArea(@PathVariable Long id) {
        areasAfectadasService.eliminarArea(id);
    }

    // Utilidad para convertir DTO a entidad
    private AreasAfectadas dtoToEntidad(AreasAfectadasDTO dto) {
        AreasAfectadas a = new AreasAfectadas();
        a.setId(dto.getId());
        a.setNombre(dto.getNombre());
        a.setDescripcion(dto.getDescripcion());
        a.setTipoRiesgo(dto.getTipoRiesgo());
        a.setGeom(dto.getGeom());
        return a;
    }
}