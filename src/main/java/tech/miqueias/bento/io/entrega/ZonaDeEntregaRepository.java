package tech.miqueias.bento.io.entrega;

import java.util.List;
import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ZonaDeEntregaRepository implements PanacheRepository<ZonaDeEntrega> {

    public Optional<ZonaDeEntrega> buscarPorIdERestaurante(Long id, Long restauranteId) {
        return find("id = ?1 and restaurante.id = ?2", id, restauranteId).firstResultOptional();
    }

    public List<ZonaDeEntrega> listarPorRestaurante(Long restauranteId) {
        return list("restaurante.id = ?1 order by localizacao", restauranteId);
    }
}
