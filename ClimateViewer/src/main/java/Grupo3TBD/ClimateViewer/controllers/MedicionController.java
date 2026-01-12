package Grupo3TBD.ClimateViewer.controllers;

import Grupo3TBD.ClimateViewer.DTO.*;
import Grupo3TBD.ClimateViewer.entities.Medicion;
import Grupo3TBD.ClimateViewer.repository.MedicionRepository;
import Grupo3TBD.ClimateViewer.servic.MedicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mediciones")
public class MedicionController {

    @Autowired
    private MedicionRepository medicionRepository;
    @Autowired
    private MedicionService medicionService;
    /**
     * Obtiene la lista de eventos extremos de temperatura registrados en el último año.
     *
     * Un evento extremo se define como un día en el que la temperatura máxima registrada
     * por algún punto de medición (de tipo "Termómetro") fue mayor a 35°C.
     *
     * @return Lista de objetos EventoExtremoDTO con fecha y máxima temperatura de cada evento.
     */
    @GetMapping("/eventos-extremos-temperatura")
    public List<EventoExtremoDTO> getEventosExtremosTemperaturaUltimoAno() {
        return medicionRepository.findEventosExtremosTemperaturaUltimoAno();
    }

    @GetMapping("/tendencia-mensual")
    public List<TendenciaMensualDTO> obtenerTendenciaMensual(
            @RequestParam(required = false) String dataset,
            @RequestParam(required = false) String sensor,
            @RequestParam(required = false) String estacion) {
        return medicionRepository.obtenerTendenciaMensual(dataset, sensor, estacion);
    }

    @GetMapping("/anomalias")
    public List<AnomaliaTemperaturaDTO> anomalias() {
        return medicionRepository.findAnomaliaTemperaturaPorPunto();
    }

    @GetMapping("/top-variacion")
    public List<VariacionTemperaturaDTO> topVariacion() {
        return medicionRepository.findTop10MayorVariacionTemperatura5Anios();
    }

    // --- CREATE ---
    @PostMapping
    public void crearMedicion(@RequestBody Medicion medicion) {
        medicionService.crearNuevaMedicion(medicion);
    }

    // --- READ paginado DTO ---
    @GetMapping
    public List<MedicionDTO> getPagedMedicionesDTO(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long idDataset
    ) {
        return medicionService.getMedicionesPaginadasDTO(page, size, idDataset);
    }
    // --- UPDATE ---
    @PutMapping("/{id}")
    public void actualizarMedicion(@PathVariable Long id, @RequestBody Medicion medicion) {
        medicion.setId(id);
        medicionService.actualizarMedicion(medicion);
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public void eliminarMedicion(@PathVariable Long id) {
        medicionService.eliminarMedicion(id);
    }











}