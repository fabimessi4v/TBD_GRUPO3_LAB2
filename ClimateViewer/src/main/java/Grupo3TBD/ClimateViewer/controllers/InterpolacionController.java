package Grupo3TBD.ClimateViewer.controllers;

import Grupo3TBD.ClimateViewer.repository.InterpolarizacionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/consultas")
public class InterpolacionController {

    private final InterpolarizacionRepository repo;

    public InterpolacionController(InterpolarizacionRepository repo) {
        this.repo = repo;
    }

    // lista mediciones nulas
    @GetMapping("/puntos")
    public ResponseEntity<?> listarPuntos() {
        return ResponseEntity.ok(Map.of(
                "data", repo.listarNulasParaFront()
        ));
    }

    // refresca vista
    @PostMapping("/interpolacion/aplicar")
    public ResponseEntity<?> aplicarInterpolacion() {
        repo.aplicarInterpolacion();
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "mensaje", "Interpolación aplicada (MV refrescada)."
        ));
    }

    // limpia vista
    @PostMapping("/interpolacion/limpiar")
    public ResponseEntity<?> limpiarInterpolacion() {
        repo.limpiarInterpolacion();
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "mensaje", "Interpolación limpiada (MV refrescada)."
        ));
    }
}