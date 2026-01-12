package Grupo3TBD.ClimateViewer.servic;

import Grupo3TBD.ClimateViewer.DTO.AreasAfectadasDTO;
import Grupo3TBD.ClimateViewer.entities.AreasAfectadas;
import Grupo3TBD.ClimateViewer.repository.AreasAfectadasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AreasAfectadasService {
    @Autowired
    private AreasAfectadasRepository repository;

    // Listado paginado con filtro por nombre, retorna DTO
    public List<AreasAfectadasDTO> getAreasPaginadasDTO(int page, int size, String nombreFiltro) {
        List<AreasAfectadas> areas = repository.findAllPaged(page, size, nombreFiltro);
        return areas.stream()
                .map(a -> new AreasAfectadasDTO(
                        a.getId(),
                        a.getNombre(),
                        a.getDescripcion(),
                        a.getTipoRiesgo(),
                        a.getGeom()
                ))
                .toList();
    }

    @Transactional
    public void crearNuevaArea(AreasAfectadas area) {
        // Validaciones básicas
        if (area.getNombre() == null || area.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (area.getGeom() == null || area.getGeom().isEmpty()) {
            throw new IllegalArgumentException("El polígono geométrico es obligatorio");
        }
        repository.save(area);
    }

    @Transactional
    public void actualizarArea(AreasAfectadas area) {
        if (area.getId() == null) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }
        repository.update(area);
    }

    @Transactional
    public void eliminarArea(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        repository.deleteById(id);
    }
}
