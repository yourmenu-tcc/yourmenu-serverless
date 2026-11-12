package tech.miqueias.bento.io.restaurante;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RestauranteRepository implements PanacheRepository<Restaurante>{
	public Optional<Restaurante> buscarPorId(Long id) {
		return Optional.ofNullable(findById(id));
	}

	public List<Restaurante> listarTodos(Integer page, Integer size) {
		return findAll().page(io.quarkus.panache.common.Page.of(page, size)).list();
	}

	public void salvar(Restaurante restaurante) {
		persist(restaurante);
	}

	public void deletarPorId(Long id) {
		deleteById(id);
	}

	public boolean existePorAcessoUrl(String acessoUrl) {
		return count("acessoUrl", acessoUrl) > 0;
	}
}
