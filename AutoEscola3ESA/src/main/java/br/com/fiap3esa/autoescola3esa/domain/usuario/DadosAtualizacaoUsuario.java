package br.com.fiap3esa.autoescola3esa.domain.usuario;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoUsuario(
        @NotNull
        Long id,

        @NotNull
        Perfil perfil) {
}
