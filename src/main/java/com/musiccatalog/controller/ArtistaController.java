package com.musiccatalog.controller;

import com.musiccatalog.dto.ArtistaResponse;
import com.musiccatalog.enums.TipoArtista;
import com.musiccatalog.model.Artista;
import com.musiccatalog.service.ArtistaService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Artista criar(@RequestBody Artista artista){
        return service.criar(new Artista(artista.getNome(), artista.getTipo()));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Artista editar(@PathVariable Long id, @RequestBody Artista artista) {
        return service.editar(id, artista);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Artista obter(@NotNull @Positive @PathVariable Long id) {
        return service.obterPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable @NotNull @Positive Long id) {
       service.excluir(id);
    }
}
