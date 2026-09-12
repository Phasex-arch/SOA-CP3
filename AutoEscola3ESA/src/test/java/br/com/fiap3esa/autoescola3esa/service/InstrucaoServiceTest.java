package br.com.fiap3esa.autoescola3esa.service;

import br.com.fiap3esa.autoescola3esa.domain.ValidacaoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InstrucaoServiceTest {
    // 2026-09-14 é uma segunda-feira; 2026-09-13, um domingo.
    private static final LocalDateTime SEGUNDA_10H = LocalDateTime.of(2026, 9, 14, 10, 0);

    @Test
    void horarioValidoDentroDoFuncionamento() {
        assertDoesNotThrow(() -> InstrucaoService.validarHorarioFuncionamento(SEGUNDA_10H));
        assertDoesNotThrow(() -> InstrucaoService.validarHorarioFuncionamento(SEGUNDA_10H.withHour(6)));
        assertDoesNotThrow(() -> InstrucaoService.validarHorarioFuncionamento(SEGUNDA_10H.withHour(20)));
    }

    @Test
    void domingoNaoEhPermitido() {
        assertThrows(ValidacaoException.class,
                () -> InstrucaoService.validarHorarioFuncionamento(SEGUNDA_10H.minusDays(1)));
    }

    @Test
    void foraDoHorarioDeFuncionamentoNaoEhPermitido() {
        assertThrows(ValidacaoException.class,
                () -> InstrucaoService.validarHorarioFuncionamento(SEGUNDA_10H.withHour(5)));
        // 21:00 encerraria às 22:00
        assertThrows(ValidacaoException.class,
                () -> InstrucaoService.validarHorarioFuncionamento(SEGUNDA_10H.withHour(21)));
    }

    @Test
    void horarioQuebradoNaoEhPermitido() {
        assertThrows(ValidacaoException.class,
                () -> InstrucaoService.validarHorarioFuncionamento(SEGUNDA_10H.withMinute(30)));
    }

    @Test
    void agendamentoExigeTrintaMinutosDeAntecedencia() {
        LocalDateTime agora = SEGUNDA_10H.minusMinutes(29);
        assertThrows(ValidacaoException.class,
                () -> InstrucaoService.validarAntecedenciaAgendamento(SEGUNDA_10H, agora));
        assertDoesNotThrow(() -> InstrucaoService.validarAntecedenciaAgendamento(
                SEGUNDA_10H, SEGUNDA_10H.minusMinutes(31)));
    }

    @Test
    void cancelamentoExigeVinteEQuatroHorasDeAntecedencia() {
        assertThrows(ValidacaoException.class,
                () -> InstrucaoService.validarAntecedenciaCancelamento(
                        SEGUNDA_10H, SEGUNDA_10H.minusHours(23)));
        assertDoesNotThrow(() -> InstrucaoService.validarAntecedenciaCancelamento(
                SEGUNDA_10H, SEGUNDA_10H.minusHours(25)));
    }
}
