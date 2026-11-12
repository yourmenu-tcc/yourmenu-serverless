package tech.miqueias.bento.io.categoria;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoriaRepository implements PanacheRepository<Categoria> {

    public java.util.Optional<Categoria> buscarPorIdERestaurante(Long id, Long restauranteId) {
        return find("id = ?1 and restaurante.id = ?2", id, restauranteId).firstResultOptional();
    }

    public java.util.List<Categoria> listarPorRestaurante(Long restauranteId) {
        return list("restaurante.id = ?1 order by nome", restauranteId);
    }
}
