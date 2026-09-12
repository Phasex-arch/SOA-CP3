package br.com.fiap3esa.autoescola3esa.domain.usuario;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
