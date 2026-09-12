package br.com.fiap3esa.autoescola3esa.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByLogin(String login);

    boolean existsByLogin(String login);
}
