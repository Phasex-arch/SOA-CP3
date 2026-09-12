package br.com.fiap3esa.autoescola3esa.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(
        @NotBlank
        @Email
        String login,

        @NotBlank
        @Size(min = 6, message = "a senha deve ter no mínimo 6 caracteres")
        String senha,

        @NotNull
        Perfil perfil) {
}
