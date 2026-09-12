package br.com.fiap3esa.autoescola3esa.controller;

import br.com.fiap3esa.autoescola3esa.domain.usuario.*;
import br.com.fiap3esa.autoescola3esa.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosListagemUsuario> cadastrarUsuario(
            @RequestBody @Valid DadosCadastroUsuario dados,
            UriComponentsBuilder uriBuilder) {
        DadosListagemUsuario dto = service.cadastrarUsuario(dados);
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DadosListagemUsuario>> listarUsuarios(
            @PageableDefault(size = 10, sort = "login") Pageable paginacao) {
        return ResponseEntity.ok(service.listarUsuarios(paginacao));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosListagemUsuario> atualizarPerfil(
            @RequestBody @Valid DadosAtualizacaoUsuario dados) {
        return ResponseEntity.ok(service.atualizarPerfil(dados));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
        service.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/senha")
    public ResponseEntity<Void> alterarPropriaSenha(
            @RequestBody @Valid DadosAlteracaoSenha dados,
            @AuthenticationPrincipal Usuario logado) {
        service.alterarPropriaSenha(logado.getLogin(), dados);
        return ResponseEntity.noContent().build();
    }
}
