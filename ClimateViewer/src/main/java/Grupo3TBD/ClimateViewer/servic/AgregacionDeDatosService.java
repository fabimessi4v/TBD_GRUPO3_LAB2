package Grupo3TBD.ClimateViewer.servic;

import Grupo3TBD.ClimateViewer.DTO.AgregacionDeDatosDTO;
import Grupo3TBD.ClimateViewer.repository.AgregacionDeDatosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgregacionDeDatosService {

    @Autowired
    AgregacionDeDatosRepository agregacionDeDatosRepository;
    /*public List<AgregacionDeDatosDTO> obtenerSerieTemporal(Integer idDataset, LocalDate fechaComienzo, LocalDate fechaTermino){
        return agregacionDeDatosRepository.obtenerSerieTemporal(idDataset, fechaComienzo, fechaTermino);
    }*/

    public List<AgregacionDeDatosDTO> obtenerSerieTemporal(Integer idDataset, LocalDate fechaComienzo, LocalDate fechaTermino) {
        agregacionDeDatosRepository.llamarProcedimiento(idDataset, fechaComienzo, fechaTermino);
        return agregacionDeDatosRepository.obtenerSerieTemporal();
    }
}
