package br.com.fiap3esa.autoescola3esa.service;

import br.com.fiap3esa.autoescola3esa.domain.ValidacaoException;
import br.com.fiap3esa.autoescola3esa.domain.aluno.Aluno;
import br.com.fiap3esa.autoescola3esa.domain.aluno.AlunoNotFoundException;
import br.com.fiap3esa.autoescola3esa.domain.aluno.AlunoRepository;
import br.com.fiap3esa.autoescola3esa.domain.instrucao.*;
import br.com.fiap3esa.autoescola3esa.domain.instrutor.Instrutor;
import br.com.fiap3esa.autoescola3esa.domain.instrutor.InstrutorNotFoundException;
import br.com.fiap3esa.autoescola3esa.domain.instrutor.InstrutorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class InstrucaoService {
    private static final int ABERTURA = 6;
    private static final int ULTIMO_HORARIO = 20; // instrução de 1h, encerramento às 21:00
    private static final int MAX_INSTRUCOES_POR_DIA = 2;

    private final InstrucaoRepository repository;
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;

    public InstrucaoService(InstrucaoRepository repository,
                            AlunoRepository alunoRepository,
                            InstrutorRepository instrutorRepository) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.instrutorRepository = instrutorRepository;
    }

    @Transactional
    public DadosDetalhamentoInstrucao agendar(DadosAgendamentoInstrucao dados) {
        validarHorarioFuncionamento(dados.data());
        validarAntecedenciaAgendamento(dados.data(), LocalDateTime.now());

        Aluno aluno = alunoRepository.findById(dados.alunoId())
                .orElseThrow(() -> new AlunoNotFoundException("ID do aluno informado não existe!"));
        if (!aluno.isAtivo()) {
            throw new ValidacaoException("Não é possível agendar instrução para aluno inativo!");
        }

        LocalDateTime inicioDoDia = dados.data().toLocalDate().atStartOfDay();
        long noDia = repository.countByAlunoIdAndDataBetweenAndMotivoCancelamentoIsNull(
                aluno.getId(), inicioDoDia, inicioDoDia.plusDays(1).minusNanos(1));
        if (noDia >= MAX_INSTRUCOES_POR_DIA) {
            throw new ValidacaoException(
                    "O aluno já possui " + MAX_INSTRUCOES_POR_DIA + " instruções agendadas nesse dia!");
        }

        Instrutor instrutor = escolherInstrutor(dados);
        return new DadosDetalhamentoInstrucao(
                repository.save(new Instrucao(aluno, instrutor, dados.data())));
    }

    @Transactional
    public DadosDetalhamentoInstrucao cancelar(DadosCancelamentoInstrucao dados) {
        Instrucao instrucao = repository.findById(dados.id())
                .orElseThrow(() ->
                        new InstrucaoNotFoundException("ID da instrução informado não existe!"));
        if (instrucao.isCancelada()) {
            throw new ValidacaoException("Instrução já está cancelada!");
        }
        validarAntecedenciaCancelamento(instrucao.getData(), LocalDateTime.now());

        instrucao.cancelar(dados.motivo());
        return new DadosDetalhamentoInstrucao(repository.save(instrucao));
    }

    public Page<DadosDetalhamentoInstrucao> listarInstrucoes(Pageable paginacao) {
        return repository.findAllByMotivoCancelamentoIsNull(paginacao)
                .map(DadosDetalhamentoInstrucao::new);
    }

    public DadosDetalhamentoInstrucao detalharInstrucao(Long id) {
        return repository.findById(id)
                .map(DadosDetalhamentoInstrucao::new)
                .orElseThrow(() ->
                        new InstrucaoNotFoundException("ID da instrução informado não existe!"));
    }

    private Instrutor escolherInstrutor(DadosAgendamentoInstrucao dados) {
        if (dados.instrutorId() == null) {
            List<Instrutor> livres = repository.buscarInstrutoresLivresNaData(dados.data());
            if (livres.isEmpty()) {
                throw new ValidacaoException("Nenhum instrutor disponível nessa data/hora!");
            }
            return livres.get(ThreadLocalRandom.current().nextInt(livres.size()));
        }

        Instrutor instrutor = instrutorRepository.findById(dados.instrutorId())
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do instrutor informado não existe!"));
        if (!instrutor.isAtivo()) {
            throw new ValidacaoException("Não é possível agendar instrução com instrutor inativo!");
        }
        if (repository.existsByInstrutorIdAndDataAndMotivoCancelamentoIsNull(
                instrutor.getId(), dados.data())) {
            throw new ValidacaoException("O instrutor já possui outra instrução nessa data/hora!");
        }
        return instrutor;
    }

    // Regras puras de data/hora — validadas em InstrucaoServiceTest.

    static void validarHorarioFuncionamento(LocalDateTime data) {
        if (data.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new ValidacaoException("A auto-escola funciona de segunda a sábado!");
        }
        if (data.getHour() < ABERTURA || data.getHour() > ULTIMO_HORARIO) {
            throw new ValidacaoException(
                    "As instruções devem começar entre " + ABERTURA + ":00 e " + ULTIMO_HORARIO + ":00!");
        }
        if (data.getMinute() != 0) {
            throw new ValidacaoException("As instruções têm duração fixa de 1 hora e começam em hora cheia!");
        }
    }

    static void validarAntecedenciaAgendamento(LocalDateTime data, LocalDateTime agora) {
        if (data.isBefore(agora.plusMinutes(30))) {
            throw new ValidacaoException("A instrução deve ser agendada com antecedência mínima de 30 minutos!");
        }
    }

    static void validarAntecedenciaCancelamento(LocalDateTime data, LocalDateTime agora) {
        if (data.isBefore(agora.plusHours(24))) {
            throw new ValidacaoException("A instrução só pode ser cancelada com antecedência mínima de 24 horas!");
        }
    }
}
