package br.com.sanquim.api.service;

import br.com.sanquim.api.domain.entity.Usuario;
import br.com.sanquim.api.dto.request.LoginRequestDTO;
import br.com.sanquim.api.dto.response.LoginResponseDTO;
import br.com.sanquim.api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO autenticar(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email().trim(), dto.senha())
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtService.gerarToken(usuario);

        return new LoginResponseDTO(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                jwtService.calcularDataExpiracao(),
                usuario.getRole().name(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}
