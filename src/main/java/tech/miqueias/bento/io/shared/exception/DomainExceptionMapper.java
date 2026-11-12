package tech.miqueias.bento.io.shared.exception;

import java.util.Map;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {
    @Override public Response toResponse(DomainException exception) {
        Response.Status status = exception instanceof RecursoNaoEncontradoException ? Response.Status.NOT_FOUND : Response.Status.BAD_REQUEST;
        return Response.status(status).entity(Map.of("erro", exception.getMessage())).build();
    }
}
