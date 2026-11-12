package tech.miqueias.bento.io.colaborador;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.List;
import tech.miqueias.bento.io.restaurante.StatusVinculoRestaurante;
import tech.miqueias.bento.io.usuario.Role;

@ApplicationScoped
public class UsuarioRestauranteRepository implements PanacheRepository<UsuarioRestaurante> {

    public Optional<UsuarioRestaurante> buscar(Long usuarioId, Long restauranteId) {
        return find("usuario.id = ?1 and restaurante.id = ?2", usuarioId, restauranteId).firstResultOptional();
    }

    public Optional<UsuarioRestaurante> buscarPorIdERestaurante(Long id, Long restauranteId) { 
        return find("id = ?1 and restaurante.id = ?2", id, restauranteId).firstResultOptional(); 
    }

    public List<UsuarioRestaurante> listarPorRestaurante(Long restauranteId) { 
        return list("restaurante.id = ?1 order by usuario.nome", restauranteId); 
    }

    public long contarAtivosPorPapel(Long restauranteId, Role papel) { 
        return count("restaurante.id = ?1 and papel = ?2 and status = ?3", restauranteId, papel, StatusVinculoRestaurante.ATIVO); 
    }
}
