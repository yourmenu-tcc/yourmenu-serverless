package tech.miqueias.bento.io.pedido;

import java.util.List;
import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidoRepository implements PanacheRepository<Pedido> {
    public Optional<Pedido> buscarPorIdERestaurante(Long id, Long restauranteId) {
        return find("id = ?1 and restaurante.id = ?2", id, restauranteId).firstResultOptional();
    }

    public List<Pedido> listarPorRestaurante(Long restauranteId) {
        return list("restaurante.id = ?1 order by criadoEm desc", restauranteId);
    }
}
