package br.com.sanquim.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Tag(name = "0. Status da API", description = "Verificação rápida de disponibilidade da API")
public class StatusController {

    @GetMapping({"/", "/api/status"})
    @Operation(summary = "Verifica se a API está online")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(Map.of(
                "sistema", "Colégio Sanquim — API RESTful",
                "status", "ONLINE",
                "documentacaoSwagger", "/swagger-ui.html",
                "timestamp", LocalDateTime.now()
        ));
    }
}
