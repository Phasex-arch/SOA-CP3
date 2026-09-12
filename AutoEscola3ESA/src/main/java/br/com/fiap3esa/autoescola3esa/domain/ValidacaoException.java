package br.com.fiap3esa.autoescola3esa.domain;

/**
 * Violação de regra de negócio. Resulta em HTTP 400.
 */
public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String message) {
        super(message);
    }
}
