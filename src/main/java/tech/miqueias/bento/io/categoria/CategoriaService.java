package tech.miqueias.bento.io.categoria;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import tech.miqueias.bento.io.categoria.dto.*;
import tech.miqueias.bento.io.pratos.PratoRepository;
import tech.miqueias.bento.io.restaurante.*;
import tech.miqueias.bento.io.shared.exception.*;
import tech.miqueias.bento.io.shared.tenant_context.AcessoRestauranteService;
import tech.miqueias.bento.io.usuario.Role;

@ApplicationScoped public class CategoriaService {

    @Inject
    CategoriaRepository categorias;

    @Inject
    RestauranteRepository restaurantes;

    @Inject
    PratoRepository pratos;

    @Inject
    AcessoRestauranteService acesso;

    public List<CategoriaResponseDto> listar(Long restauranteId) {
        return categorias.listarPorRestaurante(restauranteId)
                .stream()
                .map(this::dto)
                .toList();
    }

    public CategoriaResponseDto buscar(Long restauranteId, Long id) {
        return dto(encontrar(restauranteId, id));
    }

    @Transactional
    public CategoriaResponseDto criar(Long restauranteId, CategoriaRequestDto request) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE, Role.FUNCIONARIO);
        Restaurante restaurante = restaurantes
                .buscarPorId(restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante", restauranteId));

        Categoria categoria = new Categoria(
                restaurante,
                request.nome(),
                request.descricao()
        );

        categorias.persist(categoria);
        return dto(categoria);
    }

    @Transactional
    public CategoriaResponseDto atualizar(Long restauranteId, Long id, CategoriaRequestDto request) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE, Role.FUNCIONARIO);
        Categoria categoria = encontrar(restauranteId, id);

        categoria.atualizar(request.nome(), request.descricao());

        return dto(categoria);
    }

    @Transactional
    public void excluir(Long restauranteId, Long id) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE);
        if (pratos.existePorCategoria(id))
            throw new DomainException("Não é possível excluir uma categoria que possui pratos.");

        categorias.delete(encontrar(restauranteId, id));
    }

    private Categoria encontrar(Long restauranteId, Long id) {
        return categorias
                .buscarPorIdERestaurante(id, restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", id));
    }

    private CategoriaResponseDto dto(Categoria c) {
        return new CategoriaResponseDto(
                c.getId(),
                c.getRestaurante().getId(),
                c.getNome(),
                c.getDescricao()
        );
    }
}
