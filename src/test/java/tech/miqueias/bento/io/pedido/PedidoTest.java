package tech.miqueias.bento.io.pedido;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import tech.miqueias.bento.io.categoria.Categoria;
import tech.miqueias.bento.io.entrega.ZonaDeEntrega;
import tech.miqueias.bento.io.pratos.Prato;
import tech.miqueias.bento.io.restaurante.Restaurante;
import tech.miqueias.bento.io.shared.exception.DomainException;

class PedidoTest {
    @Test
    void deveCalcularTotalCongelandoPrecoDoPratoETaxaDeEntrega() {
        Restaurante restaurante = restaurante();
        Categoria categoria = new Categoria(restaurante, "Pizzas", "");
        Prato pizza = new Prato("Mussarela", "Pizza", categoria, restaurante, new BigDecimal("40.00"));
        Pedido pedido = new Pedido(restaurante, new PedidoCliente("Ana", "Silva", "85999999999"), TipoEntrega.ENTREGA);
        pedido.definirEndereco(new EnderecoPedido(new ZonaDeEntrega(restaurante, "Centro", new BigDecimal("7.50")), "60000-000", "Fortaleza", "Rua A", "10", "Centro", null, null));
        ItemPedido item = pedido.adicionarItem(2);
        item.adicionarProduto(pizza, BigDecimal.ONE);

        assertEquals(new BigDecimal("80.00"), pedido.getPrecoProdutos());
        assertEquals(new BigDecimal("87.50"), pedido.getPreco());
        pedido.avancarPara(StatusPedido.RECEBIDO);
        assertEquals(StatusPedido.RECEBIDO, pedido.getStatus());
    }

    @Test
    void deveImpedirRecebimentoDeItemComDivisoesIncompletas() {
        Restaurante restaurante = restaurante();
        Categoria categoria = new Categoria(restaurante, "Pizzas", "");
        Prato pizza = new Prato("Mussarela", "Pizza", categoria, restaurante, new BigDecimal("40.00"));
        Pedido pedido = new Pedido(restaurante, new PedidoCliente("Ana", "Silva", "85999999999"), TipoEntrega.RETIRADA);
        ItemPedido item = pedido.adicionarItem(1);
        item.adicionarProduto(pizza, new BigDecimal("0.50"));

        assertThrows(DomainException.class, () -> pedido.avancarPara(StatusPedido.RECEBIDO));
    }

    private Restaurante restaurante() {
        return new Restaurante("Ponto do Sabor", "", "Rua A", LocalTime.of(10, 0), LocalTime.of(22, 0), 30, "logo", "banner", "ponto-do-sabor");
    }
}
