package tech.miqueias.bento.io.pratos;

import java.util.List;
import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PratoRepository implements PanacheRepository<Prato> {
    public Optional<Prato> buscarPorIdERestaurante(Long id, Long restauranteId) {
        return find("id = ?1 and restaurante.id = ?2", id, restauranteId).firstResultOptional();
    }

    public List<Prato> listarPorRestaurante(Long restauranteId) {
        return list("restaurante.id = ?1 order by nome", restauranteId);
    }

    public boolean existePorCategoria(Long categoriaId) {
        return count("categoria.id", categoriaId) > 0;
    }
}
