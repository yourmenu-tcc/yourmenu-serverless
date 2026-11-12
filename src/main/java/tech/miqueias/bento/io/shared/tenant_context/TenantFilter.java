package tech.miqueias.bento.io.shared.tenant_context;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import tech.miqueias.bento.io.colaborador.UsuarioRestaurante;
import tech.miqueias.bento.io.colaborador.UsuarioRestauranteRepository;
import tech.miqueias.bento.io.usuario.Usuario;
import tech.miqueias.bento.io.usuario.UsuarioRepository;

import java.util.Optional;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class TenantFilter implements ContainerRequestFilter {

    private final TenantContext tenantContext;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioRestauranteRepository usuarioRestauranteRepository;

    @Inject
    public TenantFilter(TenantContext tenantContext, UsuarioRepository usuarioRepository, UsuarioRestauranteRepository usuarioRestauranteRepository) {
        this.tenantContext = tenantContext;
        this.usuarioRepository = usuarioRepository;
        this.usuarioRestauranteRepository = usuarioRestauranteRepository;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (requestContext.getSecurityContext() == null || requestContext.getSecurityContext().getUserPrincipal() == null) {
            return;
        }

        Long restauranteId = resolveRestauranteId(requestContext);
        if (restauranteId == null) {
            return;
        }

        String email = requestContext.getSecurityContext().getUserPrincipal().getName();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElse(null);

        if (usuario == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Usuário autenticado não encontrado.")
                    .build());
            return;
        }

        if (usuario.possuiRole(tech.miqueias.bento.io.usuario.Role.ADMINISTRADOR)) {
            tenantContext.bind(usuario, null, null);
            return;
        }

        Optional<UsuarioRestaurante> vinculacaoColaborador = usuarioRestauranteRepository.buscar(usuario.getId(), restauranteId);
        if (vinculacaoColaborador.isPresent() && vinculacaoColaborador.get().getStatus() == tech.miqueias.bento.io.restaurante.StatusVinculoRestaurante.ATIVO) {
            tenantContext.bind(usuario, restauranteId, vinculacaoColaborador.get().getPapel());
            return;
        }

        requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                .entity("Usuário não possui acesso válido ao restaurante informado.")
                .build());
    }

    private Long resolveRestauranteId(ContainerRequestContext requestContext) {
        String headerValue = requestContext.getHeaderString("X-Restaurante-Id");
        if (headerValue != null && !headerValue.isBlank()) {
            try {
                return Long.valueOf(headerValue.trim());
            } catch (NumberFormatException ignored) {
                requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST)
                        .entity("O cabeçalho X-Restaurante-Id deve ser um identificador numérico.")
                        .build());
                return null;
            }
        }

        String pathValue = requestContext.getUriInfo().getPathParameters().getFirst("restauranteId");
        if (pathValue != null && !pathValue.isBlank()) {
            try {
                return Long.valueOf(pathValue);
            } catch (NumberFormatException ignored) {
                requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST)
                        .entity("O parâmetro restauranteId deve ser numérico.")
                        .build());
                return null;
            }
        }

        return null;
    }
}
