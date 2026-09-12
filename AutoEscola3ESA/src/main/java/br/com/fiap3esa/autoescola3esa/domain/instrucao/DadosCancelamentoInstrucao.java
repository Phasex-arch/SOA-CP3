package br.com.fiap3esa.autoescola3esa.domain.instrucao;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoInstrucao(
        @NotNull
        Long id,

        @NotNull(message = "o motivo do cancelamento é obrigatório")
        MotivoCancelamento motivo) {
}
