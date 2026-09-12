package br.com.fiap3esa.autoescola3esa.controller;

import br.com.fiap3esa.autoescola3esa.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3esa.autoescola3esa.domain.instrucao.DadosCancelamentoInstrucao;
import br.com.fiap3esa.autoescola3esa.domain.instrucao.DadosDetalhamentoInstrucao;
import br.com.fiap3esa.autoescola3esa.service.InstrucaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/instrucoes")
public class InstrucaoController {
    private final InstrucaoService service;

    public InstrucaoController(InstrucaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoInstrucao> agendar(
            @RequestBody @Valid DadosAgendamentoInstrucao dados,
            UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoInstrucao dto = service.agendar(dados);
        URI uri = uriBuilder.path("/instrucoes/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @DeleteMapping
    public ResponseEntity<DadosDetalhamentoInstrucao> cancelar(
            @RequestBody @Valid DadosCancelamentoInstrucao dados) {
        return ResponseEntity.ok(service.cancelar(dados));
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoInstrucao>> listarInstrucoes(
            @PageableDefault(size = 10, sort = "data") Pageable paginacao) {
        return ResponseEntity.ok(service.listarInstrucoes(paginacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoInstrucao> detalharInstrucao(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalharInstrucao(id));
    }
}
