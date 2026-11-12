package tech.miqueias.bento.io.entrega;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tech.miqueias.bento.io.entrega.dto.ZonaDeEntregaRequestDto;

@Path("/api/restaurantes/{restauranteId}/zonas-de-entrega")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ZonaDeEntregaController {

    @Inject
    ZonaDeEntregaService service;

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

    @POST
    @RolesAllowed({"ADMINISTRADOR", "GERENTE"})
    public Response criar(@PathParam("restauranteId") Long restauranteId, @Valid ZonaDeEntregaRequestDto r) {
        return Response.status(Response.Status.CREATED).entity(service.criar(restauranteId, r)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMINISTRADOR", "GERENTE"})
    public Response atualizar(@PathParam("restauranteId") Long restauranteId, @PathParam("id") Long id,
            @Valid ZonaDeEntregaRequestDto r) {
        return Response.ok(service.atualizar(restauranteId, id, r)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMINISTRADOR", "GERENTE"})
    public Response excluir(@PathParam("restauranteId") Long restauranteId, @PathParam("id") Long id) {
        service.excluir(restauranteId, id);
        return Response.noContent().build();
    }
}
