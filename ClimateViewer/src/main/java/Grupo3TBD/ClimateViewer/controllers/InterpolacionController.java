package Grupo3TBD.ClimateViewer.controllers;

import Grupo3TBD.ClimateViewer.repository.InterpolarizacionRepository;
import Grupo3TBD.ClimateViewer.repository.InterpolarizacionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/consultas")
public class InterpolacionController {

    private final InterpolarizacionRepository consulta2Repository;

    public InterpolacionController(InterpolarizacionRepository consulta2Repository) {
        this.consulta2Repository = consulta2Repository;
    }

    //formato para el front
    @GetMapping("/puntos")
    public ResponseEntity<?> listarPuntos() {
        return ResponseEntity.ok(Map.of(
                "data", consulta2Repository.listarTablaSimpleAll()
        ));
    }

    //interpola los datos
    @PostMapping("/interpolacion/aplicar")
    public ResponseEntity<?> aplicarInterpolacion() {
        consulta2Repository.aplicarInterpolacion();
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "mensaje", "Interpolación aplicada (vista materializada refrescada)."
        ));
    }

    //actualiza la vista al estado anterior
    @PostMapping("/interpolacion/limpiar")
    public ResponseEntity<?> limpiarInterpolacion() {
        consulta2Repository.limpiarInterpolacion();
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "mensaje", "Interpolación limpiada (vista materializada refrescada)."
        ));
    }
}