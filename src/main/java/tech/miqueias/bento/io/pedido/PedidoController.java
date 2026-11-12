package tech.miqueias.bento.io.pedido;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tech.miqueias.bento.io.pedido.dto.AlterarStatusPedidoRequestDto;
import tech.miqueias.bento.io.pedido.dto.PedidoRequestDto;

@Path("/api/restaurantes/{restauranteId}/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoController {

    @Inject
    PedidoService service;

    @POST
    @PermitAll
    public Response criar(@PathParam("restauranteId") Long restauranteId, @Valid PedidoRequestDto request) {
        return Response.status(Response.Status.CREATED)
                .entity(service.criar(restauranteId, request))
                .build();
    }

    @GET
    @RolesAllowed({"ADMINISTRADOR", "GERENTE", "FUNCIONARIO"})
    public Response listar(@PathParam("restauranteId") Long restauranteId) {
        return Response.ok(service.listar(restauranteId)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMINISTRADOR", "GERENTE", "FUNCIONARIO"})
    public Response buscar(@PathParam("restauranteId") Long restauranteId, @PathParam("id") Long id) {
        return Response.ok(service.buscar(restauranteId, id)).build();
    }

    @PATCH
    @Path("/{id}/status")
    @RolesAllowed({"ADMINISTRADOR", "GERENTE", "FUNCIONARIO"})
    public Response alterarStatus(
            @PathParam("restauranteId") Long restauranteId,
            @PathParam("id") Long id,
            @Valid AlterarStatusPedidoRequestDto request) {
        return Response.ok(service.alterarStatus(restauranteId, id, request.status())).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMINISTRADOR", "GERENTE", "FUNCIONARIO"})
    public Response cancelar(@PathParam("restauranteId") Long restauranteId, @PathParam("id") Long id) {
        service.cancelar(restauranteId, id);
        return Response.noContent().build();
    }
}
