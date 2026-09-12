package br.com.fiap3esa.autoescola3esa.controller;

import br.com.fiap3esa.autoescola3esa.domain.usuario.DadosAutenticacao;
import br.com.fiap3esa.autoescola3esa.domain.usuario.Usuario;
import br.com.fiap3esa.autoescola3esa.infra.security.DadosTokenJWT;
import br.com.fiap3esa.autoescola3esa.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {
    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication authentication = manager.authenticate(token);
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(new DadosTokenJWT(tokenService.gerarToken(usuario)));
    }
}
