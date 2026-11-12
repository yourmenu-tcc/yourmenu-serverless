package tech.miqueias.bento.io.pedido;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProdutoItemRepository implements PanacheRepository<ProdutoItem> {

    public boolean existePorPrato(Long pratoId) {
        return count("prato.id", pratoId) > 0;
    }
}
