package utn.saborcito.El_saborcito_back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import utn.saborcito.El_saborcito_back.models.Categoria;
import utn.saborcito.El_saborcito_back.services.CategoriaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/all")
    public ResponseEntity<List<Categoria>> getCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping("/id")
    public ResponseEntity<Categoria> getCategoriaById(Long id) {
        Categoria categoria = categoriaService.buscarCategoriaPorId(id);
        if (categoria == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro la categoria con ID: " + id);
        }
        return ResponseEntity.ok(categoria);
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> saveCategoria(@RequestBody Categoria categoria,BindingResult result) {
        if(result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.ok(categoriaService.guardarCategoria(categoria));
    }

    @PostMapping("/eliminar")
    public ResponseEntity<String> deleteCategoria(Long id) {
        if (categoriaService.buscarCategoriaPorId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro la categoria con ID: " + id);
        }
        categoriaService.eliminarCategoriaPorId(id);
        return ResponseEntity.ok("Categoria eliminada");
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(e -> {
            errors.put(e.getField(), e.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}