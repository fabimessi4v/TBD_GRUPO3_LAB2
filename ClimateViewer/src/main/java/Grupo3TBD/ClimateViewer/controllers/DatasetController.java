package Grupo3TBD.ClimateViewer.controllers;


import Grupo3TBD.ClimateViewer.DTO.DatasetDTO;
import Grupo3TBD.ClimateViewer.entities.Dataset;
import Grupo3TBD.ClimateViewer.servic.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/datasets") // endpoint base
public class DatasetController {
    @Autowired
    private DatasetService datasetService;

    // 1. Listado paginado con filtro por nombre
    @GetMapping
    public List<DatasetDTO> getPagedDatasets(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(required=false) String nombre
    ) {
        return datasetService.getDatasetsPaginados(page, size, nombre);
    }

    // 2. Crear nuevo dataset
    @PostMapping
    public void crearDataset(@RequestBody Dataset dataset) {
        datasetService.crearNuevoDataset(dataset);
    }

    // 3. Actualizar un dataset
    @PutMapping("/{id}")
    public void actualizarDataset(@PathVariable Long id, @RequestBody Dataset dataset) {
        dataset.setId(id);
        datasetService.ActualizarDataset(dataset);
    }

    // 4. Eliminar un dataset
    @DeleteMapping("/{id}")
    public void eliminarDataset(@PathVariable Long id) {
        datasetService.EliminarDataset(id);
    }




}
