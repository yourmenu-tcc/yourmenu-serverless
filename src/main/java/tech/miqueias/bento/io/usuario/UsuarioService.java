package tech.miqueias.bento.io.usuario;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tech.miqueias.bento.io.shared.exception.*;
import tech.miqueias.bento.io.usuario.dto.UsuarioResponseDTO;
import tech.miqueias.bento.io.usuario.dto.UsuarioAtualizacaoRequestDTO;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Inject
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.listarTodos().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
        return toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioAtualizacaoRequestDTO request) {
        Usuario usuario = usuarioRepository.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
        if (usuarioRepository.existePorEmailExcetoId(request.email(), id)) throw new DomainException("Já existe um usuário com o email: " + request.email());
        usuario.atualizar(request.nome(), request.email(), request.roles());
        return toResponseDTO(usuario);
    }

    @Transactional
    public void excluir(Long id) {
        if (!usuarioRepository.deleteById(id)) throw new RecursoNaoEncontradoException("Usuário", id);
    }

    private UsuarioResponseDTO toResponseDTO(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail(), u.getRoles());
    }
}
