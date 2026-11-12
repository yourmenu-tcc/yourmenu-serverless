package tech.miqueias.bento.io.pedido;

import java.util.List;
import java.util.Optional;
import java.util.EnumMap;
import java.util.Map;

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

    public Map<StatusPedido, Long> contarPorStatusERestaurante(Long restauranteId) {
        List<Object[]> resultados = getEntityManager()
                .createQuery("select p.status, count(p) from Pedido p where p.restaurante.id = :restauranteId group by p.status", Object[].class)
                .setParameter("restauranteId", restauranteId)
                .getResultList();
        Map<StatusPedido, Long> contagens = new EnumMap<>(StatusPedido.class);
        for (Object[] resultado : resultados) {
            contagens.put((StatusPedido) resultado[0], (Long) resultado[1]);
        }
        return contagens;
    }
}
