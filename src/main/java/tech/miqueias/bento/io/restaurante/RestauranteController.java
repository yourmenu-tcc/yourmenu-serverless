package tech.miqueias.bento.io.restaurante;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import tech.miqueias.bento.io.restaurante.dto.RestauranteRequestDto;
import tech.miqueias.bento.io.restaurante.dto.RestauranteResponseDto;
import tech.miqueias.bento.io.shared.tenant_context.TenantContext;

import java.util.List;

@Path("/api/restaurante")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Restaurante", description = "Endpoints de visualização e gerenciamento de restaurantes")
@SecurityRequirement(name = "jwt")
public class RestauranteController {

    private final RestauranteService restauranteService;
    private final TenantContext tenantContext;

    @Inject
    public RestauranteController(
            RestauranteService restauranteService,
            TenantContext tenantContext
    ) {
        this.restauranteService = restauranteService;
        this.tenantContext = tenantContext;
    }

    @GET
    @Path("")
    @PermitAll
    public Response listarTodos(@QueryParam("page") @DefaultValue("0") int page,
                               @QueryParam("size") @DefaultValue("10") int size) {
        List<RestauranteResponseDto> restaurantes = restauranteService.buscarTodos(page, size);
        return Response.ok(restaurantes).build();
    }

    @GET
    @Path("/{restauranteId}")
    @PermitAll
    public Response buscarPorId(@PathParam("restauranteId") Long restauranteId) {
        return restauranteService.buscarPorId(restauranteId)
                .map(r -> Response.ok(r).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).entity("Restaurante não encontrado").build());
    }

    @POST
    @Path("")
    @RolesAllowed({"ADMINISTRADOR"})
    public Response criar(RestauranteRequestDto dto) {
        RestauranteResponseDto criado = restauranteService.criar(dto);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    @PUT
    @Path("/{restauranteId}")
    @RolesAllowed({"ADMINISTRADOR", "GERENTE"})
    public Response atualizar(@PathParam("restauranteId") Long restauranteId, RestauranteRequestDto dto) {
        // validação: se papel do tenant é GERENTE, o restauranteId deve bater com o do contexto
        var papelOpt = tenantContext.getPapel();
        var restOpt = tenantContext.getRestauranteId();
        if (papelOpt.isPresent() && papelOpt.get() == tech.miqueias.bento.io.usuario.Role.GERENTE) {
            if (restOpt.isEmpty() || !restOpt.get().equals(restauranteId)) {
                return Response.status(Response.Status.FORBIDDEN).entity("Apenas o gerente deste restaurante pode atualizar.").build();
            }
        }

        try {
            RestauranteResponseDto atualizado = restauranteService.atualizar(restauranteId, dto);
            return Response.ok(atualizado).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{restauranteId}")
    @RolesAllowed({"ADMINISTRADOR", "GERENTE"})
    public Response deletar(@PathParam("restauranteId") Long restauranteId) {
        var papelOpt = tenantContext.getPapel();
        var restOpt = tenantContext.getRestauranteId();
        if (papelOpt.isPresent() && papelOpt.get() == tech.miqueias.bento.io.usuario.Role.GERENTE) {
            if (restOpt.isEmpty() || !restOpt.get().equals(restauranteId)) {
                return Response.status(Response.Status.FORBIDDEN).entity("Apenas o gerente deste restaurante pode remover.").build();
            }
        }

        restauranteService.deletar(restauranteId);
        return Response.noContent().build();
    }
}
