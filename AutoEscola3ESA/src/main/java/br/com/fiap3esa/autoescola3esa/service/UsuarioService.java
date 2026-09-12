package br.com.fiap3esa.autoescola3esa.service;

import br.com.fiap3esa.autoescola3esa.domain.ValidacaoException;
import br.com.fiap3esa.autoescola3esa.domain.usuario.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public DadosListagemUsuario cadastrarUsuario(DadosCadastroUsuario dados) {
        if (repository.existsByLogin(dados.login())) {
            throw new ValidacaoException("Já existe um usuário cadastrado com esse login!");
        }
        Usuario usuario = new Usuario(dados, passwordEncoder.encode(dados.senha()));
        return new DadosListagemUsuario(repository.save(usuario));
    }

    public Page<DadosListagemUsuario> listarUsuarios(Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemUsuario::new);
    }

    @Transactional
    public DadosListagemUsuario atualizarPerfil(DadosAtualizacaoUsuario dados) {
        Usuario usuario = buscarPorId(dados.id());
        usuario.atualizarPerfil(dados.perfil());
        return new DadosListagemUsuario(repository.save(usuario));
    }

    @Transactional
    public void excluirUsuario(Long id) {
        repository.delete(buscarPorId(id));
    }

    @Transactional
    public void alterarPropriaSenha(String login, DadosAlteracaoSenha dados) {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário autenticado não existe mais!");
        }
        if (!passwordEncoder.matches(dados.senhaAtual(), usuario.getSenha())) {
            throw new ValidacaoException("Senha atual incorreta!");
        }
        usuario.alterarSenha(passwordEncoder.encode(dados.novaSenha()));
        repository.save(usuario);
    }

    private Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNotFoundException("ID do usuário informado não existe!"));
    }
}
