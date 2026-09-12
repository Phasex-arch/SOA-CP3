package br.com.fiap3esa.autoescola3esa.domain.instrucao;

import java.time.LocalDateTime;

public record DadosDetalhamentoInstrucao(
        Long id,
        Long alunoId,
        String alunoNome,
        Long instrutorId,
        String instrutorNome,
        LocalDateTime data,
        MotivoCancelamento motivoCancelamento) {
    public DadosDetalhamentoInstrucao(Instrucao instrucao) {
        this(
                instrucao.getId(),
                instrucao.getAluno().getId(),
                instrucao.getAluno().getNome(),
                instrucao.getInstrutor().getId(),
                instrucao.getInstrutor().getNome(),
                instrucao.getData(),
                instrucao.getMotivoCancelamento());
    }
}
