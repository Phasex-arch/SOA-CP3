package br.com.fiap3esa.autoescola3esa.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosAlteracaoSenha(
        @NotBlank
        String senhaAtual,

        @NotBlank
        @Size(min = 6, message = "a senha deve ter no mínimo 6 caracteres")
        String novaSenha) {
}
