package br.com.fiap3esa.autoescola3esa.domain.instrucao;

import br.com.fiap3esa.autoescola3esa.domain.aluno.Aluno;
import br.com.fiap3esa.autoescola3esa.domain.instrutor.Instrutor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Instrucao")
@Table(name = "instrucoes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Instrucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;

    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    private MotivoCancelamento motivoCancelamento;

    public Instrucao(Aluno aluno, Instrutor instrutor, LocalDateTime data) {
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.data = data;
    }

    public void cancelar(MotivoCancelamento motivo) {
        this.motivoCancelamento = motivo;
    }

    public boolean isCancelada() {
        return motivoCancelamento != null;
    }
}
