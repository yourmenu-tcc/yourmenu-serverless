package tech.miqueias.bento.io.categoria;

import jakarta.annotation.security.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import tech.miqueias.bento.io.categoria.dto.CategoriaRequestDto;

@Path("/api/restaurantes/{restauranteId}/categorias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriaController {

    @Inject
    CategoriaService service;

    @GET
    @PermitAll
    public Response listar(@PathParam("restauranteId") Long restauranteId) {
        return Response.ok(service.listar(restauranteId)).build();
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public Response buscar(@PathParam("restauranteId") Long restauranteId, @PathParam("id") Long id) {
        return Response.ok(service.buscar(restauranteId, id)).build();
    }

    @POST
    @RolesAllowed({"ADMINISTRADOR", "GERENTE", "FUNCIONARIO"})
    public Response criar(@PathParam("restauranteId") Long restauranteId, @Valid CategoriaRequestDto r) {
        return Response.status(Response.Status.CREATED).entity(service.criar(restauranteId, r)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMINISTRADOR", "GERENTE", "FUNCIONARIO"})
    public Response atualizar(
            @PathParam("restauranteId") Long restauranteId,
            @PathParam("id") Long id,
            @Valid CategoriaRequestDto r
    ) {
        return Response.ok(service.atualizar(restauranteId, id, r)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMINISTRADOR", "GERENTE"}) public Response excluir(
            @PathParam("restauranteId") Long restauranteId,
            @PathParam("id") Long id
    ) {
        service.excluir(restauranteId, id);
        return Response.noContent().build();
    }
}
