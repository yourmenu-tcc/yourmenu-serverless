package tech.miqueias.bento.io.usuario;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(findById(id));
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public List<Usuario> listarTodos() {
        return listAll();
    }

    public void salvar(Usuario usuario) {
        persist(usuario);
    }

    public void deletar(Long id) {
        deleteById(id);
    }

    public boolean existePorEmail(String email) {
        return count("email", email) > 0;
    }

    public boolean existePorEmailExcetoId(String email, Long id) { return count("email = ?1 and id <> ?2", email, id) > 0; }
}
