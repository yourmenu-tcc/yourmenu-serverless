package tech.miqueias.bento.io.pratos;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import tech.miqueias.bento.io.categoria.Categoria;
import tech.miqueias.bento.io.categoria.CategoriaRepository;
import tech.miqueias.bento.io.pratos.dto.*;
import tech.miqueias.bento.io.restaurante.*;
import tech.miqueias.bento.io.shared.exception.*;
import tech.miqueias.bento.io.shared.tenant_context.AcessoRestauranteService;
import tech.miqueias.bento.io.usuario.Role;
import tech.miqueias.bento.io.pedido.ProdutoItemRepository;

@ApplicationScoped public class PratoService {

    @Inject
    PratoRepository pratos;

    @Inject
    CategoriaRepository categorias;

    @Inject
    RestauranteRepository restaurantes;

    @Inject
    ProdutoItemRepository produtosEmPedido;

    @Inject
    AcessoRestauranteService acesso;

    public List<PratoResponseDto> listar(Long restauranteId) {
        return pratos.listarPorRestaurante(restauranteId)
                .stream()
                .map(this::dto)
                .toList();
    }

    public PratoResponseDto buscar(Long restauranteId, Long id) {
        return dto(encontrar(restauranteId, id));
    }

    @Transactional public PratoResponseDto criar(Long restauranteId, PratoRequestDto r) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE, Role.FUNCIONARIO);
        Restaurante restaurante = restaurantes
                .buscarPorId(restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante", restauranteId));
        Categoria categoria = categoria(restauranteId, r.categoriaId());
        Prato prato = new Prato(
                r.nome(),
                r.descricao(),
                categoria,
                restaurante,
                r.preco()
        );
        prato.setImagemUrl(r.imagemUrl());
        pratos.persist(prato);

        return dto(prato);
    }

    @Transactional public PratoResponseDto atualizar(Long restauranteId, Long id, PratoRequestDto r) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE, Role.FUNCIONARIO);
        Prato prato = encontrar(restauranteId, id);
        prato.atualizar(
                r.nome(),
                r.descricao(),
                categoria(restauranteId, r.categoriaId()),
                r.preco(),
                r.imagemUrl()
        );

        return dto(prato);
    }

    @Transactional public void excluir(Long restauranteId, Long id) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE);
        Prato prato = encontrar(restauranteId, id);

        if (produtosEmPedido.existePorPrato(id))
            throw new DomainException("Não é possível excluir um prato que já foi incluído em pedidos.");

        pratos.delete(prato);
    }

    private Prato encontrar(Long restauranteId, Long id) {
        return pratos
                .buscarPorIdERestaurante(id, restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prato", id));
    }

    private Categoria categoria(Long restauranteId, Long id) {
        return categorias
                .buscarPorIdERestaurante(id, restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", id));
    }

    private PratoResponseDto dto(Prato p) {
        return new PratoResponseDto(
                p.getId(),
                p.getRestaurante().getId(),
                p.getCategoria().getId(),
                p.getNome(),
                p.getDescricao(),
                p.getPreco(),
                p.getImagemUrl());
    }
}
