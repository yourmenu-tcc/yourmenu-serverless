package tech.miqueias.bento.io.shared.exception;

public class RecursoNaoEncontradoException extends DomainException {

    public RecursoNaoEncontradoException(String recurso, Object id) {
        super(recurso + " não encontrado(a) com id: " + id);
    }
}
