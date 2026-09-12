package br.com.fiap3esa.autoescola3esa.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "Usuario")
@Table(name = "usuarios")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String senha;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    public Usuario(DadosCadastroUsuario dados, String senhaCriptografada) {
        this.login = dados.login();
        this.senha = senhaCriptografada;
        this.perfil = dados.perfil();
    }

    public void atualizarPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public void alterarSenha(String senhaCriptografada) {
        this.senha = senhaCriptografada;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }
}
