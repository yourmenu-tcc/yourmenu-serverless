package tech.miqueias.bento.io.pedido;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnderecoPedidoRepository implements PanacheRepository<EnderecoPedido> {
    public boolean existePorZonaDeEntrega(Long zonaId) {
        return count("zonaDeEntrega.id", zonaId) > 0;
    }
}
