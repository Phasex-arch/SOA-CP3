package br.com.fiap3esa.autoescola3esa.domain.instrucao;

public class InstrucaoNotFoundException extends RuntimeException {
    public InstrucaoNotFoundException(String message) {
        super(message);
    }
}
