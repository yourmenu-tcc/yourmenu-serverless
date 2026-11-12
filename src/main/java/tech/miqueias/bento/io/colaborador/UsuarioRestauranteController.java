package tech.miqueias.bento.io.colaborador;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import tech.miqueias.bento.io.colaborador.dto.*;

@Path("/api/restaurantes/{restauranteId}/colaboradores") 
@Produces(MediaType.APPLICATION_JSON) 
@Consumes(MediaType.APPLICATION_JSON) 
@RolesAllowed({"ADMINISTRADOR", "GERENTE"})
public class UsuarioRestauranteController {

    @Inject 
    UsuarioRestauranteService service;

    @GET 
    public Response listar(@PathParam("restauranteId") Long restauranteId) { 
        return Response.ok(service.listar(restauranteId)).build(); 
    }

    @POST 
    public Response criar(
        @PathParam("restauranteId") Long restauranteId, 
        @Valid ColaboradorRequestDto r
    ) { 
        return Response.status(Response.Status.CREATED).entity(service.criar(restauranteId, r)).build(); 
    }

    @PUT 
    @Path("/{id}") 
    public Response atualizar(
        @PathParam("restauranteId") Long restauranteId, 
        @PathParam("id") Long id, 
        @Valid ColaboradorAtualizacaoRequestDto r
    ) {
        return Response.ok(service.atualizar(restauranteId, id, r)).build(); 
    }

    @DELETE
    @Path("/{id}") 
    public Response excluir(
        @PathParam("restauranteId") Long restauranteId, 
        @PathParam("id") Long id
    ) { 
        service.excluir(restauranteId, id); 
        return Response.noContent().build(); 
    }
}
