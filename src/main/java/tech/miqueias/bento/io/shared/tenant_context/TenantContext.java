package tech.miqueias.bento.io.shared.tenant_context;

import jakarta.enterprise.context.ApplicationScoped;
import tech.miqueias.bento.io.usuario.Role;
import tech.miqueias.bento.io.usuario.Usuario;

import java.util.Optional;

@ApplicationScoped
public class TenantContext {

    private final ThreadLocal<Long> restauranteId = new ThreadLocal<>();
    private final ThreadLocal<Usuario> usuario = new ThreadLocal<>();
    private final ThreadLocal<Role> papel = new ThreadLocal<>();

    public void bind(Usuario usuario, Long restauranteId, Role papel) {
        this.usuario.set(usuario);
        this.restauranteId.set(restauranteId);
        this.papel.set(papel);
    }

    public void clear() {
        usuario.remove();
        restauranteId.remove();
        papel.remove();
    }

    public Optional<Usuario> getUsuario() {
        return Optional.ofNullable(usuario.get());
    }

    public Optional<Long> getRestauranteId() {
        return Optional.ofNullable(restauranteId.get());
    }

    public Optional<Role> getPapel() {
        return Optional.ofNullable(papel.get());
    }
}
