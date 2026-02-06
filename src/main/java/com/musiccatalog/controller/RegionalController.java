package com.musiccatalog.controller;

import com.musiccatalog.model.Regional;
import com.musiccatalog.service.RegionalService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regionais")
public class RegionalController {

    private final RegionalService service;

    public RegionalController(RegionalService service) {this.service = service;}

    @PostMapping("/sincronizar")
    @Operation(summary = "Sincronizar regionais", description = "Busca regionais da API externa e atualiza base de dados")
    public ResponseEntity<List<Regional>> sincronizar() {
        List<Regional> response = service.sincronizar();
        return ResponseEntity.ok(response);
    }
}

