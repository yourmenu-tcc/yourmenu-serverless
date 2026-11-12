package tech.miqueias.bento.io.usuario;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import tech.miqueias.bento.io.usuario.dto.UsuarioResponseDTO;
import tech.miqueias.bento.io.usuario.dto.UsuarioAtualizacaoRequestDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Usuários", description = "Endpoints de visualização e gerenciamento de usuários")
@SecurityRequirement(name = "jwt")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Inject
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GET
    @Path("/all")
    @RolesAllowed("ADMINISTRADOR")
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMINISTRADOR")
    public UsuarioResponseDTO buscarPorId(@PathParam("id") Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMINISTRADOR")
    public UsuarioResponseDTO atualizar(@PathParam("id") Long id, @Valid UsuarioAtualizacaoRequestDTO request) { return usuarioService.atualizar(id, request); }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMINISTRADOR")
    public Response excluir(@PathParam("id") Long id) { usuarioService.excluir(id); return Response.noContent().build(); }

}
