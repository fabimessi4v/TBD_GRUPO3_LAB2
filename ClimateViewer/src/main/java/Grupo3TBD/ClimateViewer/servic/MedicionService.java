package Grupo3TBD.ClimateViewer.servic;

import Grupo3TBD.ClimateViewer.DTO.MedicionDTO;
import Grupo3TBD.ClimateViewer.entities.Medicion;
import Grupo3TBD.ClimateViewer.repository.MedicionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MedicionService {
    @Autowired
    private MedicionRepository repository;
    // Listado paginado con filtro por idDataset
    public List<MedicionDTO> getMedicionesPaginadasDTO(int page, int size, Long idDataset) {
        List<Medicion> mediciones = repository.findAllPaged(page, size, idDataset);

        return mediciones.stream()
                .map(m -> new MedicionDTO(
                        m.getId(),
                        m.getPuntoMedicion() != null ? m.getPuntoMedicion().getId() : null,
                        m.getDataset() != null ? m.getDataset().getId() : null,
                        m.getValor(),
                        m.getFechahora()))
                .toList();
    }
    @Transactional
    public void crearNuevaMedicion(Medicion medicion) {
        // Validaciones básicas de ejemplo:
        if (medicion.getPuntoMedicion() == null || medicion.getPuntoMedicion().getId() == null) {
            throw new IllegalArgumentException("El Punto de medición es obligatorio");
        }
        if (medicion.getDataset() == null || medicion.getDataset().getId() == null) {
            throw new IllegalArgumentException("El Dataset es obligatorio");
        }
        if (medicion.getValor() == null) {
            throw new IllegalArgumentException("El valor es obligatorio");
        }
        if (medicion.getFechahora() == null) {
            throw new IllegalArgumentException("La fecha/hora es obligatoria");
        }
        repository.save(medicion);
    }
    @Transactional
    public void actualizarMedicion(Medicion medicion) {
        if (medicion.getId() == null) {
            throw new IllegalArgumentException("El ID de la medición es obligatorio");
        }
        repository.update(medicion);
    }

    @Transactional
    public void eliminarMedicion(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la medición no puede ser nulo");
        }
        repository.deleteById(id);
    }




}
