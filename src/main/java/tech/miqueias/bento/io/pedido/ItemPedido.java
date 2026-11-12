package tech.miqueias.bento.io.pedido;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.*;
import tech.miqueias.bento.io.pratos.Prato;
import tech.miqueias.bento.io.restaurante.Restaurante;
import tech.miqueias.bento.io.shared.exception.DomainException;

@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @OneToMany(mappedBy = "itemPedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoItem> produtos = new ArrayList<>();

    protected ItemPedido() { }
    ItemPedido(Pedido pedido, Integer quantidade) {
        if (quantidade == null || quantidade <= 0)
            throw new DomainException("A quantidade do item deve ser maior que zero.");

        this.pedido = pedido;
        this.quantidade = quantidade;
        this.preco = BigDecimal.ZERO;
    }
    public void adicionarProduto(Prato prato, BigDecimal divisao) {
        pedido.garantirEditavel();
        if (prato == null || !mesmoRestaurante(prato.getRestaurante(), pedido.getRestaurante()))
            throw new DomainException("O prato deve pertencer ao restaurante do pedido.");

        if (produtos.stream().anyMatch(produto -> produto.getPrato() == prato))
            throw new DomainException("O prato já faz parte deste item.");

        BigDecimal totalDivisoes = produtos.stream()
                                            .map(ProdutoItem::getDivisao)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                                            .add(divisao);

        if (totalDivisoes.compareTo(BigDecimal.ONE) > 0)
            throw new DomainException("A soma das divisões dos produtos não pode ultrapassar um.");

        produtos.add(new ProdutoItem(this, prato, divisao));
        recalcularPreco();

        pedido.recalcularTotais();
    }

    public void alterarQuantidade(Integer quantidade) {
        pedido.garantirEditavel();
        if (quantidade == null || quantidade <= 0)
            throw new DomainException("A quantidade do item deve ser maior que zero.");

        this.quantidade = quantidade;
        pedido.recalcularTotais();
    }

    void validarCompleto() {
        BigDecimal soma = produtos.stream()
                                    .map(ProdutoItem::getDivisao)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (produtos.isEmpty() || soma.compareTo(BigDecimal.ONE) != 0)
            throw new DomainException("Cada item deve possuir produtos totalizando uma porção.");
    }
    private void recalcularPreco() {
        preco = produtos.stream().map(ProdutoItem::getPrecoProporcional).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    /** Preço unitário congelado do item. */
    public BigDecimal getPreco() {
        return preco;
    }

    public BigDecimal getSubtotal() {
        return preco.multiply(BigDecimal.valueOf(quantidade));
    }

    public List<ProdutoItem> getProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    private boolean mesmoRestaurante(Restaurante primeiro, Restaurante segundo) {
        return primeiro == segundo || (primeiro != null && primeiro.getId() != null && primeiro.getId().equals(segundo.getId()));
    }
}
