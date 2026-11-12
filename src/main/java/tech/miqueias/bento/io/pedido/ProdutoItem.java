package tech.miqueias.bento.io.pedido;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import tech.miqueias.bento.io.pratos.Prato;
import tech.miqueias.bento.io.shared.exception.DomainException;

/** Produto e preço congelado que compõem um item do pedido. */
@Entity
@Table(
        name = "produto_item",
        uniqueConstraints =
                @UniqueConstraint(
                        name = "uk_item_prato", columnNames = {"item_pedido_id", "prato_id"}))
public class ProdutoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_pedido_id", nullable = false)
    private ItemPedido itemPedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prato_id", nullable = false)
    private Prato prato;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal divisao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precoUnitario;

    protected ProdutoItem() {}

    ProdutoItem(ItemPedido itemPedido, Prato prato, BigDecimal divisao) {
        if (prato == null
                || divisao == null
                || divisao.signum() <= 0
                || divisao.compareTo(BigDecimal.ONE) > 0) {
            throw new DomainException("A divisão do prato deve estar entre zero e um.");
        }
        this.itemPedido = itemPedido;
        this.prato = prato;
        this.divisao = divisao;
        this.precoUnitario = prato.getPreco();
    }
    public Long getId() {
        return id;
    }

    public Prato getPrato() {
        return prato;
    }

    public BigDecimal getDivisao() {
        return divisao;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal getPrecoProporcional() {
        return precoUnitario.multiply(divisao);
    }
}
