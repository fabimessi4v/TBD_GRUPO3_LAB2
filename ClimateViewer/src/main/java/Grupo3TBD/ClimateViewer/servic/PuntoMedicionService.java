package Grupo3TBD.ClimateViewer.servic;

import Grupo3TBD.ClimateViewer.DTO.PuntoMedicionDTO;
import Grupo3TBD.ClimateViewer.entities.PuntoMedicion;
import Grupo3TBD.ClimateViewer.repository.PuntoMedicionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuntoMedicionService {

    @Autowired
    private PuntoMedicionRepository repository;

    // --- Listado paginado con filtro por nombre, retorna DTO ---
    public List<PuntoMedicionDTO> getPuntosPaginadosDTO(int page, int size, String nombreFiltro) {
        List<PuntoMedicion> puntos = repository.findAllPaged(page, size, nombreFiltro);
        return puntos.stream()
                .map(p -> new PuntoMedicionDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getLatitud(),
                        p.getLongitud(),
                        p.getTipoSensor(),
                        p.isActivo(),
                        p.getGeom()  // WKT
                ))
                .toList();
    }

    @Transactional
    public void crearNuevoPunto(PuntoMedicion punto) {
        // Validaciones mínimas
        if (punto.getNombre() == null || punto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (Double.isNaN(punto.getLatitud()) || Double.isNaN(punto.getLongitud())) {
            throw new IllegalArgumentException("Las coordenadas son obligatorias");
        }
        if (punto.getTipoSensor() == null || punto.getTipoSensor().isEmpty()) {
            throw new IllegalArgumentException("El tipo de sensor es obligatorio");
        }
        repository.save(punto);
    }

    @Transactional
    public void actualizarPunto(PuntoMedicion punto) {
        if (punto.getId() == null) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }
        repository.update(punto);
    }

    @Transactional
    public void eliminarPunto(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        repository.deleteById(id);
    }

}
