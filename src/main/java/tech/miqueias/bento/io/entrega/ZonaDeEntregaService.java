package tech.miqueias.bento.io.entrega;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import tech.miqueias.bento.io.entrega.dto.ZonaDeEntregaRequestDto;
import tech.miqueias.bento.io.entrega.dto.ZonaDeEntregaResponseDto;
import tech.miqueias.bento.io.pedido.EnderecoPedidoRepository;
import tech.miqueias.bento.io.restaurante.Restaurante;
import tech.miqueias.bento.io.restaurante.RestauranteRepository;
import tech.miqueias.bento.io.shared.exception.DomainException;
import tech.miqueias.bento.io.shared.exception.RecursoNaoEncontradoException;
import tech.miqueias.bento.io.shared.tenant_context.AcessoRestauranteService;
import tech.miqueias.bento.io.usuario.Role;

@ApplicationScoped
public class ZonaDeEntregaService {

    @Inject
    ZonaDeEntregaRepository zonas;

    @Inject
    RestauranteRepository restaurantes;

    @Inject
    EnderecoPedidoRepository enderecos;

    @Inject
    AcessoRestauranteService acesso;

    public List<ZonaDeEntregaResponseDto> listar(Long restauranteId) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE ,Role.FUNCIONARIO);
        return zonas.listarPorRestaurante(restauranteId).stream()
                .map(this::dto)
                .toList();
    }

    public ZonaDeEntregaResponseDto buscar(Long restauranteId, Long id) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE ,Role.FUNCIONARIO);
        return dto(encontrar(restauranteId, id));
    }

    @Transactional
    public ZonaDeEntregaResponseDto criar(Long restauranteId, ZonaDeEntregaRequestDto r) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE);

        Restaurante restaurante = restaurantes.buscarPorId(restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante", restauranteId));

        ZonaDeEntrega zona = new ZonaDeEntrega(restaurante, r.localizacao(), r.valor());
        zonas.persist(zona);

        return dto(zona);
    }

    @Transactional
    public ZonaDeEntregaResponseDto atualizar(Long restauranteId, Long id, ZonaDeEntregaRequestDto r) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE);

        ZonaDeEntrega zona = encontrar(restauranteId, id);
        zona.atualizar(r.localizacao(), r.valor());

        return dto(zona);
    }

    @Transactional
    public void excluir(Long restauranteId, Long id) {
        acesso.exigirAcesso(restauranteId, Role.GERENTE);

        ZonaDeEntrega zona = encontrar(restauranteId, id);

        if (enderecos.existePorZonaDeEntrega(id)) {
            throw new DomainException("Não é possível excluir uma zona já utilizada em pedidos.");
        }

        zonas.delete(zona);
    }

    private ZonaDeEntrega encontrar(Long restauranteId, Long id) {
        return zonas.buscarPorIdERestaurante(id, restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Zona de entrega", id));
    }

    private ZonaDeEntregaResponseDto dto(ZonaDeEntrega z) {
        return new ZonaDeEntregaResponseDto(z.getId(), z.getRestaurante().getId(), z.getLocalizacao(), z.getValor());
    }
}
