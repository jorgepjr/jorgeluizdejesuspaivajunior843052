package com.musiccatalog.service;

import com.musiccatalog.model.Regional;
import com.musiccatalog.repository.RegionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegionalService {

    private final RegionalRepository repository;
    private final WebClient webClient;

    public RegionalService(RegionalRepository repository, WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    public List<Regional> sincronizar() {
        Regional[] regionaisApi = webClient.get()
                .uri("https://integrador-argus-api.geia.vip/v1/regionais")
                .retrieve()
                .bodyToMono(Regional[].class)
                .block();

        List<Regional> regionaisSalvos = new ArrayList<>();
        if (regionaisApi != null) {
            for (Regional rApi : regionaisApi) {

                String sincId = String.valueOf(rApi.getId());
                LocalDateTime agora = LocalDateTime.now();

                Regional regionalExistente = repository.findBySincId(sincId).orElse(null);

                if (regionalExistente != null) {

                    regionalExistente.setNome(rApi.getNome());
                    regionalExistente.setAtivo(true);
                    regionalExistente.setUltimaSinc(agora);
                    regionaisSalvos.add(repository.save(regionalExistente));
                } else {

                    Regional novo = new Regional();
                    novo.setNome(rApi.getNome());
                    novo.setSincId(sincId);
                    novo.setAtivo(true);
                    novo.setCriadoEm(agora);
                    novo.setUltimaSinc(agora);

                    regionaisSalvos.add(repository.save(novo));
                }
            }
        }

        return regionaisSalvos;
    }


}


