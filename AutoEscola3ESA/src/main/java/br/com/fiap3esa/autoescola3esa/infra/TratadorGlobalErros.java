package br.com.fiap3esa.autoescola3esa.infra;

import br.com.fiap3esa.autoescola3esa.domain.ValidacaoException;
import br.com.fiap3esa.autoescola3esa.domain.aluno.AlunoNotFoundException;
import br.com.fiap3esa.autoescola3esa.domain.instrucao.InstrucaoNotFoundException;
import br.com.fiap3esa.autoescola3esa.domain.instrutor.InstrutorNotFoundException;
import br.com.fiap3esa.autoescola3esa.domain.usuario.UsuarioNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorGlobalErros {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarEntityNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({
            InstrutorNotFoundException.class,
            AlunoNotFoundException.class,
            UsuarioNotFoundException.class,
            InstrucaoNotFoundException.class})
    public ResponseEntity<DadosMessageNotFound> tratarNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosMessageNotFound(e.getMessage()));
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<DadosMessageNotFound> tratarRegraDeNegocio(ValidacaoException e) {
        return ResponseEntity.badRequest().body(new DadosMessageNotFound(e.getMessage()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<DadosMessageNotFound> tratarAcessoNegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new DadosMessageNotFound("Acesso negado: requer perfil de administrador."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DadosMessageNotFound> tratarFalhaAutenticacao() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new DadosMessageNotFound("Login ou senha inválidos."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosBadRequest>> tratarBadRequest(MethodArgumentNotValidException e) {
        List<FieldError> erros = e.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosBadRequest::new).toList());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DadosMessageNotFound> tratarGenericException(Exception e) {
        return ResponseEntity.unprocessableContent().body(new DadosMessageNotFound(e.getMessage()));
    }

    private record DadosBadRequest(String field, String message) {
        public DadosBadRequest(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    private record DadosMessageNotFound(String message) {
    }
}
