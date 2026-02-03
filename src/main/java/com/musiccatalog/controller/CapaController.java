package com.musiccatalog.controller;

import com.musiccatalog.model.Capa;
import com.musiccatalog.service.CapaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/v1/capas")
public class CapaController {

    private final CapaService service;
    public CapaController(CapaService service) {this.service = service;}

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Capa criar(@RequestParam Long albumId, @RequestPart("arquivo") MultipartFile arquivo) {
        return service.salvar(albumId, arquivo);
    }
}
