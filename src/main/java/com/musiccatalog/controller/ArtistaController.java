package com.musiccatalog.controller;

import com.musiccatalog.dto.ArtistaResponse;
import com.musiccatalog.enums.TipoArtista;
import com.musiccatalog.model.Artista;
import com.musiccatalog.service.ArtistaService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.musiccatalog.utils.PageableUtils.toPageable;

@Validated
@RestController
@RequestMapping("/api/v1/artistas")
public class ArtistaController {
    private final ArtistaService service;

    public ArtistaController(ArtistaService service) {this.service = service;}

    @GetMapping
    public Page<ArtistaResponse> filtrar(@RequestParam(required = false) String nome,
                                         @RequestParam(required = false) TipoArtista tipo,
                                         @RequestParam(defaultValue = "nome,asc") String sort,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {

        return service.filtrar(nome, tipo, toPageable(page, size, sort));
    }

    @PostMapping
    public ResponseEntity<ArtistaResponse> criar(@RequestBody Artista artista) {
        var response = service.criar(new Artista(artista.getNome(), artista.getTipo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistaResponse> editar(@PathVariable Long id, @RequestBody Artista artista) {
        var response = service.editar(id, artista);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistaResponse> obter(@NotNull @Positive @PathVariable Long id) {
        var response = service.obterPorId(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable @NotNull @Positive Long id) {
        service.excluir(id);
    }

}
