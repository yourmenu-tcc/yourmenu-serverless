package tech.miqueias.bento.io.shared.exception;

import java.util.Map;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {
    @Override public Response toResponse(DomainException exception) {
        Response.Status status;

        if (exception instanceof AcessoNegadoException) {
            status = Response.Status.FORBIDDEN;
        } else if (exception instanceof RecursoNaoEncontradoException) {
            status = Response.Status.NOT_FOUND;
        } else {
            status = Response.Status.BAD_REQUEST;
        }

        return Response.status(status).entity(Map.of("erro", exception.getMessage())).build();
    }
}
