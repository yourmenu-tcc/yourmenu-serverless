package tech.miqueias.bento.io.pedido;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import tech.miqueias.bento.io.entrega.ZonaDeEntrega;
import tech.miqueias.bento.io.entrega.ZonaDeEntregaRepository;
import tech.miqueias.bento.io.pedido.dto.*;
import tech.miqueias.bento.io.pratos.Prato;
import tech.miqueias.bento.io.pratos.PratoRepository;
import tech.miqueias.bento.io.restaurante.Restaurante;
import tech.miqueias.bento.io.restaurante.RestauranteRepository;
import tech.miqueias.bento.io.shared.exception.DomainException;
import tech.miqueias.bento.io.shared.exception.RecursoNaoEncontradoException;
import tech.miqueias.bento.io.shared.tenant_context.AcessoRestauranteService;
import tech.miqueias.bento.io.usuario.Role;

@ApplicationScoped
public class PedidoService {

    @Inject
    PedidoRepository pedidos;

    @Inject
    RestauranteRepository restaurantes;

    @Inject
    PratoRepository pratos;

    @Inject
    ZonaDeEntregaRepository zonas;

    @Inject
    AcessoRestauranteService acesso;

    @Transactional
    public PedidoResponseDto criar(Long restauranteId, PedidoRequestDto request) {
        Restaurante restaurante =
                restaurantes
                        .buscarPorId(restauranteId)
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante", restauranteId));
        if (!restaurante.isAberto()) {
            throw new DomainException("O restaurante não está aceitando pedidos no momento.");
        }

        Pedido pedido = new Pedido(restaurante,
                            new PedidoCliente(
                                request.cliente().primeiroNome(),
                                request.cliente().ultimoNome(),
                                request.cliente().telefone()),
                        request.tipoEntrega()
        );

        if (request.tipoEntrega() == TipoEntrega.ENTREGA) {
            if (request.endereco() == null) {
                throw new DomainException("Informe o endereço para pedidos de entrega.");
            }
            EnderecoPedidoRequestDto endereco = request.endereco();
            ZonaDeEntrega zona = zonas
                                .buscarPorIdERestaurante(endereco.zonaDeEntregaId(), restauranteId)
                                .orElseThrow(() -> new RecursoNaoEncontradoException("Zona de entrega", endereco.zonaDeEntregaId()));
            pedido.definirEndereco(new EnderecoPedido(
                                    zona,
                                    endereco.cep(),
                                    endereco.cidade(),
                                    endereco.rua(),
                                    endereco.numero(),
                                    endereco.bairro(),
                                    endereco.complemento(),
                                    endereco.referencia())
            );
        } else if (request.endereco() != null) {
            throw new DomainException("Pedidos para retirada não devem conter endereço.");
        }

        for (ItemPedidoRequestDto itemRequest : request.itens()) {
            ItemPedido item = pedido.adicionarItem(itemRequest.quantidade());
            for (ProdutoItemRequestDto produto : itemRequest.produtos()) {
                Prato prato =
                        pratos
                                .buscarPorIdERestaurante(produto.pratoId(), restauranteId)
                                .orElseThrow(
                                        () -> new RecursoNaoEncontradoException("Prato", produto.pratoId()));
                item.adicionarProduto(prato, produto.divisao());
            }
        }

        pedido.avancarPara(StatusPedido.RECEBIDO);
        pedidos.persist(pedido);
        return dto(pedido);
    }

    public List<PedidoResponseDto> listar(Long restauranteId) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE, Role.FUNCIONARIO);
        return pedidos.listarPorRestaurante(restauranteId)
                        .stream()
                        .map(this::dto)
                        .toList();
    }

    public PedidoResponseDto buscar(Long restauranteId, Long id) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE, Role.FUNCIONARIO);
        return dto(encontrar(restauranteId, id));
    }

    @Transactional
    public PedidoResponseDto alterarStatus(Long restauranteId, Long id, StatusPedido status) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE, Role.FUNCIONARIO);
        Pedido pedido = encontrar(restauranteId, id);
        pedido.avancarPara(status);
        return dto(pedido);
    }

    @Transactional
    public void cancelar(Long restauranteId, Long id) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE, Role.FUNCIONARIO);
        encontrar(restauranteId, id).cancelar();
    }

    private Pedido encontrar(Long restauranteId, Long id) {
        return pedidos
                .buscarPorIdERestaurante(id, restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido", id));
    }
    private PedidoResponseDto dto(Pedido p) {
        PedidoCliente c = p.getCliente();
        EnderecoPedido e = p.getEndereco();
        EnderecoPedidoResponseDto endereco = e == null
                        ? null
                        : new EnderecoPedidoResponseDto(
                                e.getZonaDeEntrega().getId(),
                                e.getCep(),
                                e.getCidade(),
                                e.getRua(),
                                e.getNumero(),
                                e.getBairro(),
                                e.getComplemento(),
                                e.getReferencia()
                        );

        List<ItemPedidoResponseDto> itens = p.getItens()
                .stream()
                .map(item -> new ItemPedidoResponseDto(
                                                item.getId(),
                                                item.getQuantidade(),
                                                item.getPreco(),
                                                item.getSubtotal(),
                                                item.getProdutos()
                                                        .stream()
                                                        .map(produto -> new ProdutoItemResponseDto(
                                                                                produto.getPrato().getId(),
                                                                                produto.getPrato().getNome(),
                                                                                produto.getDivisao(),
                                                                                produto.getPrecoUnitario())
                                                        ).toList())
                ).toList();

        return new PedidoResponseDto(
                p.getId(),
                p.getRestaurante().getId(),
                p.getCriadoEm(),
                p.getStatus(),
                p.getTipoEntrega(),
                p.getPrecoProdutos(),
                p.getTaxaEntrega(),
                p.getPreco(),
                new PedidoClienteRequestDto(c.getPrimeiroNome(), c.getUltimoNome(), c.getTelefone()),
                endereco,
                itens
        );
    }
}
