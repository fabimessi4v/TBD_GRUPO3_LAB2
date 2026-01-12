package Grupo3TBD.ClimateViewer.servic;

import Grupo3TBD.ClimateViewer.DTO.DatasetDTO;
import Grupo3TBD.ClimateViewer.entities.Dataset;
import Grupo3TBD.ClimateViewer.repository.DatasetRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatasetService {
    @Autowired
    private DatasetRepository repository;




    public List<DatasetDTO> getDatasetsPaginados(int page, int size, String nombre) {
        List<Dataset> datasets = repository.findAllPaged(page, size, nombre);

        // 2. Se convierte la lista de Entidades a DTOs
        return datasets.stream()
                .map(d -> new DatasetDTO(
                        d.getId(),
                        d.getNombre(),
                        d.getDescripcion(),
                        d.getFuente(),
                        d.getFechaActualizacion()))
                .toList();

    }
    @Transactional
    public void crearNuevoDataset(Dataset dataset) {
        // Lógica: Validar que el nombre no sea nulo antes de ir a la BD
        if (dataset.getNombre() == null || dataset.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        repository.save(dataset);
    }
    @Transactional
    public void ActualizarDataset(Dataset dataset) {
        repository.update(dataset);
    }
    @Transactional
    public void EliminarDataset(Long id) {
        //  validar que el ID no sea nulo
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        repository.deleteById(id);
    }



}

