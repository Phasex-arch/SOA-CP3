package br.com.fiap3esa.autoescola3esa.domain.instrucao;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendamentoInstrucao(
        @NotNull
        Long alunoId,

        // opcional: quando ausente, o sistema escolhe um instrutor livre
        Long instrutorId,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime data) {
}
