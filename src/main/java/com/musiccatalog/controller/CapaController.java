package com.musiccatalog.controller;

import com.musiccatalog.model.Capa;
import com.musiccatalog.service.CapaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/v1/capas")
@Tag(name = "4 - Capas", description = "Endpoints para gerenciar capas do album")
public class CapaController {

    private final CapaService service;
    public CapaController(CapaService service) {this.service = service;}

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Anexar capa", description = "Anexa capa ao album do artista")
    public Capa criar(@RequestParam Long albumId, @RequestPart("arquivo") MultipartFile arquivo) {
        return service.salvar(albumId, arquivo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui capa", description = "Exclui capa do album do artista")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable @NotNull @Positive Long id) {
        service.excluir(id);
    }
}
