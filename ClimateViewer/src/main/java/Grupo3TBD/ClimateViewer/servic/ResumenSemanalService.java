package Grupo3TBD.ClimateViewer.servic;

import Grupo3TBD.ClimateViewer.DTO.ResumenSemanalDTO;
import Grupo3TBD.ClimateViewer.repository.ResumenSemanalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumenSemanalService {

    @Autowired ResumenSemanalRepository resumenSemanalRepository;
    public void recalcularResumenSemanal(Integer idDataset) {
        resumenSemanalRepository.recalcularResumenSemanal(idDataset);
    }

    public List<ResumenSemanalDTO> recalcularResumenSemanalyObtener(Integer idDataset) {
        resumenSemanalRepository.recalcularResumenSemanal(idDataset);
        return resumenSemanalRepository.obtenerResumenEspecifico(idDataset);
    }

    public List<ResumenSemanalDTO> obtenerResumenSemanal() {
        return resumenSemanalRepository.obtenerResumen();
    }

    public List<ResumenSemanalDTO> obtenerResumenEspecifico(Integer idDataset) {
        return resumenSemanalRepository.obtenerResumenEspecifico(idDataset);
    }

}
