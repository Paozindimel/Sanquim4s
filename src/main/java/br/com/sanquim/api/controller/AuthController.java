package br.com.sanquim.api.controller;

import br.com.sanquim.api.dto.request.LoginRequestDTO;
import br.com.sanquim.api.dto.response.LoginResponseDTO;
import br.com.sanquim.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "1. Autenticação (/api/auth)", description = "Autenticação Stateless via e-mail e senha com emissão de Token JWT")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica usuário e retorna token JWT assinado, expiração e perfil (ROLE_)")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.autenticar(dto));
    }
}
