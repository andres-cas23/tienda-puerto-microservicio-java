package com.tiendapuerto.microservicio_java;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "CRUD de productos - Microservicio Java (Spring Boot)")
public class ProductoController {

    @Autowired
    private SupabaseService supabaseService;

    @Operation(summary = "Lista todos los productos")
    @GetMapping({"", "/"})
    public List<Producto> listar() {
        return supabaseService.listarTodos();
    }

    @Operation(summary = "Obtiene un producto por id")
    @GetMapping({"/{id}", "/{id}/"})
    public ResponseEntity<?> obtener(@PathVariable Integer id) {
        Producto producto = supabaseService.obtenerPorId(id);
        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("detail", "Producto no encontrado"));
        }
        return ResponseEntity.ok(producto);
    }

    @Operation(summary = "Crea un nuevo producto")
    @PostMapping({"", "/"})
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto creado = supabaseService.crear(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualiza un producto existente")
    @PutMapping({"/{id}", "/{id}/"})
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        Producto actualizado = supabaseService.actualizar(id, producto);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("detail", "Producto no encontrado"));
        }
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Elimina un producto")
    @DeleteMapping({"/{id}", "/{id}/"})
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        boolean eliminado = supabaseService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("detail", "Producto no encontrado"));
        }
        return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente"));
    }
}