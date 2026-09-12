package br.com.fiap3esa.autoescola3esa.domain.instrucao;

import br.com.fiap3esa.autoescola3esa.domain.instrutor.Instrutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface InstrucaoRepository extends JpaRepository<Instrucao, Long> {
    boolean existsByInstrutorIdAndDataAndMotivoCancelamentoIsNull(Long instrutorId, LocalDateTime data);

    long countByAlunoIdAndDataBetweenAndMotivoCancelamentoIsNull(
            Long alunoId, LocalDateTime inicioDoDia, LocalDateTime fimDoDia);

    Page<Instrucao> findAllByMotivoCancelamentoIsNull(Pageable paginacao);

    @Query("""
            select i from Instrutor i
            where i.ativo = true
            and i.id not in (
                select ins.instrutor.id from Instrucao ins
                where ins.data = :data and ins.motivoCancelamento is null
            )
            """)
    List<Instrutor> buscarInstrutoresLivresNaData(LocalDateTime data);
}
